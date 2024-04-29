package com.github.crisacm.sessionmanager.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class NavItem(
    internal val baseRoute: String,
    private val navArgs: List<NavItemArgs> = emptyList()
) {
    object Login : NavItem("login")
    object Register : NavItem("register")
    object Home : NavItem("home")

    /*
    object NoteDetails : NavItem("noteDetails", listOf(NavItemArgs.NoteId)) {
        fun createRoute(noteId: Long) = "$baseRoute/$noteId"
    }
    */

    val route = run {
        val argValue = navArgs.map { "{${it.key}}" }
        listOf(baseRoute)
            .plus(argValue)
            .joinToString("/")
    }

    val args = navArgs.map {
        navArgument(it.key) { type = it.navType }
    }
}

enum class NavItemArgs(val key: String, val navType: NavType<*>) {
    // NoteId("noteId", NavType.LongType)
}
