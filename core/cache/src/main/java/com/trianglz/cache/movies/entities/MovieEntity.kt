package com.trianglz.localDatabase.movies.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    val originalLanguage: String,
    val page: Int
)


@Entity(tableName = "movies_remote_keys")
data class MovieRemoteKeys(
    @PrimaryKey val movieId: Int,
    val prevPage: Int?,
    val nextPage: Int?
)