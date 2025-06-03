package com.trianglz.corenetwork

object NetworkConstants {
    const val BASE_URL = BuildConfig.BASE_URL

    object Headers {
        const val AUTHORIZATION = "Authorization"
        const val API_TOKEN = BuildConfig.USER_TOKEN

    }
    object Paging {
         const val PAGE_SIZE = 20
        const val PREFETCH_DISTANCE = 40

    }
}