package com.trianglz.movies.localDataSource

import androidx.paging.PagingSource
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.localDatabase.movies.entities.MovieEntity
import com.trianglz.localDatabase.movies.entities.MovieRemoteKeys

class LocalMoviesDataSourceImpl(
    private val moviesDao: MoviesDao, private val movieRemoteKeysDao: MovieRemoteKeysDao
) : ILocalMoviesDataSource {
    override suspend fun pagingSource(): PagingSource<Int, MovieEntity> {
        return moviesDao.pagingSource()
    }

    override suspend fun clearAllMovies() = moviesDao.clearAllMovies()

    override suspend fun clearAllRemoteKeys() = moviesDao.clearAllMovies()

    override suspend fun insertMovies(movies: List<MovieEntity>) = moviesDao.insertAll(movies)

    override suspend fun insertRemoteKeys(keys: List<MovieRemoteKeys>) =
        movieRemoteKeysDao.insertAll(keys)

    override suspend fun getRemoteKeysByMovieId(movieId: Int): MovieRemoteKeys? =
        movieRemoteKeysDao.remoteKeysByMovieId(movieId)


}