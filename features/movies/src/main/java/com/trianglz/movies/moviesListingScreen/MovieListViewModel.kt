package com.trianglz.movies.moviesListingScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.trianglz.movies.moviesListingScreen.mappers.toAppUiModel
import com.trianglz.movies.usecase.IGetMoviesUseCase
import com.trianglz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesUseCase: IGetMoviesUseCase,
) : BaseViewModel<MovieListState, MovieListEvents, MovieListEffects>() {

    private fun loadMovies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }

            val moviesFlow = getMoviesUseCase()
                .catch {
                    setState { copy(isLoading = false) }
                    setEffect { MovieListEffects.Errors.GenericError(it.message ?: "") }
                }
                .map { it.map { movie -> movie.toAppUiModel() } }
                .cachedIn(viewModelScope)

            setState {
                copy(
                    movies = moviesFlow,
                    isLoading = false
                )
            }
        }
    }



    override fun setInitialState(): MovieListState = MovieListState()

    override fun handleEvents(event: MovieListEvents) {
        when (event){
            MovieListEvents.LoadMovies -> loadMovies()
            is MovieListEvents.SearchMovies -> {

            }
        }

    }
} 