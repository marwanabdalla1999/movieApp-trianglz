package com.trianglz.cache.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trianglz.cache.movies.entities.MovieRemoteKeys

@Dao
interface MovieRemoteKeysDao {
    @Query("SELECT * FROM movies_remote_keys ORDER BY nextPage DESC LIMIT 1")
    suspend fun getLastRemoteKey(): MovieRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<MovieRemoteKeys>)

    @Query("DELETE FROM movies_remote_keys")
    suspend fun clearAllRemoteKeys()
}