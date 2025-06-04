package com.trianglz.ui.models

import java.text.SimpleDateFormat
import java.util.Locale

data class AppMovieModel(
    val id: Int,
    val title: String,
    val poster: String,
    private val releaseDate: String,
    val overview: String,
    private val voteAverage: Double
) {
    fun getFormattedReleaseDate(): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            val date = inputFormat.parse(releaseDate)
            date?.let { outputFormat.format(it) } ?: releaseDate
        } catch (e: Exception) {
            releaseDate
        }
    }

    fun getFormattedVoteAverage(): String {
        return String.format(Locale.getDefault(), "%.1f/10", voteAverage)
    }
}
