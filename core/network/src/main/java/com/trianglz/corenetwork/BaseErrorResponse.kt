package com.trianglz.corenetwork

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class BaseErrorResponse(
    @Json(name = "status_code") val statusCode: Int?,
    @Json(name = "status_message") val statusMessage: String?
)
