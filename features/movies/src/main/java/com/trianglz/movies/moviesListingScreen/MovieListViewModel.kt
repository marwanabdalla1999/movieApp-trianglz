package com.trianglz.movies.moviesListingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trianglz.movies.moviesListingScreen.mappers.toAppUiModel
import com.trianglz.movies.usecase.popularMoviesUseCase.IGetPopularMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.ISearchForMoviesUseCase
import com.trianglz.ui.base.BaseViewModel
import com.trianglz.ui.uiModels.AppMoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: IGetPopularMoviesUseCase,
    private val searchForMoviesUseCase: ISearchForMoviesUseCase
) : BaseViewModel<MovieListState, MovieListEvents, MovieListEffects>() {

    private var job: Job? = null


    private fun loadMovies() {
        viewModelScope.launch {

            val moviesFlow = getMoviesUseCase().map { it.map { movie -> movie.toAppUiModel() } }
                .cachedIn(viewModelScope)

            setState {
                MovieListState.MoviesData(
                    movies = moviesFlow
                )

            }
        }
    }

    private var searchJob: Job? = null

    private fun searchForMovies(query: String) {
        searchJob?.cancel()

        if (query.isBlank()) {
            clearSearchResults()
            return
        }

        searchJob = viewModelScope.launch {
            delay(500)

            val flow = getSearchFlow(query)

            updateSearchResults(flow)
        }
    }

    private fun clearSearchResults() {
        setState {
            when (this) {
                is MovieListState.MoviesData -> copy(searchedMovieList = flowOf(PagingData.empty()))
                else -> this
            }
        }
    }

    private suspend fun getSearchFlow(query: String) = searchForMoviesUseCase.invoke(query)
        .map { pagingData -> pagingData.map { it.toAppUiModel() } }
        .catch { exception ->
            setEffect {
                MovieListEffects.Errors.GenericError(exception.message ?: "Unknown error")
            }
        }

    private fun updateSearchResults(flow: Flow<PagingData<AppMoviesModel>>) {
        setState {
            when (this) {
                is MovieListState.MoviesData -> copy(searchedMovieList = flow)
                else -> MovieListState.MoviesData(searchedMovieList = flow)
            }
        }
    }



    override fun setInitialState(): MovieListState = MovieListState.Ideal

    override fun handleEvents(event: MovieListEvents) {
        when (event) {
            MovieListEvents.LoadMovies -> loadMovies()
            is MovieListEvents.SearchMovies -> {
                val currentState = getState()
                if (currentState is MovieListState.MoviesData) {
                    setState {
                        currentState.copy(query = event.query)
                    }
                    searchForMovies(event.query)
                } else {
                    setState {
                        MovieListState.MoviesData(query = event.query)
                    }
                }
            }
        }
    }
}