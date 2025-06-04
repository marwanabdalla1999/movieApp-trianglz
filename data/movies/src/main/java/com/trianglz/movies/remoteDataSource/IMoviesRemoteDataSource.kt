package com.trianglz.movies.remoteDataSource

import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.models.responses.MoviesResponse

interface IMoviesRemoteDataSource {

    suspend fun getMovies(page:Int,pageSize:Int): MoviesResponse?

    suspend fun searchForMovies(query: String,page: Int): MoviesResponse?


    suspend fun getMoviesDetails(movieId: Int) : MovieDto?
}