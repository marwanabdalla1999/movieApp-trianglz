package com.trianglz.cache.movies

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trianglz.cache.movies.entities.MovieEntity

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    fun pagingSource(): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("DELETE FROM movies")
    suspend fun clearAllMovies()
}