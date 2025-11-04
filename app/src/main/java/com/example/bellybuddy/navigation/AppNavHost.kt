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
import com.example.bellybuddy.userint.screen.ProfileScreen
import com.example.bellybuddy.userint.screen.SymptomScreen
import com.example.bellybuddy.userint.screen.EditProfileScreen
import com.example.bellybuddy.userint.screen.DailyScoreScreen
import com.example.bellybuddy.userint.screen.WeightScreen
import com.example.bellybuddy.userint.screen.UserListScreen

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



        // Add the new destination to the NavHost
        composable(Route.UserList.path) {
            UserListScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Dashboard.path) {
            DashboardScreen(
                onProfileClick = { go(Route.Profile) },
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard) // already here; keeps highlight
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onLogout = { navController.popBackStack(Route.Login.path, inclusive = false) },
                onDailyScoreClick = { navController.navigate(Route.DailyScore.path) },
                onWeightClick = { navController.navigate(Route.Weight.path) },
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
                onBowelMovementClick = { go(Route.BowelMovementTracking) },
                onUserListClick = { navController.navigate(Route.UserList.path) }
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

        composable(Route.Profile.path) {
            ProfileScreen(
                onSelectBottom = { item ->
                    when (item) {
                        BottomItem.Home -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell -> go(Route.Bell)
                    }
                },
                onBack = { go(Route.Dashboard) }, // back button
                onGoToSettings = { go(Route.Settings) },
                onEditProfile = { go(Route.EditProfile) }
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
                },
                onBack = { go(Route.Grid) }
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
                },
                onBack = { go(Route.Grid) }
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
                onBack = { go(Route.Grid) }
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
                },
                onBack = { go(Route.Grid) }
            )
        }

        composable(Route.EditProfile.path) {
            EditProfileScreen(
                onSave = { _, _, _ -> go(Route.Profile) },
                onBack = { go(Route.Profile) }
            )
        }

        composable(Route.DailyScore.path) {
            DailyScoreScreen(
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onBack = {
                    navController.popBackStack(Route.Dashboard.path, inclusive = false)
                }
            )
        }

        composable(Route.Weight.path) {
            WeightScreen(
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onBack = {
                    navController.popBackStack(Route.Dashboard.path, inclusive = false)
                }
            )
        }
    }
}
