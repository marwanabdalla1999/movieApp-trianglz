package com.trianglz.cache.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys
/**
 * The main Room database for the Movies app.
 *
 * It provides access to the local cache of movies and pagination keys
 * through [MoviesDao] and [MovieRemoteKeysDao].
 *
 * This database includes:
 * - [MovieEntity] for storing movie data.
 * - [MovieRemoteKeys] for managing pagination keys for [RemoteMediator].
 *
 * @see MoviesDao
 * @see MovieRemoteKeysDao
 * @see MovieEntity
 * @see MovieRemoteKeys
 */

@Database(
    entities = [MovieEntity::class, MovieRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Provides DAO for accessing movies.
     */
    abstract fun movieDao(): MoviesDao

    /**
     * Provides DAO for accessing pagination keys.
     */
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
    companion object {

        // Volatile to ensure visibility across threads.
        @Volatile private var INSTANCE: AppDatabase? = null

        /**
         * Singleton instance of [AppDatabase].
         *
         * This ensures that the database is only instantiated once across the app,
         * using the application context.
         *
         * @param context The context used to create the database.
         * @return The singleton [AppDatabase] instance.
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "movies_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
