package com.trianglz.movies.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.movies.localDataSource.ILocalMoviesDataSource
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.pagingMediator.MoviesRemoteMediator
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MovieRepositoryImpl (
    private val remoteDataSource: IMoviesRemoteDataSource,
    private val localMoviesDataSourceImpl: ILocalMoviesDataSource,
    private val database: AppDatabase
) : IMoviesRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getMovies(): Flow<PagingData<MovieDomainModel>> {
        val pagingSourceFactory = { database.movieDao().pagingSource() }

        return Pager(
            config = PagingConfig(
                pageSize = 20, enablePlaceholders = false
            ), remoteMediator = MoviesRemoteMediator(
                remoteDataSource = remoteDataSource,
                localMoviesDataSource = localMoviesDataSourceImpl,
                database = database
            ), pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingDataOfEntities ->
            pagingDataOfEntities.map { entity ->
                entity.toDomain()
            }
        }
    }

}

