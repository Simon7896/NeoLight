package com.example.neolight.ui

import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.neolight.R
import com.example.neolight.ui.flashoption.FlashOptionViewModel
import com.example.neolight.ui.theme.NeoLightTheme

sealed class Screen(val route: String, @get:StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : Screen("home", R.string.home, Icons.Default.Home)
    object Select : Screen("select", R.string.select, Icons.Default.List)
}

class MainActivity : ComponentActivity() {

    private val flashOptionViewModel: FlashOptionViewModel by viewModels {
        FlashOptionViewModel.Factory
    }

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasFlash =
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        cameraId = cameraManager.cameraIdList[0]

        handler = Handler(Looper.getMainLooper())

        setContent {
            
            if (!hasFlash) {
                NoFlashAlert()
            }

            val navController = rememberNavController()

            val items = listOf(
                Screen.Home,
                Screen.Select,
            )

            Scaffold(
                topBar = { },
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
                    composable(Screen.Home.route) { HomeScreen(
                        viewModel = flashOptionViewModel,
                        onClick = ::startTorch
                    ) }
                    composable(Screen.Select.route) { CustomMenuScreen(
                        viewModel = flashOptionViewModel
                    ) }
                }
            }
        }
    }

    private fun setTorch(mode: Boolean) {
        try {
            cameraManager.setTorchMode(cameraId, mode)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun startTorch(mode: Boolean, delay: Long) {
        handler.removeCallbacksAndMessages(null)
        if (mode) {
            if (delay == 0L) {
                setTorch(true)
            } else {
                val runnable = object : Runnable {
                    var toggle = true
                    override fun run() {
                        setTorch(toggle)
                        toggle = !toggle
                        handler.postDelayed(this, delay)
                    }
                }
                handler.post(runnable)
            }
        } else {
            setTorch(false)
        }
    }
}

@Composable
fun HomeScreen(
        viewModel: FlashOptionViewModel,
        onClick: (mode: Boolean, delay: Long) -> Unit,
    ) {

    val (isTorchOn, onTorchChange) = remember {
        mutableStateOf(false)
    }

    NeoLightTheme {
        Scaffold {
            paddingValues -> Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                TorchToggleButton(
                    isChecked = isTorchOn,
                    onClick = { 
                        val newMode = !isTorchOn
                        onClick(newMode, viewModel.selected.delay)
                        onTorchChange(newMode) 
                    }
                )
            }
        }
    }
}

@Composable
fun TorchToggleButton(
    isChecked: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier= Modifier.size(100.dp),  //avoid the oval shape
        contentPadding = PaddingValues(0.dp)  //avoid the little icon
    ) {
        Icon(
            painter = painterResource(
                id = if (isChecked) R.drawable.baseline_flashlight_on_24 else {
                        R.drawable.baseline_flashlight_off_24
                }
            ),
            contentDescription = "Torch",
        )
    }
}

@Composable
fun NoFlashAlert() {
    NeoLightTheme {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {},
        )
    }
}