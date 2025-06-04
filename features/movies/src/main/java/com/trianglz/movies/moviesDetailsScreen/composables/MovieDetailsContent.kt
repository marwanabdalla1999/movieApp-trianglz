package com.trianglz.movies.moviesDetailsScreen.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trianglz.ui.models.AppMovieModel


@Composable
fun MovieDetailsContent(
    modifier: Modifier = Modifier, movie: AppMovieModel, onBackClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MovieDetailsTopBar(
            title = movie.title, modifier = Modifier.fillMaxWidth(), onBackClick = onBackClick
        )
        MoviePoster(
            posterUrl = movie.poster,
            title = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        MovieMetaInfo(
            releaseDate = movie.getFormattedReleaseDate(),
            voteAverage = movie.getFormattedVoteAverage(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MovieOverview(
            overview = movie.overview, modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}

@Preview
@Composable
fun MovieDetailsContentPreview() {
    MovieDetailsContent(
        movie = AppMovieModel(
            id = 1, title = "", overview = "", poster = "", releaseDate = "", voteAverage = 0.0,
        ), onBackClick = {})
}