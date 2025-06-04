package com.trianglz.movies.remoteDataSource

import com.trianglz.movies.apiServices.MovieApiService
import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.models.responses.MoviesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class MoviesRemoteDataSourceImplTest {

    private lateinit var remoteDataSource: MoviesRemoteDataSourceImpl
    private lateinit var apiService: MovieApiService

    private val fakeResponse = MoviesResponse(
        page = 1,
        results = listOf(
            MovieDto(
                id = 1,
                title = "Test Movie",
                overview = "Test Overview",
                posterPath = "/test.jpg",
                releaseDate = "2025-01-01",
                voteAverage = 7.5,
                voteCount = 10,
                popularity = 1.0,
                adult = false,
                backdropPath = "",
                originalLanguage = "",
                originalTitle = ""
            )
        ),
        totalPages = 1,
        totalResults = 1
    )

    private val fakeMovieDto = MovieDto(
        id = 1,
        title = "Test Movie",
        overview = "Test Overview",
        posterPath = "/test.jpg",
        releaseDate = "2025-01-01",
        voteAverage = 7.5,
        voteCount = 10,
        popularity = 1.0,
        adult = false,
        backdropPath = "",
        originalLanguage = "",
        originalTitle = ""
    )

    @Before
    fun setup() {
        apiService = mockk()
        remoteDataSource = MoviesRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `getMovies should return MoviesResponse from apiService`() = runTest {
        coEvery { apiService.getMovies(page = 1, pageSize = 20) } returns Response.success(fakeResponse)

        val result = remoteDataSource.getMovies(page = 1, pageSize = 20)

        assertEquals(fakeResponse, result)
    }

    @Test
    fun `searchForMovies should return MoviesResponse from apiService`() = runTest {
        coEvery { apiService.searchMovies(query = "Batman", page = 1) } returns Response.success(fakeResponse)

        val result = remoteDataSource.searchForMovies(query = "Batman", page = 1)

        assertEquals(fakeResponse, result)
    }

    @Test
    fun `getMoviesDetails should return MovieDto from apiService`() = runTest {
        coEvery { apiService.getMovieDetails(movieId = 1) } returns Response.success(fakeMovieDto)

        val result = remoteDataSource.getMoviesDetails(movieId = 1)

        assertEquals(fakeMovieDto, result)
    }
}
