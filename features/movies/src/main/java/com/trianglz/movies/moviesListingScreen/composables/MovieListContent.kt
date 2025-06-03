package com.trianglz.movies.moviesListingScreen.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.core_ui.commonUi.SearchView
import com.trianglz.movies.moviesListingScreen.MovieListEvents
import com.trianglz.movies.moviesListingScreen.MovieListState
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import com.trianglz.ui.uiModels.AppMoviesModel

@Composable
fun MovieListContent(
    modifier: Modifier,
    state: MovieListState.MoviesData,
    setEvents: (MovieListEvents) -> Unit,
    listState: LazyListState,
    movies: LazyPagingItems<AppMoviesModel>,
    searchedMovies: LazyPagingItems<AppMoviesModel>,
) {
    val snackBarHostState = LocalAppSnackBarHostState.current

    val isSearchMode = state.query.isNotEmpty()
    val currentPagingItems = if (isSearchMode) searchedMovies else movies

    val isLoading = currentPagingItems.loadState.refresh is LoadState.Loading

    val errorMessage = when (val refreshState = currentPagingItems.loadState.refresh) {
        is LoadState.Error -> refreshState.error.message
        else -> null
    }

    val isSearchEmpty = isSearchMode &&
            currentPagingItems.loadState.refresh is LoadState.NotLoading &&
            currentPagingItems.itemCount == 0

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackBarHostState.showSnackbar(it)
        }
    }

    Column (modifier = modifier.fillMaxSize().background(Color.Black).padding(top = 10.dp)) {
        SearchView(
            query = state.query,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
            onQueryChanged = { query ->
                setEvents(MovieListEvents.SearchMovies(query))
            }
        )

        Box(modifier = modifier.fillMaxSize()) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                isSearchEmpty -> {
                    EmptySearchState(modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    MoviesListing(
                        movies = currentPagingItems,
                        state = listState
                    )
                }
            }
        }
    }
}

@Composable
fun EmptySearchState(modifier: Modifier = Modifier) {
    Text(
        text = "No results found",
        modifier = modifier.padding(16.dp)
    )
}
