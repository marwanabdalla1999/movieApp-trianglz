package com.trianglz.ui.uiModels

data class AppMoviesModel(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double,
    val voteCount: Int
)
