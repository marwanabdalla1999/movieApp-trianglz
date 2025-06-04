Movie App

A modern movie browsing application built with Jetpack Compose, MVI architecture, and Paging 3. The app features a paginated list of popular movies, a debounced search function, and a detailed screen for each movie. Data is sourced from a remote API and cached locally in a Room database. Navigation between screens is handled via Compose Navigation.

Table of Contents

Features
Architecture
MVI Overview
Layer Breakdown
Data Model
Screens
Movie Listing Screen
Search Screen
Movie Details Screen
Paging 3 & Caching
MoviesMediator
SearchMoviesPagingSource
How to Build & Run
Prerequisites
Getting Started
Dependencies
Project Structure
Further Improvements
Features

Paginated “Popular Movies” List
Infinite scrolling of popular movies (loaded page-by-page via Paging 3).
Shows cached data immediately; updates with fresh data when the device is online.
Debounced Movie Search
User can type in a search query into the top‐level search bar.
Results are fetched and displayed in a paginated list (also via Paging 3).
Search requests are debounced (e.g., 300 ms) to avoid spamming the remote API.
Movie Details Screen
Tapping on any movie in either list opens a details screen.
Details include poster image, title, formatted release date, average vote, and overview.
Offline Caching with Room
Local caching of movie data and paging‐keys (MovieEntity + MovieRemoteKeys).
On app launch or refresh, the UI shows cached pages first, then synchronizes with the remote source.
MVI-Based Architecture
All screens follow a unidirectional data flow pattern (Intent → ViewModel → State → UI).
Clear separation between UI layer (Compose), business logic, and data layers.
Architecture

MVI Overview
Intent (UserAction)
Represent user interactions (e.g., “Refresh”, “Search(query)”, “LoadNextPage”).
ViewModel
Receives intents, triggers domain/use-case operations, and updates state.
Exposes a StateFlow<UiState> that the UI collects.
State (UiState)
Immutable data class that holds everything the UI needs:
current list of AppMovieModel items
loading/error flags
search query string
pagination status
UI (Composable)
Collects the StateFlow and displays either a LazyColumn of movies, a loading spinner, or an error message.
Emits new intents back to the ViewModel when the user scrolls or types.
Layer Breakdown
UI Layer (Jetpack Compose)  
│  
├─ ViewModels (MVI)  
│  
├─ Use Cases / Interactors  
│  
├─ Repository  
│    ├─ Remote Data Source (Retrofit)  
│    └─ Local Data Source (Room / Paging 3)  
│  
└─ Models / Mappers  
     ├─ MovieDto (network) → MovieEntity (DB)  
     └─ MovieEntity → AppMovieModel (UI)
Data Model

/**
 * Data class used by the UI to render each movie.
 *
 * @property id           Unique movie identifier
 * @property title        Title of the movie
 * @property poster       Poster image path (to be converted via `getFullPosterUrl()`)
 * @property releaseDate  Release date string in “yyyy-MM-dd” format
 * @property overview     Brief synopsis or overview
 * @property voteAverage  Average rating (0.0–10.0)
 */
data class AppMovieModel(
    val id: Int,
    val title: String,
    val poster: String,
    val releaseDate: String,
    val overview: String,
    val voteAverage: Double
) {
    /**
     * Formats “yyyy-MM-dd” → “MMM dd, yyyy” (e.g., “2022-01-15” → “Jan 15, 2022”).
     * Falls back to the raw string if parsing fails.
     */
    fun getFormattedReleaseDate(): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            inputFormat.parse(releaseDate)?.let { outputFormat.format(it) } ?: releaseDate
        } catch (e: Exception) {
            releaseDate
        }
    }

    /**
     * Formats vote average to one decimal place, appending “/10” (e.g., “8.3 → “8.3/10”).
     */
    fun getFormattedVoteAverage(): String {
        return String.format(Locale.getDefault(), "%.1f/10", voteAverage)
    }
}
Screens

Movie Listing Screen
Composable: MovieListScreen
Description: Displays a paginated list of popular movies fetched from the remote API.
Key Features:
Uses a LazyColumn with a PagingDataAdapter (Compose’s collectAsLazyPagingItems()).
Shows a full‐width poster thumbnail (aspect ratio 2:3), title, release date, and average vote.
Pull-to-refresh to trigger a LoadType.REFRESH.
“Load more” when reaching the bottom (handled automatically by Paging 3).
Tapping on any movie triggers a NavigateTo(MovieDetailsScreen, movieId) intent.
@Composable
fun MovieListScreen(
    viewModel: MovieListViewModel,
    onMovieClick: (AppMovieModel) -> Unit
) {
    val uiState by viewModel.state.collectAsState()
    // uiState contains PagingData<AppMovieModel> for popular movies
    // Render a Scaffold with TopAppBar, LazyColumn, Loading /Error indicators, etc.
}
Search Screen
Composable: MovieSearchScreen (often shown at the top of the listing screen or as a separate tab)
Description: Allows users to type a query. Implements a 300 ms debounce so that we only fire a network request if the user stops typing for 300 ms.
Key Features:
TextField at the top, tied to viewModel.searchQuery (a MutableStateFlow<String>).
Each time searchQuery changes, use a .debounce(300) on the Flow in the ViewModel to call searchMoviesUseCase(query).
searchMoviesUseCase() returns a Flow<PagingData<AppMovieModel>> via SearchMoviesPagingSource.
The UI collects this PagingData and submits it to a LazyPagingItems.
“Clear” or “Cancel” button resets searchQuery to "" and shows the “Popular movies” listing again.
@Composable
fun MovieSearchScreen(
    viewModel: MovieSearchViewModel,
    onMovieClick: (AppMovieModel) -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults = viewModel.searchPagingData.collectAsLazyPagingItems()

    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.onIntent(MovieSearchIntent.QueryChanged(it)) },
            label = { Text("Search movies...") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(searchResults) { movie ->
                movie?.let {
                    MovieListItem(
                        movie = it,
                        onClick = { onMovieClick(it) }
                    )
                }
            }
            // You can show loading / error states here via searchResults.loadState
        }
    }
}
Movie Details Screen
Composable: MovieDetailsScreen
Description: Displays all details for a single movie.
Key Features:
TopAppBar with a back arrow (Modifier.statusBarsPadding() to remove extra padding).
Large poster image at the top (aspect ratio 2:3, rounded corners).
Movie title, release date (getFormattedReleaseDate()), vote average (getFormattedVoteAverage()), and overview.
Scrollable Column if the overview is long.
@Composable
fun MovieDetailsScreen(
    viewModel: MovieDetailsViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.state.collectAsState()

    // TopAppBar + MovieDetailsContent
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = { Text(uiState.movie.title, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            )
        }
    ) { paddingValues ->
        uiState.movie?.let { movie ->
            MovieDetailsContent(
                modifier = Modifier.padding(paddingValues),
                movie = movie
            )
        }
    }
}
Paging 3 & Caching

MoviesMediator
The MoviesMediator class is responsible for bridging the remote API and Room cache so that:

On LoadType.REFRESH, it clears the local tables (movies + movie_remote_keys) and fetches page 1.
On LoadType.APPEND, it looks up the last MovieRemoteKeys to find nextPage.
It inserts new MovieEntity rows and updates MovieRemoteKeys (prev/next) in a single transaction using withTransaction { ... }.
Key Points:

REFRESH (page = 1)
moviesDao.clearAllMovies()
remoteKeysDao.clearAllRemoteKeys()
Fetch page 1 → insert into movies and movie_remote_keys.
APPEND
remoteKeysDao.getLastRemoteKey() to get nextPage.
If nextPage is null, we are at the end of pagination.
Otherwise, fetch that page and insert both entities and remote keys.
@OptIn(ExperimentalPagingApi::class)
class MoviesMediator @Inject constructor(
    private val remoteDataSource: IMoviesRemoteDataSource,
    private val moviesDao: MoviesDao,
    private val remoteKeysDao: MovieRemoteKeysDao,
    private val withTransaction: suspend (suspend () -> Unit) -> Unit
) : RemoteMediator<Int, MovieEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult { ... }
    private suspend fun getPageToLoad(loadType: LoadType): Int? { ... }
    private suspend fun persistToDb(
        loadType: LoadType,
        page: Int,
        movies: List<MovieDto>,
        isEnd: Boolean
    ) = withTransaction { ... }
}
SearchMoviesPagingSource
SearchMoviesPagingSource is a simple PagingSource that:

Takes a search query: String and a remoteMoviesDataSource.
On load(params), it calls remoteMoviesDataSource.searchForMovies(query, page).
Maps each MovieDto → MovieDomainModel (and ultimately to AppMovieModel in the UI).
prevKey is (page == 1) ? null : page - 1;
nextKey is (response.results.isEmpty()) ? null : page + 1.
class SearchMoviesPagingSource(
    private val remoteMoviesDataSource: IMoviesRemoteDataSource,
    private val query: String
) : PagingSource<Int, MovieDomainModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDomainModel> {
        return try {
            val page = params.key ?: 1
            val response = remoteMoviesDataSource.searchForMovies(query = query, page = page)
            LoadResult.Page(
                data = response?.results?.map { it.toDomain() } ?: emptyList(),
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response?.results?.isEmpty() == true) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieDomainModel>): Int? {
        return state.anchorPosition?.let { anchor ->
            state.closestPageToPosition(anchor)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchor)?.nextKey?.minus(1)
        }
    }
}
How to Build & Run

Prerequisites
Android Studio Arctic Fox (or later) with Kotlin 1.8+
Gradle 7.0+
An API key for the movie database (e.g., TMDB) configured in local.properties or via environment variable.
Getting Started
Clone the repository
git clone https://github.com/yourusername/movie-app.git
cd movie-app
Add your API key
In local.properties (at project root), add:
TMDB_API_KEY="your_api_key_here"
Sync & Build
Open the project in Android Studio.
Click “Sync Project with Gradle Files.”
Run on an emulator or physical device (minimum SDK 21).
Navigate
The app will launch to the Movie Listing Screen (popular movies).
Use the top search bar to switch to Search Mode.
Tap any movie to see the Movie Details Screen.
