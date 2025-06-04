package com.trianglz.movies.moviesDetailsScreen.composables



import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp



@Composable
fun MovieOverview(overview: String,modifier: Modifier = Modifier) {
    Text(
        text = overview.ifEmpty { "No overview available." },
        style = MaterialTheme.typography.bodySmall,
        color = Color.White,
        lineHeight = 20.sp,
        modifier = modifier
    )
}