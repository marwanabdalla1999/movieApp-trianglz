package com.trianglz.movies.usecase.popularMoviesUseCase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow


class GetPopularMoviesUseCaseImpl(private val movieRepository: IMoviesRepository):
    IGetPopularMoviesUseCase {
    override operator fun invoke(): Flow<PagingData<MovieDomainModel>> {
        return movieRepository.getMovies()
    }
} 