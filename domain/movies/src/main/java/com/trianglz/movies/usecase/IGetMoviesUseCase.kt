package com.trianglz.movies.usecase

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IGetMoviesUseCase {

    suspend operator fun invoke(): Flow<PagingData<MovieDomainModel>>

}