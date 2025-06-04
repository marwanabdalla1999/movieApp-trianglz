package com.trianglz.movies.moviesListingScreen.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trianglz.movies.R

@Composable
fun LoadMoreErrorItem(onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        TextButton(onClick = onRetry, modifier = Modifier.align(Alignment.Center)) {
            Text(text = stringResource(R.string.retry_loading_more))
        }
    }
}

@Preview
@Composable
fun LoadMoreErrorItemPreview() {
    LoadMoreErrorItem(onRetry = {})
}