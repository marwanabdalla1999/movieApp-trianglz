package com.trianglz.movies.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.withTransaction
import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.corenetwork.NetworkConstants.Paging.INITIAL_LOAD_SIZE
import com.trianglz.corenetwork.NetworkConstants.Paging.PAGE_SIZE
import com.trianglz.corenetwork.NetworkConstants.Paging.PREFETCH_DISTANCE
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.paging.MoviesMediator
import com.trianglz.movies.paging.SearchMoviesPagingSource
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * A concrete implementation of [IMoviesRepository] that uses:
 *  • Paging 3 + Room + RemoteMediator to load “popular” movies (`getMovies()`),
 *  • A simple PagingSource for searching (`searchForMovies(query)`),
 *  • A flow‐based single‐item fetch for details (`getMovieDetails(movieId)`).
 *
 * Internally, “popular movies” are backed by a local Room cache (via [MoviesDao] and [MovieRemoteKeysDao])
 * and a remote REST source ([IMoviesRemoteDataSource]) through [MoviesMediator].
 *
 * Search is performed entirely through a custom [SearchMoviesPagingSource], which directly queries
 * the [IMoviesRemoteDataSource] without caching to Room.
 *
 * Note on threading: most work happens on background threads, and each public function
 * returns a cold [Flow], which is shifted to [Dispatchers.IO] at the repository boundary.
 *
 * Dependencies are injected via Dagger‐Hilt.
 */

class MovieRepositoryImpl @Inject constructor(
    private val remoteMoviesDataSource: IMoviesRemoteDataSource,
    private val moviesDoa: MoviesDao,
    private val remoteKeysDao: MovieRemoteKeysDao,
    private val database: AppDatabase
) : IMoviesRepository {

    /**
     * Returns a [Flow] of paged “popular” movies as [PagingData]<[MovieDomainModel]>.
     *
     * Internally, this sets up a [Pager] with:
     *  • A [MoviesMediator] (RemoteMediator) that:
     *      – Fetches page N from network via [IMoviesRemoteDataSource.getMovies],
     *      – Stores [MovieEntity] and [MovieRemoteKeys] into Room in a transaction,
     *      – Reads the “last remote key” from [MovieRemoteKeysDao.getLastRemoteKey()]
     *        to compute what page to load next.
     *  • A paging source factory: `moviesDoa.pagingSource()`.
     *
     * @return a cold [Flow] that, when collected, will emit new [PagingData] snapshots.
     *         Each [PagingData] is immediately mapped from [MovieEntity] → [MovieDomainModel]
     *         via the `map { it.toDomain() }` call.
     */
    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MovieDomainModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = INITIAL_LOAD_SIZE,
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = true
            ), remoteMediator = MoviesMediator(
                remoteDataSource = remoteMoviesDataSource,
                withTransaction = database::withTransaction,
                remoteKeysDao = remoteKeysDao,
                moviesDao = moviesDoa
            ), pagingSourceFactory = { moviesDoa.pagingSource() }
        ).flow.map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }

    /**
     * Returns a [Flow] of paged search results for the given [query], as [PagingData]<[MovieDomainModel]>.
     *
     * This uses a one‐off [SearchMoviesPagingSource], which calls the remote data source
     * ([IMoviesRemoteDataSource.searchMovies]) directly, without writing to Room.
     * Placeholders are disabled.
     *
     * @param query the text to search for; passed directly into the remote API.
     * @return a cold [Flow] of [PagingData] of [MovieDomainModel].
     */
    override suspend fun searchForMovies(query: String): Flow<PagingData<MovieDomainModel>> {
        val pagingSourceFactory = {
            SearchMoviesPagingSource(
                remoteMoviesDataSource = remoteMoviesDataSource, query = query
            )
        }
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    /**
     * Returns a [Flow] that emits exactly one [MovieDomainModel] for the given [movieId].
     * The remote data source is called (via [IMoviesRemoteDataSource.getMoviesDetails]),
     * and the raw DTO is mapped to [MovieDomainModel] and emitted.
     *
     * @param movieId the ID of the movie to fetch details for.
     * @return a cold [Flow] which emits the [MovieDomainModel] once and then completes.
     */
    override suspend fun getMovieDetails(movieId: Int): Flow<MovieDomainModel> = flow {
       val response = remoteMoviesDataSource.getMoviesDetails(movieId = movieId)

        response?.let { emit(it.toDomain()) }
    }

}

