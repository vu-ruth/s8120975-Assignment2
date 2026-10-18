package com.s8120975.assignment2.screens.signin

data class SignInState(
    val studentId: String = "",
    val firstName: String = "",
    val busy: Boolean = false,
    val problem: String? = null,
    val keypass: String? = null,
)
