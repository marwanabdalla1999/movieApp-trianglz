package com.trianglz.movies.moviesDetailsScreen

import androidx.lifecycle.viewModelScope
import com.trianglz.movies.mappers.toAppUiModel
import com.trianglz.movies.usecase.movieDetailsUseCase.IMovieDetailsUseCase
import com.trianglz.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: IMovieDetailsUseCase,
) : BaseViewModel<MoviesDetailsState, MoviesDetailsEvents, MoviesDetailsEffects>() {

    private fun loadMovie(movieId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
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