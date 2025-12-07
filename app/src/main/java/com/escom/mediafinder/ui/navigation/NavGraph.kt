package com.escom.mediafinder.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.escom.mediafinder.ui.screens.*
import com.escom.mediafinder.ui.viewmodel.AuthViewModel
import com.escom.mediafinder.ui.viewmodel.ShowViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel = hiltViewModel(),
    showViewModel: ShowViewModel = hiltViewModel()
) {
    val currentUser by authViewModel.currentUser.collectAsState()

    val startDestination = if (currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            currentUser?.let { user ->
                HomeScreen(
                    user = user,
                    showViewModel = showViewModel,
                    onNavigateToFavorites = {
                        navController.navigate(Screen.Favorites.route)
                    },
                    onNavigateToHistory = {
                        navController.navigate(Screen.History.route)
                    },
                    onNavigateToRecommendations = {
                        navController.navigate(Screen.Recommendations.route)
                    },
                    onLogout = {
                        authViewModel.logout()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }

        composable(Screen.Favorites.route) {
            currentUser?.let { user ->
                FavoritesScreen(
                    user = user,
                    showViewModel = showViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }

        composable(Screen.History.route) {
            currentUser?.let { user ->
                HistoryScreen(
                    user = user,
                    showViewModel = showViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    },
                    onSearchQuery = { query ->
                        showViewModel.searchShows(query, user.id)
                    }
                )
            }
        }

        composable(Screen.Recommendations.route) {
            currentUser?.let { user ->
                RecommendationsScreen(
                    user = user,
                    showViewModel = showViewModel,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}