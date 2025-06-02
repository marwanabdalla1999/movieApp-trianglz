package com.trianglz.ui.utils

import com.trianglz.ui.constants.UiConstants

fun String?.getFullPosterUrl(): String {
        return this?.let { "${UiConstants.IMAGE_BASE_URL_W780}$it" }?:""
    }