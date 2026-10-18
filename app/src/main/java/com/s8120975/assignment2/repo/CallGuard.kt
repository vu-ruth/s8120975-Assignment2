package com.s8120975.assignment2.repo

import retrofit2.HttpException
import java.io.IOException

/**
 * Function that turns a failed HTTP call into a sentence the screens can show.
 * Note: error message, HttpException, network error.
 * 400, 401 and 403 use the sentence passed in by the gateway (wrong sign-in, or a bad keypass).
 * Any other status code includes the number. No internet becomes "No connection".
 * SignInFragment and CatalogueFragment display whatever message is thrown.
 */
internal suspend fun <T> guarded(rejectedMessage: String, call: suspend () -> T): T {
    try {
        return call()
    } catch (already: StudioException) {
        throw already
    } catch (http: HttpException) {
        val message = when (http.code()) {
            400, 401, 403 -> rejectedMessage
            else -> "The server replied with an error (${http.code()}). Try again."
        }
        throw StudioException(message)
    } catch (offline: IOException) {
        throw StudioException("No connection. Check the network and try again.")
    } catch (other: Exception) {
        throw StudioException("Something unexpected happened. Try again.")
    }
}
