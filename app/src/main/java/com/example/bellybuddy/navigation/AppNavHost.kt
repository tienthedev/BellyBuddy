package com.example.bellybuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bellybuddy.userint.screen.DashboardScreen
import com.example.bellybuddy.userint.screen.LoginScreen
import com.example.bellybuddy.userint.screen.SettingsScreen
import com.example.bellybuddy.userint.screen.BottomItem
import com.example.bellybuddy.userint.screen.BowelMovementScreen
import com.example.bellybuddy.userint.screen.GridScreen
import com.example.bellybuddy.userint.screen.CalendarScreen
import com.example.bellybuddy.userint.screen.DailyJournalingScreen
import com.example.bellybuddy.userint.screen.FoodLoggingScreen
import com.example.bellybuddy.userint.screen.ReminderScreen
import com.example.bellybuddy.userint.screen.SymptomScreen

@Composable
fun AppNavHost(navController: NavHostController) {

    // Helper to navigate without piling up duplicates of top-level screens
    fun go(route: Route) {
        navController.navigate(route.path) {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.startDestinationId) { saveState = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Route.Login.path
    ) {
        composable(Route.Login.path) {
            LoginScreen(
                onLoginSuccess = { go(Route.Dashboard) }
            )
        }

        composable(Route.Dashboard.path) {
            DashboardScreen(
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard) // already here; keeps highlight
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onLogout = { navController.popBackStack(Route.Login.path, inclusive = false) }
            )
        }

        composable(Route.Settings.path) {
            SettingsScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> Unit
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                }
            )
        }

        composable(Route.Grid.path) {
            GridScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid -> Unit
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell -> go(Route.Bell)
                    }
                },
                onJournalClick = { go(Route.DailyJournaling) },
                onFoodLogClick = { go(Route.FoodLogging) },
                onSymptomClick = { go(Route.SymptomTracking) },
                onBowelMovementClick = { go(Route.BowelMovementTracking) }
            )
        }

        composable(Route.Calendar.path) {
            CalendarScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> Unit
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                }
            )
        }

        composable(Route.Bell.path) {
            ReminderScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell -> Unit
                    }
                }
            )
        }

        composable(Route.DailyJournaling.path) {
            DailyJournalingScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                }
            )
        }

        composable(Route.FoodLogging.path) {
            FoodLoggingScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                }
            )
        }

        composable(Route.SymptomTracking.path) {
            SymptomScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onBackClick = { go(Route.Grid) }
            )
        }

        composable(Route.BowelMovementTracking.path) {
            BowelMovementScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                }
            )
        }


    }
}
