package com.trianglz.movies.moviesListingScreen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.LoadState
import com.trianglz.ui.commonUi.AsyncImageView
import com.trianglz.ui.commonUi.LocalAppSnackBarHostState
import com.trianglz.ui.uiModels.AppMoviesModel
import com.trianglz.ui.utils.getFullPosterUrl
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieListScreen(
    modifier: Modifier = Modifier,
    state: MovieListState,
    setEvents: (MovieListEvents) -> Unit = {},
    effects: Flow<MovieListEffects>
) {
    val snackBarHostState = LocalAppSnackBarHostState.current


    LaunchedEffect(Unit) {
        setEvents(MovieListEvents.LoadMovies)
        effects.collect {
            when (it) {
                is MovieListEffects.Errors.GenericError -> snackBarHostState.showSnackbar(it.message)
            }
        }
    }


    Box(modifier = modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val movies = state.movies.collectAsLazyPagingItems()

            LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
                items(movies.itemCount) { index ->
                    val movie = movies[index]
                    if (movie != null) {
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
    }

}


@Composable
fun MovieItem(movie: AppMoviesModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImageView(
                imageUrl = movie.poster.getFullPosterUrl(),
                modifier = Modifier.size(160.dp),
                contentDescription = movie.title,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = movie.title,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = movie.releaseDate,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                textAlign = TextAlign.Center
            )

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
