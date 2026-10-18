package com.s8120975.assignment2.screens.detail

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/** Unit test for the detail screen. Note: unit test, description, DetailViewModel. */
class DetailViewModelTest {
    @Test
    fun readsEveryFieldIncludingTheDescription() {
        val model = DetailViewModel(
            SavedStateHandle(
                mapOf(
                    "technique" to "Long Exposure",
                    "equipment" to "Tripod",
                    "subject" to "Night sky",
                    "pioneeringPhotographer" to "Michael Kenna",
                    "yearIntroduced" to 1975,
                    "description" to "A technique where the camera's shutter is left open.",
                ),
            ),
        )

        val state = model.state.value
        assertEquals("Long Exposure", state.technique)
        assertEquals("Tripod", state.equipment)
        assertEquals("Night sky", state.subject)
        assertEquals("Michael Kenna", state.pioneeringPhotographer)
        assertEquals("1975", state.yearLabel)
        assertEquals("A technique where the camera's shutter is left open.", state.description)
        assertFalse(state.missing)
    }

    @Test
    fun aBlankTechniqueIsMarkedMissing() {
        val model = DetailViewModel(SavedStateHandle())
        assertTrue(model.state.value.missing)
    }
}
