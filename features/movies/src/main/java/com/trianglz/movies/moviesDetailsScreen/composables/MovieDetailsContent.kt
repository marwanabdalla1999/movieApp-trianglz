package com.trianglz.movies.moviesDetailsScreen.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trianglz.ui.commonUi.AsyncImageView
import com.trianglz.ui.models.AppMovieModel
import com.trianglz.ui.utils.getFullPosterUrl


@Composable
fun MovieDetailsContent(
    modifier: Modifier = Modifier,
    movie: AppMovieModel
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (movie.poster.isNotBlank()) {
            AsyncImageView(
                imageUrl = movie.poster.getFullPosterUrl(),
                contentDescription = "Poster for ${movie.title}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(2 / 3f)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Release: ${movie.releaseDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.LightGray
            )
            Text(
                text = "⭐ ${movie.voteAverage}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Yellow
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = movie.overview.ifEmpty { "No overview available." },
            style = MaterialTheme.typography.bodySmall,
            color = Color.White,
            lineHeight = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )
    }
}
