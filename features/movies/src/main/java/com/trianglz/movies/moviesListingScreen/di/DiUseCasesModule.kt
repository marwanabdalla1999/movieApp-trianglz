package com.trianglz.movies.moviesListingScreen.di

import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.usecase.GetMoviesUseCaseImpl
import com.trianglz.movies.usecase.IGetMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DiUseCasesModule {

    @Singleton
    @Provides
    fun provideGetMoviesUseCase(moviesRepository: IMoviesRepository): IGetMoviesUseCase =
        GetMoviesUseCaseImpl(moviesRepository)


}
