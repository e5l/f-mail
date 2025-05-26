package com.fmail.frontend.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.fmail.frontend.ui.screens.MainScreen
import com.fmail.frontend.ui.theme.FMailTheme

@Composable
fun FMailApp() {
    FMailTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            MainScreen()
        }
    }
}