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
import com.trianglz.corenetwork.NetworkConstants.Paging.PAGE_SIZE
import com.trianglz.corenetwork.NetworkConstants.Paging.PREFETCH_DISTANCE
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.paging.MoviesMediator
import com.trianglz.movies.paging.SearchMoviesPagingSource
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class MovieRepositoryImpl @Inject constructor (
    private val remoteMoviesDataSource: IMoviesRemoteDataSource,
    private val moviesDoa: MoviesDao,
    private val remoteKeysDao: MovieRemoteKeysDao,
    private val database: AppDatabase
) : IMoviesRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun getMovies(): Flow<PagingData<MovieDomainModel>> {
        val pagingSourceFactory = { moviesDoa.pagingSource() }

        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = PREFETCH_DISTANCE ,enablePlaceholders = true),
            remoteMediator = MoviesMediator(
                remoteDataSource = remoteMoviesDataSource,
                withTransaction = database::withTransaction,
                remoteKeysDao = remoteKeysDao,
                moviesDao = moviesDoa
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
            .map { pagingData ->
                pagingData.map { it.toDomain() }
            }
    }


    override suspend fun searchForMovies(query: String): Flow<PagingData<MovieDomainModel>> {
        val pagingSourceFactory = {
            SearchMoviesPagingSource(
                remoteMoviesDataSource = remoteMoviesDataSource,
                query = query
            )
        }
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

}

