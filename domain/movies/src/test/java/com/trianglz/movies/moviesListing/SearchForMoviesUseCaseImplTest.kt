package com.trianglz.movies.moviesListing

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.usecase.searchForMovieUseCase.SearchForMoviesUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchForMoviesUseCaseImplTest {

    private lateinit var repository: IMoviesRepository
    private lateinit var useCase: SearchForMoviesUseCaseImpl

    private val fakeMovies = listOf(
        MovieDomainModel(
            1,
            "Interstellar",
            "Space movie",
            "/poster1.jpg",
            "2014-11-07",
            8.6,
            popularity = 1.0,
            adult = false,
            originalLanguage = "",
            voteCount = 12
        ), MovieDomainModel(
            2,
            "Inception",
            "Dream within a dream",
            "/poster2.jpg",
            "2010-07-16",
            8.8,
            popularity = 1.0,
            adult = false,
            originalLanguage = "",
            voteCount = 12
        )
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = SearchForMoviesUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns expected PagingData`() = runTest {
        val query = "Christopher Nolan"
        val pagingData = PagingData.from(fakeMovies)
        coEvery { repository.searchForMovies(query) } returns flowOf(pagingData)

        val result = useCase(query).asSnapshot()

        assertEquals(fakeMovies, result)
    }
}
