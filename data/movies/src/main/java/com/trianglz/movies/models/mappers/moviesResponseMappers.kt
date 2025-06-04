package com.trianglz.movies.models.mappers

import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.models.responses.MovieDto


fun MovieDto.toEntity(): MovieEntity = MovieEntity(
    id = id,
    title = title,
    posterPath = posterPath?:"",
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage
)

fun MovieDto.toDomain(): MovieDomainModel = MovieDomainModel(
    id = id,
    title = title,
    posterPath = posterPath?:"",
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage
)

