package com.trianglz.ui.utils

import android.view.MotionEvent
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.pointer.pointerInteropFilter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun Modifier.singleClick(
    debounceTime: Long = 500L,
    onClick: () -> Unit
): Modifier = composed {
    var enabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    this.pointerInteropFilter {
        if (it.action == MotionEvent.ACTION_UP && enabled) {
            enabled = false
            onClick()
            scope.launch {
                delay(debounceTime)
                enabled = true
            }
        }
        true
    }
}
