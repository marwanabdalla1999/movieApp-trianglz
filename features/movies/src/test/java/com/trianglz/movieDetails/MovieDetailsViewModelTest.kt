package com.trianglz.movieDetails

import app.cash.turbine.test
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.moviesDetailsScreen.MovieDetailsViewModel
import com.trianglz.movies.moviesDetailsScreen.MoviesDetailsEffects
import com.trianglz.movies.moviesDetailsScreen.MoviesDetailsEvents
import com.trianglz.movies.moviesDetailsScreen.MoviesDetailsState
import com.trianglz.movies.usecase.movieDetailsUseCase.IMovieDetailsUseCase
import com.trianglz.ui.models.AppMovieModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieDetailsViewModelTest {

    private lateinit var getMovieDetailsUseCase: IMovieDetailsUseCase
    private lateinit var viewModel: MovieDetailsViewModel

    private val fakeMovieDomain = MovieDomainModel(
        id = 1,
        title = "Test Movie",
        overview = "A test overview",
        posterPath = "/test.jpg",
        releaseDate = "2025-01-01",
        voteAverage = 7.8,
        voteCount = 12,
        popularity = 1.0,
        adult = false,
        originalLanguage = ""
    )

    private val fakeAppMovie = AppMovieModel(
        id = 1,
        title = "Test Movie",
        overview = "A test overview",
        poster = "/test.jpg",
        releaseDate = "2025-01-01",
        voteAverage = 7.8,
        voteCount = 12,
    )

    @Before
    fun setUp() {
        getMovieDetailsUseCase = mockk()
        viewModel = MovieDetailsViewModel(getMovieDetailsUseCase)
    }

    @Test
    fun `loadMovie emits SuccessMovieLoaded state when use case returns data`() = runTest {
        coEvery { getMovieDetailsUseCase(1) } returns flow {
            emit(fakeMovieDomain)
        }

        viewModel.viewState.test {
            viewModel.setEvent(MoviesDetailsEvents.LoadMovie(1))

            assertEquals(MoviesDetailsState.Ideal, awaitItem())
            assertEquals(MoviesDetailsState.SuccessMovieLoaded(fakeAppMovie), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadMovie emits Error effect when use case throws exception`() = runTest {
        val errorMessage = "Network Error"
        coEvery { getMovieDetailsUseCase(1) } returns flow {
            throw RuntimeException(errorMessage)
        }

        viewModel.effect.test {
            viewModel.setEvent(MoviesDetailsEvents.LoadMovie(1))

            assertEquals(MoviesDetailsEffects.Error(errorMessage), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
