package com.trianglz.movies.moviesListingScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import com.core_ui.commonUi.SearchView
import com.trianglz.movies.moviesListingScreen.MovieListEvents
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import com.trianglz.ui.models.AppMovieModel

@Composable
fun MovieListContent(
    modifier: Modifier,
    query: String,
    setEvents: (MovieListEvents) -> Unit,
    movies: LazyPagingItems<AppMovieModel>,
    isLoading: Boolean,
    errorMessage: String?,
    isSearchEmpty: Boolean,
) {
    val snackBarHostState = LocalAppSnackBarHostState.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(top = 10.dp)
    ) {
        SearchView(
            query = query,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onQueryChanged = { query ->
                setEvents(MovieListEvents.SearchMovies(query))
            })

        Box(modifier = modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                isSearchEmpty -> {
                    EmptySearchState(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                else -> {
                    MoviesListing(
                        movies = movies, setEvents = setEvents
                    )
                }
            }
        }
    }
}



