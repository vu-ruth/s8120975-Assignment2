package com.s8120975.assignment2.screens.catalogue

import androidx.lifecycle.SavedStateHandle
import com.s8120975.assignment2.MainThreadRule
import com.s8120975.assignment2.model.Catalogue
import com.s8120975.assignment2.model.Technique
import com.s8120975.assignment2.repo.CatalogueGateway
import com.s8120975.assignment2.repo.StudioException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
/** Unit test for the catalogue function. Note: unit test, CatalogueViewModel, dashboard function. */
class CatalogueViewModelTest {
    @get:Rule
    val mainThread = MainThreadRule()

    @Test
    fun loadsTheTechniquesAndTheTotal() {
        val gateway = FakeCatalogue()
        val model = CatalogueViewModel(gateway, SavedStateHandle(mapOf("keypass" to "photography")))

        assertEquals(1, gateway.calls)
        assertEquals("photography", gateway.lastKey)
        assertEquals(7, model.state.value.entityTotal)
        assertEquals("Long Exposure", model.state.value.techniques.first().technique)
        assertTrue(model.state.value.techniques.first().description.contains("shutter"))
    }

    @Test
    fun missingKeyDoesNotCallTheApi() {
        val gateway = FakeCatalogue()
        val model = CatalogueViewModel(gateway, SavedStateHandle())

        assertEquals(0, gateway.calls)
        assertEquals(CatalogueViewModel.NEED_KEY, model.state.value.problem)
    }

    @Test
    fun aFailedLoadShowsTheMessage() {
        val gateway = FakeCatalogue().apply {
            next = { throw StudioException("No connection. Check the network and try again.") }
        }
        val model = CatalogueViewModel(gateway, SavedStateHandle(mapOf("keypass" to "photography")))

        assertEquals("No connection. Check the network and try again.", model.state.value.problem)
        assertTrue(model.state.value.techniques.isEmpty())
    }

    private class FakeCatalogue : CatalogueGateway {
        var next: () -> Catalogue = {
            Catalogue(
                techniques = listOf(
                    Technique(
                        technique = "Long Exposure",
                        equipment = "Tripod",
                        subject = "Night sky",
                        pioneeringPhotographer = "Michael Kenna",
                        yearIntroduced = 1975,
                        description = "A technique where the camera's shutter is left open.",
                    ),
                ),
                entityTotal = 7,
            )
        }
        var calls = 0
        var lastKey: String? = null

        override suspend fun load(keypass: String): Catalogue {
            calls += 1
            lastKey = keypass
            return next()
        }
    }
}
