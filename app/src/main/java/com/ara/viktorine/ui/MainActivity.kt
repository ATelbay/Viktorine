package com.ara.viktorine.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ara.viktorine.ui.theme.ViktorineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViktorineTheme {
                ViktorineScreen()
            }
        }
    }
}