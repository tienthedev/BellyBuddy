package com.example.bellybuddy.navigation

sealed class Route(val path: String) {
    data object Login : Route("login")
    data object Dashboard : Route("dashboard")
    data object Settings : Route("settings")
    data object Grid: Route("grid")
    data object Calendar: Route("calendar")
    data object Bell: Route("bell")
}
