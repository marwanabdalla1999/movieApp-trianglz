package com.trianglz.movies.localDataSource

import androidx.paging.PagingSource
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys


interface ILocalMoviesDataSource {

    suspend fun pagingSource(): PagingSource<Int, MovieEntity>
    suspend fun clearAllMovies()
    suspend fun clearAllRemoteKeys()
    suspend fun insertMovies(movies: List<MovieEntity>)
    suspend fun insertRemoteKeys(keys: List<MovieRemoteKeys>)
    suspend fun getRemoteKeysByMovieId(movieId: Int): MovieRemoteKeys?

}