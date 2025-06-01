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
        TODO("Not yet implemented")
    }

    override suspend fun getMoviesDetails() {
        TODO("Not yet implemented")
    }

}
