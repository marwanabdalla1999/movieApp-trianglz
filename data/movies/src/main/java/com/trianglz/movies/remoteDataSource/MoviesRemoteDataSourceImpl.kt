package com.trianglz.movies.remoteDataSource

import com.trianglz.corenetwork.NetworkHelper
import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.models.responses.MoviesResponse
import com.trianglz.movies.apiServices.MovieApiService
import javax.inject.Inject

class MoviesRemoteDataSourceImpl @Inject constructor(private val apiService: MovieApiService) :
    IMoviesRemoteDataSource {

    override suspend fun getMovies(page: Int, pageSize: Int): MoviesResponse? =
        NetworkHelper.executeCall {
            apiService.getMovies(page = page, pageSize = pageSize)
        }


    override suspend fun searchForMovies(query: String, page: Int): MoviesResponse? =
        NetworkHelper.executeCall {
            apiService.searchMovies(query = query, page = page)
        }


    override suspend fun getMoviesDetails(movieId: Int): MovieDto? = NetworkHelper.executeCall {
        apiService.getMovieDetails(movieId = movieId)
    }

}
