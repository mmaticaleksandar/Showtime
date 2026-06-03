package com.example.showtime

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import core.di.AppModule

import data.local.DesktopDatabaseFactory
import data.local.auth.createJvmTokenStorage
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState


fun main() = application {
    AppModule.setDatabaseFactory(
        DesktopDatabaseFactory()
    )

    AppModule.init(
        tokenStorage = createJvmTokenStorage()
    )

    val windowState = rememberWindowState(
        placement = WindowPlacement.Maximized
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Showtime",
        state = windowState
    ) {
        App()
    }
}