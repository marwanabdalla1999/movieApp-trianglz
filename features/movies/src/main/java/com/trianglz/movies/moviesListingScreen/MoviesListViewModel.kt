package com.trianglz.movies.moviesListingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trianglz.movies.mappers.toAppUiModel
import com.trianglz.movies.usecase.popularMoviesUseCase.IGetPopularMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.ISearchForMoviesUseCase
import com.trianglz.ui.base.BaseViewModel
import com.trianglz.ui.uiModels.AppMoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for loading a paginated list of popular movies and
 * performing movie searches. Exposes UI state and one‐time navigation effects.
 *
 * @param getMoviesUseCase    a use case that returns a Flow of paging data for popular movies.
 * @param searchForMoviesUseCase    a use case that returns a Flow of paging data for search results.
 */
@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val getMoviesUseCase: IGetPopularMoviesUseCase,
    private val searchForMoviesUseCase: ISearchForMoviesUseCase
) : BaseViewModel<MovieListState, MovieListEvents, MovieListEffects>() {

    override fun setInitialState(): MovieListState = MovieListState.Ideal

    private var searchJob: Job? = null

    /**
     * Routes incoming UI events to appropriate handlers.
     */
    override fun handleEvents(event: MovieListEvents) {
        when (event) {
            MovieListEvents.LoadMovies -> loadMovies()
            is MovieListEvents.SearchMovies -> {
                updateQuery(event.query)
                searchForMovies(event.query)
            }
            is MovieListEvents.MovieClicked -> {
                setEffect { MovieListEffects.Navigation.MovieDetails(event.movieId) }
            }
        }
    }

    /**
     * Loads the popular movies Flow and updates state to [MovieListState.Success]
     * with an empty search list.
     */
    private fun loadMovies() {
        val moviesFlow: Flow<PagingData<AppMoviesModel>> =
            getMoviesUseCase()
                .map { pagingData -> pagingData.map { it.toAppUiModel() } }
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .flowOn(Dispatchers.IO)

        val emptyPaging: Flow<PagingData<AppMoviesModel>> = flowOf(PagingData.empty())

        setState {
            MovieListState.Success(
                movies = moviesFlow,
                searchedMovieList = emptyPaging,
                query = ""
            )
        }
    }

    /**
     * Debounces input for 500ms and then calls the search use case. If [query] is blank,
     * clears any existing search results.
     *
     * @param query    the search string entered by the user.
     */
    private fun searchForMovies(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            clearSearchResults()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // debounce

            val newSearchFlow: Flow<PagingData<AppMoviesModel>> =
                searchForMoviesUseCase(query)
                    .map { pagingData -> pagingData.map { it.toAppUiModel() } }
                    .distinctUntilChanged()
                    .cachedIn(viewModelScope)
                    .flowOn(Dispatchers.IO)

            val currentState = getState()
            if (currentState is MovieListState.Success) {
                setState {
                    currentState.copy(
                        searchedMovieList = newSearchFlow,
                        query = query
                    )
                }
            } else {
                setState {
                    MovieListState.Success(
                        movies = flowOf(PagingData.empty()),
                        searchedMovieList = newSearchFlow,
                        query = query
                    )
                }
            }
        }
    }

    /**
     * Resets search‐related fields. If the current state is [MovieListState.Success],
     * drops the search list and restores an empty query. Otherwise, goes back to [MovieListState.Ideal].
     */
    private fun clearSearchResults() {
        val currentState = getState()
        if (currentState is MovieListState.Success) {
            setState {
                currentState.copy(
                    searchedMovieList = flowOf(PagingData.empty()),
                    query = ""
                )
            }
        } else {
            setState { MovieListState.Ideal }
        }
    }

    /**
     * Updates the current [MovieListState.Success.query] without restarting a search.
     */
    private fun updateQuery(query: String) {
        val currentState = getState()
        if (currentState is MovieListState.Success) {
            setState {
                currentState.copy(query = query)
            }
        }
    }
}
