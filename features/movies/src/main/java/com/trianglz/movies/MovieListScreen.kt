package com.trianglz.movies

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.LoadState
import com.trianglz.ui.commonUi.AsyncImageView
import com.trianglz.ui.uiModels.AppMoviesModel

@Composable
fun MovieListScreen(modifier: Modifier = Modifier,viewModel: MovieListViewModel = hiltViewModel()) {
    val state by viewModel.viewState.collectAsStateWithLifecycle()
    val movies = state.movies.collectAsLazyPagingItems()

    Box(modifier = modifier.fillMaxSize()) {
        when (movies.loadState.refresh) {
            is LoadState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is LoadState.Error -> {
                val error = (movies.loadState.refresh as LoadState.Error).error
                Text(
                    text = "Error loading movies: ${error.message ?: "Unknown error"}",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(movies.itemSnapshotList.items) { movie ->
                        MovieItem(movie)
                    }

                    when (movies.loadState.append) {
                        is LoadState.Loading -> item { LoadingMoreItem() }
                        is LoadState.Error -> item { LoadMoreErrorItem { movies.retry() } }
                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: AppMoviesModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            AsyncImageView(
                imageUrl = movie.poster, modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = movie.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "Release Year: ${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun LoadingMoreItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun LoadMoreErrorItem(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextButton(onClick = onRetry, modifier = Modifier.align(Alignment.Center)) {
            Text(text = "Retry Loading More")
        }
    }
}
