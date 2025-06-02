package com.trianglz.movies.usecase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow


class GetMoviesUseCaseImpl(private val movieRepository: IMoviesRepository):IGetMoviesUseCase {
    override suspend operator fun invoke(): Flow<PagingData<MovieDomainModel>> {
        return movieRepository.getMovies()
    }
} 