package com.github.crisacm.sessionmanager.util

object FieldValidations {

    fun isValidEmail(email: String): FieldError? {
        val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        if (!emailPattern.matches(email)) {
            return FieldError(true, "Invalid Email")
        }

        return null
    }

    fun isEmpty(email: String) : FieldError? {
        if (email.isEmpty()) {
            return FieldError(true, "Please insert a value")
        }

        return null
    }
}

data class FieldError(
    val isError: Boolean,
    val errorMessage: String
)
