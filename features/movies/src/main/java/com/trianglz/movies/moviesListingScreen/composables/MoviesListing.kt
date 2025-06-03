package com.trianglz.movies.moviesListingScreen.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.trianglz.ui.commonUi.MovieItem
import com.trianglz.ui.uiModels.AppMoviesModel

@Composable
fun MoviesListing(movies: LazyPagingItems<AppMoviesModel>, state: LazyListState) {
    LazyColumn (
        modifier = Modifier.fillMaxSize(), state = state
    ) {
        items(
            count = movies.itemCount
        ) { index ->
            movies[index]?.let { movie ->
                MovieItem(movie)
            }
        }

        when (movies.loadState.append) {
            is LoadState.Loading -> item { LoadingMoreItem() }
            is LoadState.Error -> item {
                LoadMoreErrorItem(onRetry = { movies.retry() })
            }

            else -> Unit
        }
    }
}