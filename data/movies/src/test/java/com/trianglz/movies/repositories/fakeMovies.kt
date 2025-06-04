package com.trianglz.movies.repositories

import com.trianglz.movies.models.mappers.toEntity
import com.trianglz.movies.models.responses.MovieDto

val movieDto = MovieDto(
    id = 1,
    title = "Test Movie 1",
    posterPath = "",
    releaseDate = "",
    overview = "",
    voteAverage = 8.0,
    voteCount = 100,
    popularity = 50.0,
    adult = false,
    originalLanguage = "en",
    backdropPath = "",
    originalTitle = ""
)
val fakeMovies = listOf(
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto,
    movieDto
)
