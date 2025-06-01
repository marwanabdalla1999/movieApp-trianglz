package com.trianglz.movies.remoteDataSource

import com.trianglz.movies.models.responses.MoviesResponse

interface IMoviesRemoteDataSource {

    suspend fun getMovies(page:Int,pageSize:Int): MoviesResponse?

    suspend fun searchForMovies()


    suspend fun getMoviesDetails()
}