package com.example.neolight.home

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.neolight.R
import com.example.neolight.core.TopBar
import com.example.neolight.ui.theme.NeoLightTheme

class MainActivity : ComponentActivity() {

    private var isTorchOn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hasFlash =
            applicationContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)

        setContent {

            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    HomeScreen(
                        navController = navController,
                        hasFlash = hasFlash,
                        onClick = ::toggleTorch
                    )
                }
                composable("select") {

                }
                composable("options") {

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
    navController: NavController,
    hasFlash: Boolean,
    onClick: () -> Unit,
) {
    NeoLightTheme {
        // A surface container using the 'background' color from the theme
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column {
                TopBar()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                NoFlashAlert(hasFlash = hasFlash)
                TorchToggleButton(onClick)
            }
            Column(verticalArrangement = Arrangement.Bottom) {

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
        HomeScreen(navController = rememberNavController(), hasFlash = true) {
        }
    }
}