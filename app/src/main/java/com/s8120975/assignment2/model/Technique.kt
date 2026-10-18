package com.s8120975.assignment2.model

/**
 * One photographic technique. Note: description.
 * The catalogue row shows technique, year, subject, photographer and equipment.
 * description is kept on the object and is only displayed on the detail screen.
 */
data class Technique(
    val technique: String,
    val equipment: String,
    val subject: String,
    val pioneeringPhotographer: String,
    val yearIntroduced: Int,
    val description: String,
)

data class Catalogue(
    val techniques: List<Technique>,
    val entityTotal: Int,
)

/**
 * Student ID helper. Note: student ID, username, leading s.
 * SignInViewModel.submit calls asUsername before RemoteAuth.signIn.
 */
object IdText {
    /** Function that turns s8120975 into 8120975. A value that is already digits is left alone. */
    fun asUsername(raw: String): String {
        val trimmed = raw.trim()
        if (trimmed.length <= 1) return trimmed
        val prefix = trimmed.first()
        if (prefix != 's' && prefix != 'S') return trimmed
        val rest = trimmed.drop(1)
        return if (rest.all { it.isDigit() }) rest else trimmed
    }
}
