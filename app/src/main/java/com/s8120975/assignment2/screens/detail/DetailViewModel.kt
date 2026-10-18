package com.s8120975.assignment2.screens.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DetailState(
    val technique: String = "",
    val equipment: String = "",
    val subject: String = "",
    val pioneeringPhotographer: String = "",
    val yearLabel: String = "",
    val description: String = "",
    val missing: Boolean = false,
)

/**
 * Detail screen logic. Note: detail, description, navigation arguments.
 * Reads the arguments CatalogueFragment.open put on the navigation.
 * yearLabel is the year as text. description is kept so the detail layout can show it.
 */
class DetailViewModel(
    savedState: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(read(savedState))
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private fun read(savedState: SavedStateHandle): DetailState {
        val technique = savedState.get<String>(KEY_TECHNIQUE).orEmpty()
        if (technique.isBlank()) {
            return DetailState(missing = true)
        }
        val year = savedState.get<Int>(KEY_YEAR) ?: 0
        return DetailState(
            technique = technique,
            equipment = savedState.get<String>(KEY_EQUIPMENT).orEmpty(),
            subject = savedState.get<String>(KEY_SUBJECT).orEmpty(),
            pioneeringPhotographer = savedState.get<String>(KEY_PHOTOGRAPHER).orEmpty(),
            yearLabel = year.toString(),
            description = savedState.get<String>(KEY_DESCRIPTION).orEmpty(),
        )
    }

    companion object {
        const val KEY_TECHNIQUE = "technique"
        const val KEY_EQUIPMENT = "equipment"
        const val KEY_SUBJECT = "subject"
        const val KEY_PHOTOGRAPHER = "pioneeringPhotographer"
        const val KEY_YEAR = "yearIntroduced"
        const val KEY_DESCRIPTION = "description"
    }
}
