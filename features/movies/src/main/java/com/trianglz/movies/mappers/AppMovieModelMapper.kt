package com.trianglz.movies.mappers

import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.ui.models.AppMovieModel


fun MovieDomainModel.toAppUiModel() = AppMovieModel(
    id = id,
    title = title,
    poster = posterPath,
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage
)