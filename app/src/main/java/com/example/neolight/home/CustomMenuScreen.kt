package com.example.neolight.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.neolight.core.BottomNavigationBar
import com.example.neolight.core.TopBar
import com.example.neolight.ui.theme.NeoLightTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomMenuScreen() {
    NeoLightTheme {
        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomNavigationBar() }
        ) {
            Column(verticalArrangement = Arrangement.SpaceEvenly) {
                Card(
                    content = { Text(text = "Test")}
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomMenuScreenPreview() {
    CustomMenuScreen()
}