package com.s8120975.assignment2.net

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

/**
 * Retrofit API. Note: sign-in endpoint, login endpoint, catalogue endpoint, dashboard endpoint, keypass, Moshi.
 * signIn is the login endpoint: POST footscray/auth. The body is username and password.
 * catalogue is the dashboard endpoint: GET dashboard/{keypass}.
 * buildPhotoApi below attaches Moshi and the base URL.
 */
interface PhotoApi {
    /** Login endpoint. POST footscray/auth. Returns the keypass. For Ruth that keypass is photography. */
    @POST("footscray/auth")
    suspend fun signIn(@Body body: SignInBody): KeypassBody

    /** Dashboard endpoint. GET dashboard/{keypass}. Returns entities and entityTotal. */
    @GET("dashboard/{keypass}")
    suspend fun catalogue(@Path("keypass") keypass: String): CatalogueBody
}

data class SignInBody(
    val username: String,
    val password: String,
)

data class KeypassBody(
    val keypass: String = "",
)

data class TechniqueBody(
    val technique: String = "",
    val equipment: String = "",
    val subject: String = "",
    val pioneeringPhotographer: String = "",
    val yearIntroduced: Int = 0,
    val description: String = "",
)

data class CatalogueBody(
    val entities: List<TechniqueBody> = emptyList(),
    val entityTotal: Int = 0,
)

/** Base URL for both the sign-in endpoint and the catalogue endpoint. */
private const val HOST = "https://nit3213apinew.onrender.com/"

/**
 * Function that builds Retrofit. Note: base URL, Moshi, OkHttp.
 * Timeouts are 60 seconds because the host sleeps until the first request wakes it.
 * Tests pass a MockWebServer URL instead of HOST.
 */
fun buildPhotoApi(baseUrl: String = HOST): PhotoApi {
    val logger = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logger)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()
    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(PhotoApi::class.java)
}
