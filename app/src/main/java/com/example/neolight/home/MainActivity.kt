package com.example.neolight.home

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.neolight.R
import com.example.neolight.core.TopBar
import com.example.neolight.ui.theme.NeoLightTheme

sealed class Screen(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Select : Screen("select", R.string.select, Icons.Default.List)
    object Settings: Screen("settings", R.string.settings, Icons.Default.Settings)
}

class MainActivity : ComponentActivity() {

    private var isTorchOn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasFlash =
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        setContent {
            val navController = rememberNavController()

            val items = listOf(
                Screen.Home,
                Screen.Select,
                Screen.Settings
            )

            Scaffold(
                topBar = { TopBar() },
                bottomBar = {
                    NavigationBar {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination
                        items.forEach { screen ->
                            NavigationBarItem(
                                icon = { Icon(screen.icon, contentDescription = null) },
                                label = { Text(stringResource(id = screen.resourceId)) },
                                selected = currentDestination?.hierarchy?.any { it.route == screen.route} == true,
                                onClick = {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            ) { paddingValues ->
                NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(paddingValues)) {
                    composable(Screen.Home.route) { HomeScreen(onClick = {::toggleTorch}) }
                    composable(Screen.Select.route) { CustomMenuScreen() }
                    composable(Screen.Settings.route) {}
                }
            }
        }
    }

    private fun toggleTorch() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        try {
            cameraManager.setTorchMode(cameraId, !isTorchOn)
            isTorchOn = !isTorchOn
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }
}

@Composable
fun HomeScreen(
        onClick: () -> Unit,
    ) {
    NeoLightTheme {
        Scaffold(
        ) {
            paddingValues -> Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                TorchToggleButton(onClick)
            }
        }
    }
}

@Composable
fun TorchToggleButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier= Modifier.size(100.dp),  //avoid the oval shape
        contentPadding = PaddingValues(0.dp)  //avoid the little icon
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_flashlight_on_24),
            contentDescription = "Torch",
        )
    }
}

@Composable
fun NoFlashAlert(hasFlash: Boolean) {
    if (hasFlash) { return }

    NeoLightTheme {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    NeoLightTheme {
        HomeScreen() {
        }
    }
}