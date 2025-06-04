package com.trianglz.movies.moviesDetailsScreen.composables
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.trianglz.movies.R


@Composable
fun MovieMetaInfo(
    releaseDate: String,
    voteAverage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.release, releaseDate),
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray
        )
        Text(
            text = "⭐ $voteAverage",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Yellow
        )
    }
}