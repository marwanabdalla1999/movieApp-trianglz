package com.trianglz.movies.moviesDetailsScreen

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.trianglz.movies.mappers.toAppUiModel
import com.trianglz.movies.usecase.movieDetailsUseCase.IMovieDetailsUseCase
import com.trianglz.movies.usecase.popularMoviesUseCase.IGetPopularMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.ISearchForMoviesUseCase
import com.trianglz.ui.base.BaseViewModel
import com.trianglz.ui.uiModels.AppMoviesModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: IMovieDetailsUseCase,
) : BaseViewModel<MoviesDetailsState, MoviesDetailsEvents, MoviesDetailsEffects>() {

    private fun loadMovie(movieId: Int) {
        viewModelScope.launch {
            getMovieDetailsUseCase(movieId = movieId).map { it.toAppUiModel() }.launchAndCollectResult(
                onSuccess = {
                    setState { MoviesDetailsState.SuccessMovieLoaded(it) }
                }, onError = {
                    setEffect { MoviesDetailsEffects.Error(it.message?:"Unknown Error") }
                }
            )

        }
    }


    override fun setInitialState(): MoviesDetailsState = MoviesDetailsState.Ideal

    override fun handleEvents(event: MoviesDetailsEvents) {
        when (event) {
            is MoviesDetailsEvents.LoadMovie -> loadMovie(event.id)
        }
    }
}