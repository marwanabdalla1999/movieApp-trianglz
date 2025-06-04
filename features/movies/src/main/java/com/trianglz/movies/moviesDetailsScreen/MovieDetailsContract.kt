package com.trianglz.movies.moviesDetailsScreen

import com.trianglz.ui.base.ViewEvent
import com.trianglz.ui.base.ViewSideEffect
import com.trianglz.ui.base.ViewState
import com.trianglz.ui.uiModels.AppMoviesModel


sealed class MoviesDetailsState:ViewState {
    data object Ideal: MoviesDetailsState()
    data object Loading: MoviesDetailsState()
    data class SuccessMovieLoaded(val movie: AppMoviesModel): MoviesDetailsState()
}

sealed class MoviesDetailsEvents : ViewEvent {
    data class LoadMovie(val id:Int) : MoviesDetailsEvents()
}

sealed class MoviesDetailsEffects : ViewSideEffect {
    data class Error(val message: String): MoviesDetailsEffects()
}