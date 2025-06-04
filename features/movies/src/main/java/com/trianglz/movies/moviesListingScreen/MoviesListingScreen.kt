package com.trianglz.movies.moviesListingScreen


import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.trianglz.movies.moviesListingScreen.composables.MovieListContent
import kotlinx.coroutines.flow.Flow

@Composable
fun MovieListScreen(
    state: MovieListState,
    setEvents: (MovieListEvents) -> Unit,
    effects: Flow<MovieListEffects>,
    modifier: Modifier = Modifier,
    navigateToMovieDetails: (Int) -> Unit = {},
) {
    LaunchedEffect(Unit) {
        effects.collect {
            when (it) {
                is MovieListEffects.Navigation.MovieDetails -> navigateToMovieDetails(it.movieId)

            }
        }
    }

    when (state) {
        MovieListState.Ideal -> setEvents(MovieListEvents.LoadMovies)

        is MovieListState.Success -> {
            val moviesPager = state.movies.collectAsLazyPagingItems()

            val searchPager = state.searchedMovieList.collectAsLazyPagingItems()


            val isSearchMode = state.query.isNotEmpty()
            val pagerToShow = if (isSearchMode) searchPager else moviesPager

            val isLoading =
                pagerToShow.loadState.refresh is LoadState.Loading && pagerToShow.itemCount == 0


            val errorMessage by remember(pagerToShow.loadState.refresh) {
                mutableStateOf(
                    (pagerToShow.loadState.refresh as? LoadState.Error)?.error?.message
                )
            }

            val isSearchEmpty by remember {
                derivedStateOf {
                    isSearchMode && pagerToShow.loadState.refresh is LoadState.NotLoading && pagerToShow.itemCount == 0
                }
            }


            MovieListContent(
                modifier = modifier,
                query = state.query,
                setEvents = setEvents,
                movies = pagerToShow,
                isLoading = isLoading,
                errorMessage = errorMessage,
                isSearchEmpty = isSearchEmpty
            )


        }

    }


}














