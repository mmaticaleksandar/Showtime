package com.example.showtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import core.di.AppModule
import data.local.AndroidDatabaseFactory
import data.local.auth.AndroidTokenStorage
import presentation.common.AppCloser

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        AppModule.setDatabaseFactory(
            AndroidDatabaseFactory(applicationContext)
        )
        AppModule.setTokenStorage(
            AndroidTokenStorage(applicationContext)
        )

        setContent {
            App(appCloser = AppCloser{finish()})
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}