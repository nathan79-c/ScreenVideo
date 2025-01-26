package com.example.screenvideo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.screenvideo.ScreenCaptureService
import com.example.screenvideo.ui.theme.ScreenVideoTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenVideoTheme {
                ScreenRecordApp(
                    onStartService = { startScreenCaptureService() },
                    onStopService = { stopScreenCaptureService() }
                )
            }
        }
    }

    private fun startScreenCaptureService() {
        val intent = Intent(this, ScreenCaptureService::class.java)
        startService(intent)
    }

    private fun stopScreenCaptureService() {
        val intent = Intent(this, ScreenCaptureService::class.java)
        stopService(intent)
    }
}









@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScreenVideoTheme {

    }
}