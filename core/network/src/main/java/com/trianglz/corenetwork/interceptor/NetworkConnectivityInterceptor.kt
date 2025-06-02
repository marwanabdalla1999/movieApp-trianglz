package com.trianglz.corenetwork.interceptor


import com.example.network.NoNetworkException
import com.trianglz.corenetwork.NetworkConstants
import com.trianglz.corenetwork.connectivity.INetworkConnectivity
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkConnectivityInterceptor(
    private val networkChecker: INetworkConnectivity
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkChecker.isNetworkAvailable()) {
            throw NoNetworkException
        }

        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader(NetworkConstants.Headers.AUTHORIZATION, NetworkConstants.Headers.API_TOKEN)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}
