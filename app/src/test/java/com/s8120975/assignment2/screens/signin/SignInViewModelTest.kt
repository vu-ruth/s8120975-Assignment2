package com.s8120975.assignment2.screens.signin

import androidx.lifecycle.SavedStateHandle
import com.s8120975.assignment2.MainThreadRule
import com.s8120975.assignment2.repo.AuthGateway
import com.s8120975.assignment2.repo.StudioException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
/** Unit test for the sign-in function. Note: unit test, SignInViewModel, login function. */
class SignInViewModelTest {
    @get:Rule
    val mainThread = MainThreadRule()

    @Test
    fun emptyStudentIdStaysOnTheForm() {
        val auth = FakeAuth()
        val model = SignInViewModel(auth, SavedStateHandle())

        model.noteFirstName("Ruth")
        model.submit()

        assertEquals(SignInViewModel.NEED_ID, model.state.value.problem)
        assertEquals(0, auth.calls)
    }

    @Test
    fun emptyFirstNameStaysOnTheForm() {
        val auth = FakeAuth()
        val model = SignInViewModel(auth, SavedStateHandle())

        model.noteStudentId("8120975")
        model.submit()

        assertEquals(SignInViewModel.NEED_NAME, model.state.value.problem)
        assertEquals(0, auth.calls)
    }

    @Test
    fun acceptedLoginStripsThePrefixAndKeepsTheNameAsTyped() {
        val auth = FakeAuth()
        val model = SignInViewModel(auth, SavedStateHandle())

        model.noteStudentId("s8120975")
        model.noteFirstName("Ruth")
        model.submit()

        assertEquals("8120975", auth.lastUsername)
        assertEquals("Ruth", auth.lastPassword)
        assertEquals("photography", model.state.value.keypass)
        assertFalse(model.state.value.busy)
        assertNull(model.state.value.problem)
    }

    @Test
    fun rejectedLoginShowsTheGatewayMessage() {
        val auth = FakeAuth().apply {
            next = { throw StudioException("That student ID or first name was not accepted.") }
        }
        val model = SignInViewModel(auth, SavedStateHandle())

        model.noteStudentId("8120975")
        model.noteFirstName("ruth")
        model.submit()

        assertEquals("ruth", auth.lastPassword)
        assertEquals("That student ID or first name was not accepted.", model.state.value.problem)
        assertNull(model.state.value.keypass)
    }

    private class FakeAuth : AuthGateway {
        var next: () -> String = { "photography" }
        var lastUsername: String? = null
        var lastPassword: String? = null
        var calls = 0

        override suspend fun signIn(username: String, password: String): String {
            calls += 1
            lastUsername = username
            lastPassword = password
            return next()
        }
    }
}
