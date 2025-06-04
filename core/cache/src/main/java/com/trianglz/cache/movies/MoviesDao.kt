package com.trianglz.cache.movies

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trianglz.cache.movies.entities.MovieEntity

@Dao
interface MoviesDao {
    /**
     * Provides a paging source to load movies from the local database.
     */
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    /**
     * Inserts a list of [MovieEntity] into the database.
     * If a movie already exists, it will be replaced.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    /**
     * Clears all movies from the local database.
     */
    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
}