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

/** Unit test for the catalogue endpoint. Note: unit test, dashboard endpoint, entityTotal, description. */
class RemoteCatalogueTest {
    private val server = MockWebServer()
    private lateinit var catalogue: RemoteCatalogue

    @Before
    fun setUp() {
        server.start()
        catalogue = RemoteCatalogue(buildPhotoApi(server.url("/").toString()))
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun readsTechniquesAndKeepsTheDescription() = runTest {
        server.enqueue(
            MockResponse().setResponseCode(200).setBody(
                """
                {
                  "entityTotal": 7,
                  "entities": [
                    {
                      "technique": "Long Exposure",
                      "equipment": "Tripod",
                      "subject": "Night sky",
                      "pioneeringPhotographer": "Michael Kenna",
                      "yearIntroduced": 1975,
                      "description": "Shutter left open."
                    }
                  ]
                }
                """.trimIndent(),
            ),
        )

        val page = catalogue.load("photography")

        assertEquals(7, page.entityTotal)
        assertEquals(1, page.techniques.size)
        val row = page.techniques.first()
        assertEquals("Long Exposure", row.technique)
        assertEquals("Tripod", row.equipment)
        assertEquals("Night sky", row.subject)
        assertEquals("Michael Kenna", row.pioneeringPhotographer)
        assertEquals(1975, row.yearIntroduced)
        assertEquals("Shutter left open.", row.description)
        val recorded = server.takeRequest()
        assertEquals("/dashboard/photography", recorded.path)
        assertTrue(recorded.method == "GET")
    }
}
