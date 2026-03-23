package com.example.bellybuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bellybuddy.userint.screen.*
import com.example.bellybuddy.viewmodel.UserViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    userViewModel: UserViewModel
) {
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
        startDestination = Route.Splash.path
    ) {
        composable(Route.Splash.path) {
            SplashScreen(
                userViewModel = userViewModel,
                onNavigateToProfile = {
                    navController.navigate(Route.InitialProfile.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                },
                onNavigateToDashboard = {
                    navController.navigate(Route.Dashboard.path) {
                        popUpTo(Route.Splash.path) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.InitialProfile.path) {
            InitialProfileScreen(
                onComplete = { name, age, weight, height, _ ->
                    userViewModel.registerUser(
                        name = name,
                        email = "user@example.com",
                        password = "password",
                        age = age,
                        weight = weight,
                        height = height
                    ) { success, _ ->
                        if (success) {
                            navController.navigate(Route.Dashboard.path) {
                                popUpTo(Route.InitialProfile.path) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }

        composable(Route.Login.path) {
            LoginScreen(
                onLoginSuccess = { go(Route.Dashboard) }
            )
        }

        composable(Route.UserList.path) {
            UserListScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(Route.Dashboard.path) {
            DashboardScreen(
                userViewModel = userViewModel,
                onProfileClick = { go(Route.Profile) },
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onLogout = { 
                    userViewModel.logout()
                    navController.navigate(Route.InitialProfile.path) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onDailyScoreClick = { navController.navigate(Route.DailyScore.path) },
                onWeightClick = { navController.navigate(Route.Weight.path) },
                onReminderClick = { go(Route.Bell) }
            )
        }

        composable(Route.Settings.path) {
            SettingsScreen(
                userViewModel = userViewModel,
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
                onDailyScoreClickv2 = { navController.navigate(Route.DailyScore.path) },
                onWeightClickv2 = { navController.navigate(Route.Weight.path) },
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
                onBack = { go(Route.Dashboard) },
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
                score = 88,
                onBottomSelect = { item ->
                    when (item) {
                        BottomItem.Home     -> go(Route.Dashboard)
                        BottomItem.Settings -> go(Route.Settings)
                        BottomItem.Grid     -> go(Route.Grid)
                        BottomItem.Calendar -> go(Route.Calendar)
                        BottomItem.Bell     -> go(Route.Bell)
                    }
                },
                onBack = { navController.popBackStack() }
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
                onBack = { navController.popBackStack() }
            )
        }
    }
}
