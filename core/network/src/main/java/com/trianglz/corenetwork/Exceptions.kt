package com.example.network

import java.io.IOException


/**
 * Exception represents IO exception or Http exception with code [HTTP_GATEWAY_TIMEOUT]
 */
 object NetworkException : IOException("Network Exception") {
    private fun readResolve(): Any = NetworkException
}

/**
 * Exception represents no network connectivity
 */
object NoNetworkException : IOException("No Network Connection Available") {
    private fun readResolve(): Any = NoNetworkException
}

/**
 * Exception represents Http Exception with error body of [ErrorBodyResponse]
 */
open class GeneralHttpException(
    val statusCode: Int?,
    val errorMsg: String?
) : Exception("General Http Exception")

/**
 * Exception represents unauthorized user
 */
class UnauthorizedException : Exception("Unauthorized Exception")

/**
 * Exception represents any unknown Exception
 */
class UnknownException(message: String) : Exception(message)

/**
 * Exception represents parsing error
 * */
class ParsingException(message: String) : Exception(message)
