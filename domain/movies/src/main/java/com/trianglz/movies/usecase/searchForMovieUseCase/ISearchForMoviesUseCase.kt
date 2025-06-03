package com.trianglz.movies.usecase.searchForMovieUseCase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface ISearchForMoviesUseCase {

    suspend operator fun invoke(query: String): Flow<PagingData<MovieDomainModel>>

}