package com.trianglz.movies.moviesListingScreen.di

import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.usecase.popularMoviesUseCase.GetPopularMoviesUseCaseImpl
import com.trianglz.movies.usecase.popularMoviesUseCase.IGetPopularMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.ISearchForMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.SearchForMoviesUseCaseImpl
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
    fun provideGetMoviesUseCase(moviesRepository: IMoviesRepository): IGetPopularMoviesUseCase =
        GetPopularMoviesUseCaseImpl(moviesRepository)



    @Singleton
    @Provides
    fun provideSearchForMoviesUseCase(moviesRepository: IMoviesRepository): ISearchForMoviesUseCase =
        SearchForMoviesUseCaseImpl(moviesRepository)
}
