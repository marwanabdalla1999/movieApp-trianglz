package com.trianglz.movies.remoteDataSource.apiServices

import com.trianglz.movies.models.responses.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("/movies")
    suspend fun getMovies(
        @Query("api_key") apiKey: String = "",
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): Response<MoviesResponse>


    @GET("/search")
    suspend fun searchMovies(
        @Query("query") query: String, @Query("page") page: Int = 1
    ): MoviesResponse


    @GET("/movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): MoviesResponse

} 