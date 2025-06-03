package com.trianglz.movies.usecase.searchForMovieUseCase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import kotlinx.coroutines.flow.Flow


class SearchForMoviesUseCaseImpl(private val movieRepository: IMoviesRepository) :
    ISearchForMoviesUseCase {
    override suspend operator fun invoke(query: String): Flow<PagingData<MovieDomainModel>> {
        return movieRepository.searchForMovies(query)
    }
} 