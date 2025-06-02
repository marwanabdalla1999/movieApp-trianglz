package com.trianglz.cache.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.cache.movies.entities.MovieRemoteKeys

@Database(
    entities = [MovieEntity::class, MovieRemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MoviesDao
    abstract fun movieRemoteKeysDao(): MovieRemoteKeysDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

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
