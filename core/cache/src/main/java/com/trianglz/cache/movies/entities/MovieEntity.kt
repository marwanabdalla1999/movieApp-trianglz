package com.trianglz.cache.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Local Room entity representing a movie.
 *
 * This data class holds movie information used throughout the app and is
 * populated by the remote source via [MoviesDao.toEntity].
 *
 * @property id Unique ID of the movie.
 * @property title Title of the movie.
 * @property poster Poster image path.
 * @property releaseDate Release date of the movie.
 * @property overview Overview or summary of the movie.
 * @property voteAverage Average vote rating (0-10).
 */
@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val posterPath: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int,
    val popularity: Double,
    val adult: Boolean,
    val originalLanguage: String
)

@Entity(tableName = "movies_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey val movieId: Int,
    val prevPage: Int?,
    val nextPage: Int?
)