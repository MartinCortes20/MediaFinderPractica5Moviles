package com.escom.mediafinder.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Favorites : Screen("favorites")
    object History : Screen("history")
    object Recommendations : Screen("recommendations")
}