package com.trianglz.movies.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys
import com.trianglz.corenetwork.NetworkConstants.Paging.PAGE_SIZE
import com.trianglz.movies.models.mappers.toEntity
import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
/**
 * [MoviesMediator] is a [RemoteMediator] implementation responsible for
 * synchronizing paginated movie data between a remote API and local Room cache.
 *
 * It supports three [LoadType]s: REFRESH, APPEND (for pagination), and ignores PREPEND.
 *
 * @property remoteDataSource The remote source to fetch paginated movies.
 * @property moviesDao DAO to interact with the local movies Room table.
 * @property remoteKeysDao DAO to manage pagination keys for each movie.
 * @property withTransaction A transactional wrapper to perform DB operations safely.
 */
@OptIn(ExperimentalPagingApi::class)
class MoviesMediator @Inject constructor(
    private val remoteDataSource: IMoviesRemoteDataSource,
    private val moviesDao: MoviesDao,
    private val remoteKeysDao: MovieRemoteKeysDao,
    private val withTransaction: suspend (suspend () -> Unit) -> Unit
) : RemoteMediator<Int, MovieEntity>() {

    /**
     * Called by Paging 3 to load data from the network and update the local database.
     *
     * @param loadType Indicates whether this is a REFRESH, PREPEND, or APPEND operation.
     * @param state Provides information about the current paging state.
     *
     * @return [MediatorResult] indicating success or error.
     */
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = getPageToLoad(loadType) ?: return MediatorResult.Success(true)

            val response = remoteDataSource.getMovies(page = page, pageSize = PAGE_SIZE)
            val movies = response?.results.orEmpty()
            val isEnd = (response?.page ?: 1) >= (response?.totalPages ?: 1)

            persistToDb(loadType, page, movies, isEnd)

            MediatorResult.Success(endOfPaginationReached = isEnd)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    /**
     * Determines which page number to load based on the [LoadType].
     * - REFRESH always starts from page 1.
     * - APPEND loads the next page using [MovieRemoteKeys].
     * - PREPEND is ignored (returns null).
     *
     * @return The page number to load, or null if no further pages should be loaded.
     */
    private suspend fun getPageToLoad(
        loadType: LoadType
    ): Int? {
        return when (loadType) {
            LoadType.REFRESH -> 1

            LoadType.PREPEND -> null

            LoadType.APPEND -> {
                val remoteKeys = remoteKeysDao.getLastRemoteKey()
                remoteKeys?.nextPage
            }
        }
    }

    /**
     * Persists movie data and remote keys to the local database within a transaction.
     *
     * @param loadType The current load type (used to determine whether to clear old data).
     * @param page The page number being persisted.
     * @param movies The list of [MovieDto]s fetched from the remote source.
     * @param isEnd Whether this is the last page.
     */
    private suspend fun persistToDb(
        loadType: LoadType,
        page: Int,
        movies: List<MovieDto>,
        isEnd: Boolean
    ) = withTransaction {
        if (loadType == LoadType.REFRESH) {
            moviesDao.clearAllMovies()
            remoteKeysDao.clearAllRemoteKeys()
        }

        val entities = movies.map { it.toEntity() }
        moviesDao.insertAll(entities)

        val keys = movies.map {
            MovieRemoteKeys(
                movieId = it.id,
                prevPage = if (page == 1) null else page - 1,
                nextPage = if (isEnd) null else page + 1
            )
        }
        remoteKeysDao.insertAll(keys)
    }
}

