package com.trianglz.movies



class MovieListViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
) : BaseViewModel<MovieListIntent, MovieListState>(MovieListState()) {

    init {
        handleIntent(MovieListIntent.LoadMovies)
    }

    override fun handleIntent(intent: MovieListIntent) {
        when (intent) {
            is MovieListIntent.LoadMovies -> loadMovies()
            is MovieListIntent.SearchMovies -> searchMovies(intent.query)
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            getMoviesUseCase().collect { movies ->
                setState { copy(movies = movies, isLoading = false) }
            }
        }
    }

    private fun searchMovies(query: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true) }
            searchMoviesUseCase(query).collect { movies ->
                setState { copy(movies = movies, isLoading = false) }
            }
        }
    }
} 