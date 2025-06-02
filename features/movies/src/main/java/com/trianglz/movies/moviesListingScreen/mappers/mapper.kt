package com.trianglz.movies.moviesListingScreen.mappers

import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.ui.uiModels.AppMoviesModel


fun MovieDomainModel.toAppUiModel() = AppMoviesModel(
    title = title,
    poster = posterPath,
    releaseDate = releaseDate,
    overview = overview,
    voteAverage = voteAverage,
    voteCount = voteCount
)