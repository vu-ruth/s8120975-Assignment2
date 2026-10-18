package com.s8120975.assignment2.screens.signin

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.s8120975.assignment2.model.IdText
import com.s8120975.assignment2.repo.AuthGateway
import com.s8120975.assignment2.repo.StudioException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Sign-in screen logic. Note: function that signs the user in, ViewModel, StateFlow, student ID, password.
 * The layout is fragment_sign_in.xml. SignInFragment shows this ViewModel's state.
 * Koin creates this class in StudioModule and passes AuthGateway, which is RemoteAuth.
 * This class does not build the URL. RemoteAuth calls the sign-in endpoint.
 */
class SignInViewModel(
    private val auth: AuthGateway,
    private val savedState: SavedStateHandle,
) : ViewModel() {

    private val _state = MutableStateFlow(
        SignInState(
            studentId = savedState.get<String>(KEY_ID).orEmpty(),
            firstName = savedState.get<String>(KEY_NAME).orEmpty(),
        ),
    )
    val state: StateFlow<SignInState> = _state.asStateFlow()

    fun noteStudentId(value: String) {
        savedState[KEY_ID] = value
        _state.update { it.copy(studentId = value, problem = null) }
    }

    fun noteFirstName(value: String) {
        savedState[KEY_NAME] = value
        _state.update { it.copy(firstName = value, problem = null) }
    }

    /**
     * Function that signs the user in.
     * An empty student ID or an empty first name sets a problem and does not call the endpoint.
     * Otherwise it calls AuthGateway.signIn, which POSTs to footscray/auth.
     * Success stores the keypass. SignInFragment then opens the catalogue.
     * Failure stores the error sentence. SignInFragment shows it on the sign-in layout.
     */
    fun submit() {
        if (_state.value.busy) return

        val username = IdText.asUsername(_state.value.studentId)
        val password = _state.value.firstName
        if (username.isEmpty()) {
            _state.update { it.copy(problem = NEED_ID) }
            return
        }
        if (password.isEmpty()) {
            _state.update { it.copy(problem = NEED_NAME) }
            return
        }

        _state.update { it.copy(busy = true, problem = null, keypass = null) }
        viewModelScope.launch {
            try {
                val keypass = auth.signIn(username, password)
                _state.update { it.copy(busy = false, keypass = keypass) }
            } catch (error: StudioException) {
                _state.update { it.copy(busy = false, problem = error.message ?: UNEXPECTED) }
            } catch (error: Exception) {
                _state.update { it.copy(busy = false, problem = UNEXPECTED) }
            }
        }
    }

    fun keypassConsumed() {
        _state.update { it.copy(keypass = null) }
    }

    companion object {
        const val NEED_ID = "Type your student ID."
        const val NEED_NAME = "Type your first name."
        const val UNEXPECTED = "Something unexpected happened. Try again."
        private const val KEY_ID = "studentId"
        private const val KEY_NAME = "firstName"
    }
}
