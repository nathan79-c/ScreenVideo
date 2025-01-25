package com.example.screenvideo.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.screenvideo.data.ScreenRecorderManager
import com.example.screenvideo.ui.theme.ScreenVideoTheme

class MainActivity : ComponentActivity() {

    private lateinit var screenRecorderManager: ScreenRecorderManager
    private lateinit var screenCaptureIntentLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        screenRecorderManager = ScreenRecorderManager(this)

        // Register a launcher to get MediaProjection permission
        screenCaptureIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                screenRecorderManager.startRecording(result.resultCode, result.data!!)
            }
        }

        setContent {
            ScreenVideoTheme {
                ScreenRecordApp(
                    onStartRecording = {
                        val projectionIntent = screenRecorderManager.getProjectionIntent()
                        screenCaptureIntentLauncher.launch(projectionIntent)
                    },
                    onStopRecording = {
                        screenRecorderManager.stopRecording()
                    }
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ScreenVideoTheme {

    }
}