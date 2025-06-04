package com.trianglz.movies.moviesDetailsScreen.composables


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.trianglz.ui.commonUi.AsyncImageView
import com.trianglz.ui.utils.getFullPosterUrl


@Composable
fun MoviePoster(posterUrl: String, title: String,modifier: Modifier = Modifier) {
    if (posterUrl.isNotBlank()) {
        AsyncImageView(
            imageUrl = posterUrl.getFullPosterUrl(),
            contentDescription = "Poster for $title",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}
