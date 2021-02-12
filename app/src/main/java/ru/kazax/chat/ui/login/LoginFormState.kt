package ru.kazax.chat.ui.login

/**
 * Data validation state of the Login form.
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val passwordRetypeError: Int? = null,
    val isDataValid: Boolean = false
)