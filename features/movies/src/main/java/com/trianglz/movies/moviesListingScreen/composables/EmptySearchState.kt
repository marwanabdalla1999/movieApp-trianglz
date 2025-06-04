package com.trianglz.movies.moviesListingScreen.composables

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.trianglz.movies.R

@Composable
fun EmptySearchState(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.no_results_found),
        modifier = modifier
    )
}

@Preview
@Composable
fun EmptySearchStatePreview() {
    EmptySearchState()
}