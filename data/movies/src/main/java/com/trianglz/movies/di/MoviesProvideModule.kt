package com.trianglz.movies.di

import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import com.trianglz.movies.remoteDataSource.MoviesRemoteDataSourceImpl
import com.trianglz.movies.apiServices.MovieApiService
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.repository.MovieRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MoviesProvideModule {



    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): MovieApiService {
        return retrofit.create(MovieApiService::class.java)
    }


}
