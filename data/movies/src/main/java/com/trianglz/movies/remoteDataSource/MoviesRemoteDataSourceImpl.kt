package com.trianglz.movies.remoteDataSource

import com.trianglz.corenetwork.NetworkHelper
import com.trianglz.movies.models.responses.MoviesResponse
import com.trianglz.movies.remoteDataSource.apiServices.MovieApiService

class MoviesRemoteDataSourceImpl(private val apiService: MovieApiService) :
    IMoviesRemoteDataSource {

    override suspend fun getMovies(page: Int,pageSize: Int) : MoviesResponse? =
        NetworkHelper.executeCall {
            apiService.getMovies(page = page, pageSize = pageSize)
        }


    override suspend fun searchForMovies() {
    }

    override suspend fun getMoviesDetails() {
    }

}
