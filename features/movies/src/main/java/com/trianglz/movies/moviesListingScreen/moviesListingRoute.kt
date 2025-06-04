package com.trianglz.movies.moviesListingScreen

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.trianglz.ui.navigations.MoviesListing

fun NavGraphBuilder.moviesListingRoute(
    modifier: Modifier = Modifier, navigateToMovieDetails: (Int) -> Unit
) {

    composable<MoviesListing> {
        val viewModel: MoviesListViewModel = hiltViewModel()
        val state by viewModel.viewState.collectAsStateWithLifecycle()
        val effects = viewModel.effect
        MovieListScreen(
            state = state,
            setEvents = viewModel::setEvent,
            effects = effects,
            modifier = modifier,
            navigateToMovieDetails = navigateToMovieDetails
        )
    }


}