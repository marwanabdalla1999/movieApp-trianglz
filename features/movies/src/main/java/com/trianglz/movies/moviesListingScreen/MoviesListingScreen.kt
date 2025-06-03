package com.trianglz.movies.moviesListingScreen


import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.compose.collectAsLazyPagingItems
import com.trianglz.movies.moviesListingScreen.composables.MovieListContent
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieListScreen(
    modifier: Modifier = Modifier,
    state: MovieListState,
    setEvents: (MovieListEvents) -> Unit = {},
    effects: Flow<MovieListEffects>
) {
    val listState = rememberLazyListState()

    when (state) {
        is MovieListState.Ideal -> {
            setEvents(MovieListEvents.LoadMovies)
        }

        is MovieListState.MoviesData -> {
            MovieListContent(
                modifier = modifier,
                state = state,
                setEvents = setEvents,
                listState = listState,
                movies = state.movies.collectAsLazyPagingItems(),
                searchedMovies = state.searchedMovieList.collectAsLazyPagingItems()
            )

        }
    }
}












