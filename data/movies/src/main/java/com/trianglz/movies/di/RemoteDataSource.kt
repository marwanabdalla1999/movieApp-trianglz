package com.trianglz.movies.di

import com.trianglz.movies.paging.MoviesMediator
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import com.trianglz.movies.remoteDataSource.MoviesRemoteDataSourceImpl
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataSource {

    @Singleton
    @Binds
    abstract fun provideRemoteMoviesDataSource(moviesRemoteDataSource: MoviesRemoteDataSourceImpl): IMoviesRemoteDataSource

    @Binds
    abstract fun bindMoviesRepository(moviesRepositoryImpl: MovieRepositoryImpl): IMoviesRepository

}