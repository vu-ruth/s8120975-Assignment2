package com.s8120975.assignment2.screens.catalogue

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s8120975.assignment2.repo.CatalogueGateway
import com.s8120975.assignment2.repo.StudioException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Catalogue screen logic. Note: catalogue function, dashboard function, keypass, ViewModel.
 * Layout: res/layout/fragment_catalogue.xml. The keypass arrives as a navigation argument.
 * init calls refresh() as soon as the screen is created.
 */
class CatalogueViewModel(
    private val catalogue: CatalogueGateway,
    savedState: SavedStateHandle,
) : ViewModel() {

    private val keypass: String = savedState.get<String>(KEY_KEYPASS).orEmpty()

    private val _state = MutableStateFlow(CatalogueState())
    val state: StateFlow<CatalogueState> = _state.asStateFlow()

    init {
        refresh()
    }

    /**
     * Function that loads the catalogue. This is the dashboard function.
     * Calls CatalogueGateway.load, which is GET dashboard/{keypass}.
     * Success puts the techniques and entityTotal into state.
     * The description stays on each Technique. The RecyclerView layout does not display it.
     * Failure puts an error sentence into state. CatalogueFragment shows a retry button.
     */
    fun refresh() {
        if (keypass.isBlank()) {
            _state.update { it.copy(busy = false, problem = NEED_KEY) }
            return
        }
        _state.update { it.copy(busy = true, problem = null) }
        viewModelScope.launch {
            try {
                val page = catalogue.load(keypass)
                _state.update {
                    it.copy(
                        busy = false,
                        techniques = page.techniques,
                        entityTotal = page.entityTotal,
                    )
                }
            } catch (error: StudioException) {
                _state.update { it.copy(busy = false, problem = error.message ?: UNEXPECTED) }
            } catch (error: Exception) {
                _state.update { it.copy(busy = false, problem = UNEXPECTED) }
            }
        }
    }

    companion object {
        const val KEY_KEYPASS = "keypass"
        const val NEED_KEY = "Sign in again to open the catalogue."
        const val UNEXPECTED = "Something unexpected happened. Try again."
    }
}
