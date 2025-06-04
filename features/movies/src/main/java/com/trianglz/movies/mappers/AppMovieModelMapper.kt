package com.trianglz.movies.mappers

import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.ui.uiModels.AppMoviesModel


fun MovieDomainModel.toAppUiModel() = AppMoviesModel(
    id = id,
    title = title,
    poster = posterPath,
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount
)