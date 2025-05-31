package com.trianglz.corenetwork

import com.example.network.GeneralHttpException
import com.example.network.NetworkException
import com.example.network.NoNetworkException
import com.example.network.ParsingException
import com.example.network.UnauthorizedException
import com.example.network.UnknownException
import com.squareup.moshi.Moshi
import okio.IOException
import retrofit2.Response

object NetworkHelper {
    suspend inline fun <reified T> executeCall(networkCall: () -> Response<T>): T? {
        val response = try {
            networkCall.invoke()
        } catch (throwable: NoNetworkException) {
            throw NoNetworkException
        } catch (throwable: IOException) {
            throw NetworkException
        } catch (throwable: Throwable) {
            throw UnknownException("Unknown Exception: ${throwable.message}")
        }

        return handleResponse(response)
    }

    suspend inline fun <reified T> handleResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            val body = response.body()
            return body ?: throw ParsingException("Response body is null")
        } else {
            val errorBody = try {
                val errorJson = response.errorBody()?.string()
                if (errorJson.isNullOrEmpty()) {
                    throw ParsingException("Empty error body")
                }
                parseErrorBody<BaseErrorResponse>(errorJson)
            } catch (e: Exception) {
                throw ParsingException("Failed to parse error body: ${e.message}")
            }

            when (response.code()) {
                401 -> throw UnauthorizedException()
                in 400..599 -> throw GeneralHttpException(
                    errorMsg = errorBody.statusMessage,
                    statusCode = errorBody.statusCode
                )

                else -> throw UnknownException("Unexpected HTTP status code: ${response.code()}")
            }
        }
    }

    inline fun <reified T> parseErrorBody(json: String): T {
        val moshi = Moshi.Builder().build()
        val adapter = moshi.adapter(T::class.java)
        return adapter.fromJson(json) ?: throw ParsingException("Empty parsed error body")
    }

}
