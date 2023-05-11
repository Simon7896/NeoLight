package com.example.neolight.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar() {
    NavigationBar() {
        NavigationBarItem(
            selected = true,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text(text = "Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.List, contentDescription = "Custom") },
            label = { Text(text = "Custom") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /*TODO*/ },
            icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
            label = { Text(text = "Settings") }
        )
    }
}