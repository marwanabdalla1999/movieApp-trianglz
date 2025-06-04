package com.trianglz.movies.moviesListingScreen

import androidx.paging.PagingData
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.usecase.popularMoviesUseCase.IGetPopularMoviesUseCase
import com.trianglz.movies.usecase.searchForMovieUseCase.ISearchForMoviesUseCase
import com.trianglz.ui.uiModels.AppMoviesModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MoviesListViewModelTest {

    private lateinit var getMoviesUseCase: IGetPopularMoviesUseCase
    private lateinit var searchForMoviesUseCase: ISearchForMoviesUseCase
    private lateinit var viewModel: MoviesListViewModel

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        getMoviesUseCase = mockk()
        searchForMoviesUseCase = mockk()
        viewModel = MoviesListViewModel(getMoviesUseCase, searchForMoviesUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun <T : Any> simplePagingData(vararg items: T): PagingData<T> {
        return PagingData.from(listOf(*items))
    }

    @Test
    fun `when LoadMovies event is handled, state becomes Success with an empty search Flow`() =
        testScope.runTest {
            val sampleMovie = MovieDomainModel(
                id = 1,
                title = "Test Movie",
                posterPath = "poster_path.jpg",
                releaseDate = "2025-01-01",
                overview = "Overview text",
                voteAverage = 8.5,
                voteCount = 100,
                popularity = 100.00,
                adult = true,
                originalLanguage = ""
            )
            val popularPaging: PagingData<MovieDomainModel> = simplePagingData(sampleMovie)
            coEvery { getMoviesUseCase() } returns flowOf(popularPaging)

            viewModel.setEvent(MovieListEvents.LoadMovies)
            advanceUntilIdle()
            val state = viewModel.viewState.value
            assertTrue(
                "Expected state to be Success after LoadMovies", state is MovieListState.Success
            )

            state as MovieListState.Success
            assertEquals("", state.query)

            val emittedMovies = state.movies.firstOrNull()
            assertNotNull("movies Flow must emit a PagingData", emittedMovies)

            val emittedSearch = state.searchedMovieList.firstOrNull()
            assertNotNull("searchedMovieList Flow must emit a PagingData (empty)", emittedSearch)

        }

    @Test
    fun `when SearchMovies with blank query is handled, state returns to Ideal`() =
        testScope.runTest {
            // Act
            viewModel.setEvent(MovieListEvents.SearchMovies(query = ""))

            // Assert
            val state = viewModel.viewState.value
            assertTrue("Blank query should reset to Ideal", state is MovieListState.Ideal)
        }

    @Test
    fun `when SearchMovies with non-blank query after debounce updates state and calls use case`() =
        testScope.runTest {
            // Arrange
            val sampleSearchMovie = MovieDomainModel(
                id = 1,
                title = "Test Movie",
                posterPath = "poster_path.jpg",
                releaseDate = "2025-01-01",
                overview = "Overview text",
                voteAverage = 8.5,
                voteCount = 100,
                popularity = 100.00,
                adult = true,
                originalLanguage = ""
            )
            val searchPaging: PagingData<MovieDomainModel> = simplePagingData(sampleSearchMovie)
            coEvery { searchForMoviesUseCase("matrix") } returns flowOf(searchPaging)

            viewModel.setEvent(MovieListEvents.SearchMovies(query = "matrix"))

            advanceUntilIdle()
            val state = viewModel.viewState.value
            assertTrue("Expected state to be Success after search", state is MovieListState.Success)

            state as MovieListState.Success
            assertEquals("matrix", state.query)

            coVerify(exactly = 1) { searchForMoviesUseCase("matrix") }

            val emittedSearch = state.searchedMovieList.firstOrNull()
            assertNotNull(
                "searchedMovieList Flow must emit a PagingData after debounce", emittedSearch
            )
        }

    @Test
    fun `when SearchMovies is called twice quickly, previous job is cancelled and only last query runs`() =
        testScope.runTest {
            coEvery { searchForMoviesUseCase(any()) } returns flowOf(PagingData.empty())

            viewModel.setEvent(MovieListEvents.SearchMovies(query = "one"))
            viewModel.setEvent(MovieListEvents.SearchMovies(query = "two"))
            advanceUntilIdle()

            coVerify(exactly = 0) { searchForMoviesUseCase("one") }
            coVerify(exactly = 1) { searchForMoviesUseCase("two") }

            val state = viewModel.viewState.value
            assertTrue(state is MovieListState.Success)
            state as MovieListState.Success
            assertEquals("two", state.query)
        }

//    @Test
//    fun `updateQuery only changes query when in Success state`() = testScope.runTest {
//
//        coEvery { searchForMoviesUseCase("abc") } returns flowOf(PagingData.empty())
//        viewModel.setEvent(MovieListEvents.SearchMovies(query = "abc"))
//        advanceUntilIdle()
//
//        var state = viewModel.viewState.value as MovieListState.Success
//        assertEquals("abc", state.query)
//
//
//        viewModel.setEvent(MovieListEvents.SearchMovies(query = "updated"))
//        advanceUntilIdle()
//
//        state = viewModel.viewState.value as MovieListState.Success
//        assertEquals("updated", state.query)
//    }
}
