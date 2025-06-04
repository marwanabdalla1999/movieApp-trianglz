package com.trianglz.movies.usecase.popularMoviesUseCase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IGetPopularMoviesUseCase {

     operator fun invoke(): Flow<PagingData<MovieDomainModel>>

}