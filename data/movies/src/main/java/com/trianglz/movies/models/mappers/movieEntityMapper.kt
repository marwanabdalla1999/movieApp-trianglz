package com.trianglz.movies.models.mappers

import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.movies.models.MovieDomainModel

fun MovieEntity.toDomain(): MovieDomainModel = MovieDomainModel(
    id = id,
    title = title,
    posterPath = posterPath,
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount,
    popularity = popularity,
    adult = adult,
    originalLanguage = originalLanguage
)