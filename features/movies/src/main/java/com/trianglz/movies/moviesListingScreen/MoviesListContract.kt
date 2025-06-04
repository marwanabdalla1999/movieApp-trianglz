package com.trianglz.movies.moviesListingScreen

import androidx.paging.PagingData
import com.trianglz.ui.base.ViewEvent
import com.trianglz.ui.base.ViewSideEffect
import com.trianglz.ui.base.ViewState
import com.trianglz.ui.uiModels.AppMoviesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


sealed class MovieListState:ViewState {
    data object Ideal : MovieListState()
    data class Success(
        val movies: Flow<PagingData<AppMoviesModel>>,
        val searchedMovieList: Flow<PagingData<AppMoviesModel>>,
        val query: String
    ) : MovieListState()
}

sealed class MovieListEvents : ViewEvent {
    data object LoadMovies : MovieListEvents()
    data class MovieClicked(val movieId: Int) : MovieListEvents()
    data class SearchMovies(val query: String) : MovieListEvents()
}

sealed class MovieListEffects : ViewSideEffect {
    sealed class Navigation : MovieListEffects() {
        data class MovieDetails(val movieId: Int) : Navigation()
    }
}