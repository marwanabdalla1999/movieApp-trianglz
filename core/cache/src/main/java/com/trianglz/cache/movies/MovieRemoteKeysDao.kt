package com.trianglz.cache.movies

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.trianglz.cache.movies.entities.MovieRemoteKeys

/**
 * DAO for accessing remote pagination keys used by the [MoviesMediator].
 */
@Dao
interface MovieRemoteKeysDao {
    /**
     * Inserts a list of [MovieRemoteKeys] into the database.
     */
    @Query("SELECT * FROM movies_remote_keys ORDER BY nextPage DESC LIMIT 1")
    suspend fun getLastRemoteKey(): MovieRemoteKeys?

    /**
     * Retrieves the remote key for the last item in the local database.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<MovieRemoteKeys>)

    /**
     * Clears all remote keys.
     */
    @Query("DELETE FROM movies_remote_keys")
    suspend fun clearAllRemoteKeys()
}