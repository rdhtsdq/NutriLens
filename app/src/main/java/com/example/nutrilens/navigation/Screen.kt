package com.example.nutrilens.navigation

sealed class Screen(val route: String, val label: String) {
    data object Home : Screen("home", "Home")
    data object Scanner : Screen("scanner", "Scanner")
    data object Analytics : Screen("analytics", "Analytics")
    data object Result : Screen("result/{foodName}/{calories}/{sugar}/{riskLevel}", "Result") {
        fun createRoute(foodName: String, calories: Int, sugar: Double, riskLevel: String): String =
            "result/$foodName/$calories/$sugar/$riskLevel"
    }
    data object ManualInput : Screen("manual_input/{foodName}", "Manual Input") {
        fun createRoute(foodName: String): String =
            "manual_input/$foodName"
    }
    data object Settings : Screen("settings", "Settings")
}
