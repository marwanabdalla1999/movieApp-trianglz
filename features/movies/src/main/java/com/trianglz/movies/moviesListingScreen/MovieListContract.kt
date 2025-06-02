package com.trianglz.movies.moviesListingScreen

import androidx.paging.PagingData
import com.trianglz.ui.base.ViewEvent
import com.trianglz.ui.base.ViewSideEffect
import com.trianglz.ui.base.ViewState
import com.trianglz.ui.uiModels.AppMoviesModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


data class MovieListState(
    val movies: Flow<PagingData<AppMoviesModel>> = flowOf(PagingData.empty()),
    val isLoading: Boolean = false,
    val isLoadingMore:Boolean = false,
    val query: String = "",
):ViewState

sealed class MovieListEvents:ViewEvent {
    data object LoadMovies : MovieListEvents()
    data class SearchMovies(val query: String) : MovieListEvents()
}

sealed class MovieListEffects:ViewSideEffect {
    sealed class Errors: MovieListEffects() {
        data class GenericError(val message: String) : Errors()
    }
}