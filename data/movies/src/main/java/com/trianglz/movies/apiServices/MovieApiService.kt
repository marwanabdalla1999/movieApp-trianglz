package com.trianglz.movies.apiServices

import androidx.paging.PagingSource
import com.trianglz.corenetwork.NetworkConstants
import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.models.responses.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApiService {

    @GET("movie/popular")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 20
    ): Response<MoviesResponse?>


    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String, @Query("page") page: Int = 1
    ): Response<MoviesResponse?>


    @GET("movie/{movieId}")
    suspend fun getMovieDetails(@Path("movieId") movieId: Int): Response<MovieDto?>

} 