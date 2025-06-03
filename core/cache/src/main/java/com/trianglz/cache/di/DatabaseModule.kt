package com.trianglz.cache.di

import android.content.Context
import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Singleton
    @Provides
    fun provideMovieDao(database: AppDatabase) : MoviesDao = database.movieDao()

    @Singleton
    @Provides
    fun provideRemoteKeysDao(database: AppDatabase) : MovieRemoteKeysDao = database.movieRemoteKeysDao()


}