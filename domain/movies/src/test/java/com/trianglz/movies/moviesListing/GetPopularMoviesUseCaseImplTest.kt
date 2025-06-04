package com.trianglz.movies.moviesListing

import androidx.paging.PagingData
import androidx.paging.testing.asSnapshot
import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.usecase.popularMoviesUseCase.GetPopularMoviesUseCaseImpl
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class GetPopularMoviesUseCaseImplTest {

    private lateinit var repository: IMoviesRepository
    private lateinit var useCase: GetPopularMoviesUseCaseImpl

    private val fakeMovies = listOf(
        MovieDomainModel(
            1,
            "Movie A",
            "Desc A",
            "/a.jpg",
            "2025-01-01",
            8.1,
            popularity = 1.0,
            adult = false,
            originalLanguage = "",
            voteCount = 12
        ), MovieDomainModel(
            2,
            "Movie B",
            "Desc B",
            "/b.jpg",
            "2025-02-01",
            7.3,
            popularity = 1.0,
            adult = false,
            originalLanguage = "",
            voteCount = 12
        )
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = GetPopularMoviesUseCaseImpl(repository)
    }

    @Test
    fun `invoke returns expected PagingData`() = runTest {
        val pagingData = PagingData.from(fakeMovies)
        every { repository.getMovies() } returns flowOf(pagingData)

        val result = useCase.invoke().asSnapshot()

        assertEquals(fakeMovies, result)
    }
}
