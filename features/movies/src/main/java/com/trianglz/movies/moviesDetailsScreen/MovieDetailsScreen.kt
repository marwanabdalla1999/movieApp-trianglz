package com.trianglz.movies.moviesDetailsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import com.trianglz.movies.moviesDetailsScreen.composables.MovieDetailsContent
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import kotlinx.coroutines.flow.Flow


@Composable
fun MovieDetailsScreen(
    movieId: Int,
    state: MoviesDetailsState,
    setEvents: (MoviesDetailsEvents) -> Unit,
    effects: Flow<MoviesDetailsEffects>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val snackBarHostState = LocalAppSnackBarHostState.current

    if (state is MoviesDetailsState.Ideal) {
        LaunchedEffect(Unit) {
            setEvents(MoviesDetailsEvents.LoadMovie(movieId))
        }
    }

    LaunchedEffect(effects) {
        effects.collect { effect ->
            when (effect) {
                is MoviesDetailsEffects.Error -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        when (state) {
            is MoviesDetailsState.Ideal,
            is MoviesDetailsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            is MoviesDetailsState.SuccessMovieLoaded -> {
                MovieDetailsContent(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    movie = state.movie,
                    onBackClick = onBackClick
                )
            }
        }
    }
}
