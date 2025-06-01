package com.trianglz.movies


sealed class MovieListIntent {
    object LoadMovies : MovieListIntent()
    data class SearchMovies(val query: String) : MovieListIntent()
}

data class MovieListState(
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

sealed class MovieListEvent {
    data class ShowError(val message: String) : MovieListEvent()
} 