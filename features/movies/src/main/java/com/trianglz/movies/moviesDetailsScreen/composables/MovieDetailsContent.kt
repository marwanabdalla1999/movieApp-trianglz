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
import androidx.compose.ui.unit.dp
import com.trianglz.ui.uiModels.AppMoviesModel


@Composable
fun MovieDetailsContent(
    modifier: Modifier = Modifier,
    movie: AppMoviesModel,
    onBackClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MovieDetailsTopBar(
            title = movie.title,
            modifier = Modifier.fillMaxWidth(),
            onBackClick = onBackClick
        )
        MoviePoster(
            posterUrl = movie.poster, title = movie.title, modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(2 / 3f)
                .clip(RoundedCornerShape(12.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        MovieMetaInfo(
            releaseDate = movie.releaseDate,
            voteAverage = movie.voteAverage.toString(),
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