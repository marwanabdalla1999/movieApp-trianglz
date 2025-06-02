package com.trianglz.movies.localDataSource

import androidx.paging.PagingSource
import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys


class LocalMoviesDataSourceImpl(
    private val database:AppDatabase
) : ILocalMoviesDataSource {

    override suspend fun pagingSource(): PagingSource<Int, MovieEntity> {
        return database.movieDao().pagingSource()
    }

    override suspend fun clearAllMovies() = database.movieDao().clearAllMovies()

    override suspend fun clearAllRemoteKeys() = database.movieRemoteKeysDao().clearAllRemoteKeys()

    override suspend fun insertMovies(movies: List<MovieEntity>) = database.movieDao().insertAll(movies)

    override suspend fun insertRemoteKeys(keys: List<MovieRemoteKeys>) =
        database.movieRemoteKeysDao().insertAll(keys)

    override suspend fun getRemoteKeysByMovieId(movieId: Int): MovieRemoteKeys? =
        database.movieRemoteKeysDao().remoteKeysByMovieId(movieId)


}