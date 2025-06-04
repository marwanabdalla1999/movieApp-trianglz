package com.trianglz.movies.repositories

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import androidx.room.withTransaction
import app.cash.turbine.test
import com.trianglz.cache.dataBase.AppDatabase
import com.trianglz.cache.movies.MovieRemoteKeysDao
import com.trianglz.cache.movies.MoviesDao
import com.trianglz.cache.movies.entities.MovieEntity
import com.trianglz.movies.models.mappers.toDomain
import com.trianglz.movies.models.mappers.toEntity
import com.trianglz.movies.models.responses.MovieDto
import com.trianglz.movies.models.responses.MoviesResponse
import com.trianglz.movies.remoteDataSource.IMoviesRemoteDataSource
import com.trianglz.movies.repository.MovieRepositoryImpl
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoviesRepositoryImplTest {

    private lateinit var moviesApiService: IMoviesRemoteDataSource
    private lateinit var moviesCacheService: MoviesDao
    private lateinit var repository: MovieRepositoryImpl
    private lateinit var database: AppDatabase
    private lateinit var movieRemoteKeysDao: MovieRemoteKeysDao
    private lateinit var moviesDao: MoviesDao

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        moviesApiService = mockk()
        moviesCacheService = mockk()
        database = mockk()
        moviesDao = mockk()
        movieRemoteKeysDao = mockk()

        repository = MovieRepositoryImpl(
            remoteMoviesDataSource = moviesApiService,
            moviesDoa = moviesDao,
            remoteKeysDao = movieRemoteKeysDao,
            database = database
        )

    }

    @Test
    fun `searchForMovies returns expected paged data`() = runTest {
        val query = "test"

        coEvery {
            moviesApiService.searchForMovies(query = query, page = any())
        } returns MoviesResponse(
            page = 1, results = fakeMovies, totalPages = 1, totalResults = fakeMovies.size
        )

        val actualPagingData = repository.searchForMovies(query).asSnapshot()

        assertEquals(expectedDomainModels, actualPagingData)
    }


    @Test
    fun `test movie details returns movie details depend on movieId`() = runTest {
        val movieId = 123
        val mockMovieResponse = createMockMovieResponse(movieId, "Test Movie")
        coEvery { moviesApiService.getMoviesDetails(movieId) } returns mockMovieResponse

        val result = repository.getMovieDetails(movieId)

        result.test {
            val movie = awaitItem()
            assertEquals(movieId, movie.id)
            awaitComplete()
        }
    }

    @Test
    fun `test movie details don't emit any flows when id is invalid`() = runTest {
        val movieId = 999
        coEvery { moviesApiService.getMoviesDetails(movieId) } returns null

        val result = repository.getMovieDetails(movieId)

        result.test {
            expectNoEvents()
            awaitComplete()
        }
    }

    @Test
    fun `test MovieDetails handle  movieIds correctly`() = runTest {
        val movieId1 = 111
        val movieId2 = 222
        val movieResponse1 = createMockMovieResponse(movieId1, "Movie 1")
        val movieResponse2 = createMockMovieResponse(movieId2, "Movie 2")

        coEvery { moviesApiService.getMoviesDetails(movieId1) } returns movieResponse1
        coEvery { moviesApiService.getMoviesDetails(movieId2) } returns movieResponse2

        repository.getMovieDetails(movieId1).test {
            val result1 = awaitItem()
            assertEquals(movieId1, result1.id)
            assertEquals("Movie 1", result1.title)
            awaitComplete()
        }

        repository.getMovieDetails(movieId2).test {
            val result2 = awaitItem()
            assertEquals(movieId2, result2.id)
            assertEquals("Movie 2", result2.title)
            awaitComplete()
        }

        coVerify(exactly = 1) { moviesApiService.getMoviesDetails(movieId1) }
        coVerify(exactly = 1) { moviesApiService.getMoviesDetails(movieId2) }
    }

    @Test
    fun `MovieDetails should map MovieResponse to domain model correctly`() = runTest {
        val movieId = 456
        val movieResponse = createMockMovieResponse(movieId, "Detailed Movie")
        coEvery { moviesApiService.getMoviesDetails(movieId) } returns movieResponse

        val result = repository.getMovieDetails(movieId)

        result.test {
            val movie = awaitItem()
            assertEquals(movieResponse.id, movie.id)
            assertEquals(movieResponse.title, movie.title)
            assertEquals(movieResponse.overview, movie.overview)
            assertEquals(movieResponse.posterPath, movie.posterPath)
            assertEquals(movieResponse.releaseDate, movie.releaseDate)
            assertEquals(movieResponse.voteCount, movie.voteCount)
            assertEquals(movieResponse.adult, movie.adult)
            assertEquals(movieResponse.originalLanguage, movie.originalLanguage)
            awaitComplete()
        }
    }

    @Test
    fun `searchMovies should handle empty query`() = runTest {
        val emptyQuery = ""

        val result = repository.searchForMovies(emptyQuery)

        result.test {
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `searchMovies should handle special characters in query`() = runTest {
        val specialQuery = "T-test test query"

        val result = repository.searchForMovies(specialQuery)

        result.test {
            cancelAndIgnoreRemainingEvents()
        }
    }


    private fun createMockMovieResponse(id: Int, title: String) = MovieDto(
        id = id,
        title = title,
        overview = "Test overview",
        posterPath = "/test_poster.jpg",
        backdropPath = "/test_backdrop.jpg",
        releaseDate = "2023-01-01",
        voteAverage = 7.5,
        voteCount = 1000,
        popularity = 100.0,
        adult = false,
        originalLanguage = "en",
        originalTitle = title
    )


    val expectedDomainModels = fakeMovies.map { it.toDomain() }
}
