package com.github.crisacm.sessionmanager.presentation.screens.login

object LoginValidations {

    fun isValidEmail(email: String):Boolean {
        val emailPattern = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
        return emailPattern.matches(email)
    }
}
