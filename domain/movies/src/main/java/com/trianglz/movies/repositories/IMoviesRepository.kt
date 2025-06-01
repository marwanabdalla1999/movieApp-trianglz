package com.trianglz.movies.repositories

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {
   suspend fun getMovies(): Flow<PagingData<MovieDomainModel>>

} 