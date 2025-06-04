package com.trianglz.movies.moviesDetailsScreen

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.trianglz.ui.navigations.MovieDetails


fun NavGraphBuilder.movieDetailsRoute() {
    composable<MovieDetails> { navBackStackEntry ->
        val movieId = navBackStackEntry.toRoute<MovieDetails>().movieId
        val viewModel: MovieDetailsViewModel = hiltViewModel()
        val state by viewModel.viewState.collectAsStateWithLifecycle()
        val effects = viewModel.effect
        MovieDetailsScreen(
            movieId = movieId,
            state = state,
            setEvents = viewModel::setEvent,
            effects = effects,
            modifier = Modifier
        )
    }
}