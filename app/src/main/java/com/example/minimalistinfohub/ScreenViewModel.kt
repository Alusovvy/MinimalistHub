package com.example.minimalistinfohub

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ScreenViewModel : ViewModel() {
    private val _currentScreen: MutableState<Screen> = mutableStateOf(Screen.DrawerScreen.Stock)

    val currentScreen : MutableState<Screen>
        get() = _currentScreen

    fun setCurrentScreen(screen: Screen) {
        _currentScreen.value = screen
    }
}