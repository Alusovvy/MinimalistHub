package com.example.minimalistinfohub

import androidx.annotation.DrawableRes

sealed class Screen(val title: String, val route: String) {

    sealed class BottomScreen(
        val bTitle: String, val bRoute: String, @DrawableRes val icon: Int
    ) : Screen(bTitle, bRoute) {
        object Stock : BottomScreen("Top Stock", "stockWatch", R.drawable.baseline_attach_money_24)

        object Weather : BottomScreen("Local Weather", "weather", R.drawable.baseline_terrain_24)

        object Finance : BottomScreen("Financial News", "financialNews", R.drawable.baseline_newspaper_24)
    }

    sealed class DrawerScreen(val dTitle: String, val dRoute: String) : Screen(dTitle, dRoute) {
        object Stock : DrawerScreen("Top Stock", "stockWatch")

        object Weather : DrawerScreen("Local Weather", "weather")

        object Finance : DrawerScreen("Financial News", "financialNews")
    }

}

val screensInDrawer = listOf(
    Screen.DrawerScreen.Stock,
    Screen.DrawerScreen.Finance,
    Screen.DrawerScreen.Weather
)

val screensInBottom = listOf(
    Screen.BottomScreen.Finance,
    Screen.BottomScreen.Stock,
    Screen.BottomScreen.Weather
)