package com.trianglz.movies.usecase.movieDetailsUseCase

import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow


class MovieDetailsUseCaseImpl(private val movieRepository: IMoviesRepository):
    IMovieDetailsUseCase {
    override suspend operator fun invoke(movieId: Int): Flow<MovieDomainModel> {
        return movieRepository.getMovieDetails(movieId)
    }
}