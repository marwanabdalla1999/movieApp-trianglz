package com.trianglz.ui.navigations

import kotlinx.serialization.Serializable

@Serializable
object MoviesListing


@Serializable
data class MovieDetails(val movieId:Int)