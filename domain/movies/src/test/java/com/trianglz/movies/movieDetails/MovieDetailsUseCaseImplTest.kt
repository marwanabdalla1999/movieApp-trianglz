import com.trianglz.movies.models.MovieDomainModel
import com.trianglz.movies.repositories.IMoviesRepository
import com.trianglz.movies.usecase.movieDetailsUseCase.MovieDetailsUseCaseImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class MovieDetailsUseCaseImplTest {

    private lateinit var repository: IMoviesRepository
    private lateinit var useCase: MovieDetailsUseCaseImpl

    private val fakeMovie = MovieDomainModel(
        id = 1,
        title = "Test Movie",
        overview = "A test overview",
        posterPath = "/test.jpg",
        releaseDate = "2025-01-01",
        voteAverage = 7.8,
        voteCount = 1000,
        popularity = 100.00,
        adult = false,
        originalLanguage = ""
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = MovieDetailsUseCaseImpl(repository)
    }

    @Test
    fun `invoke should return movie details from repository`() = runTest {
        val movieId = 1
        coEvery { repository.getMovieDetails(movieId) } returns flowOf(fakeMovie)

        val result = useCase(movieId).first()

        assertEquals(fakeMovie, result)
    }
}
