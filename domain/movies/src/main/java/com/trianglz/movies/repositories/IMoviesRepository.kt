package com.trianglz.movies.repositories

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import kotlinx.coroutines.flow.Flow

interface IMoviesRepository {
   fun getMovies(): Flow<PagingData<MovieDomainModel>>

   suspend  fun searchForMovies(query: String): Flow<PagingData<MovieDomainModel>>

   suspend fun getMovieDetails(movieId: Int): Flow<MovieDomainModel>

} 