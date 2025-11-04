package com.example.bellybuddy.navigation

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Dashboard : Route("dashboard")
    data object Settings : Route("settings")
    data object Grid: Route("grid")
    data object Calendar: Route("calendar")
    data object Bell: Route("bell")
    data object Profile : Route("profile")
    data object DailyJournaling: Route("daily_journaling")
    data object FoodLogging: Route("food_logging")
    data object SymptomTracking: Route("symptom_tracking")
    data object BowelMovementTracking: Route("bowel_movement_tracking")
    data object DailyScore : Route("daily_score")
    data object EditProfile : Route("edit_profile")
    data object Weight : Route("weight")
    data object Reminder : Route("reminder")
}
