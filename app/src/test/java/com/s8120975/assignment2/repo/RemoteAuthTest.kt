package com.s8120975.assignment2.repo

import com.s8120975.assignment2.net.buildPhotoApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Unit test for the sign-in endpoint. Note: unit test, login endpoint, footscray/auth. */
class RemoteAuthTest {
    private val server = MockWebServer()
    private lateinit var auth: RemoteAuth

    @Before
    fun setUp() {
        server.start()
        auth = RemoteAuth(buildPhotoApi(server.url("/").toString()))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun postsTheFootscrayLoginAndReturnsTheKey() = runTest {
        server.enqueue(MockResponse().setResponseCode(200).setBody("""{"keypass":"photography"}"""))

        val key = auth.signIn("8120975", "Ruth")

        assertEquals("photography", key)
        val recorded = server.takeRequest()
        assertEquals("/footscray/auth", recorded.path)
        assertEquals("POST", recorded.method)
        val body = recorded.body.readUtf8()
        assertTrue(body.contains("8120975"))
        assertTrue(body.contains("Ruth"))
    }

    @Test
    fun aRejectedLoginBecomesAReadableError() = runTest {
        server.enqueue(MockResponse().setResponseCode(400).setBody(""))

        val error = runCatching { auth.signIn("8120975", "wrong") }.exceptionOrNull()

        assertTrue(error is StudioException)
        assertTrue(error?.message?.contains("not accepted") == true)
    }
}
