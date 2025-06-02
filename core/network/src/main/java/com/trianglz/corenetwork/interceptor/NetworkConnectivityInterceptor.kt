package com.trianglz.corenetwork.interceptor


import android.util.Log
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
        Log.d("NetworkInterceptor", "Request URL: ${originalRequest.url}")
        Log.d("NetworkInterceptor", "Request Method: ${originalRequest.method}")
        Log.d("NetworkInterceptor", "Request Headers: ${originalRequest.headers}")
        val newRequest = originalRequest.newBuilder()
            .addHeader(NetworkConstants.Headers.AUTHORIZATION, NetworkConstants.Headers.API_TOKEN)
            .addHeader("Content-Type", "application/json")
            .addHeader("Accept", "application/json")
            .build()
        val response = chain.proceed(newRequest)

        // Log response info
        Log.d("NetworkInterceptor", "Response Code: ${response.code}")
        Log.d("NetworkInterceptor", "Response Message: ${response.message}")
        Log.d("NetworkInterceptor", "Response Headers: ${response.headers}")

        return response
    }
}
