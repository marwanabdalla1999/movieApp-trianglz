package com.trianglz.ui.commonUi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.trianglz.ui.R

@Composable
fun AsyncImageView(
    imageUrl: String,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current).data(imageUrl).crossfade(true)
            .scale(Scale.FILL).build()
    )

    val painterState = painter.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier, contentAlignment = Alignment.Center
    ) {
        when (painterState.value) {

            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(50.dp), strokeWidth = 2.dp
                )
            }

            is AsyncImagePainter.State.Error -> {
                Image(
                    painter = painterResource(R.drawable.error),
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = modifier

                )
            }

            else -> {
                Image(
                    painter = painter,
                    contentDescription = contentDescription,
                    contentScale = contentScale,
                    modifier = modifier
                )
            }
        }
    }
}

@Preview
@Composable
fun AsyncImageViewPreview(){
    AsyncImageView(imageUrl = "")
}
