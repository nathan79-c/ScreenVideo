package com.example.screenvideo.ui

import android.content.Intent
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.screenvideo.ui.theme.ScreenVideoTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenVideoTheme {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    ScreenRecordApp(
                        onStartService = {  },
                        onStopService = {  }
                    )
                    val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
                    var mediaProjection : MediaProjection

                    val startMediaProjection = registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult()
                    ) { result ->
                        if (result.resultCode == RESULT_OK) {
                            mediaProjection = mediaProjectionManager
                                .getMediaProjection(result.resultCode, result.data!!)
                        }
                    }

                    startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())
                }
                val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
                var mediaProjection : MediaProjection

                val startMediaProjection = registerForActivityResult(
                    StartActivityForResult()
                ) { result ->
                    if (result.resultCode == RESULT_OK) {
                        mediaProjection = mediaProjectionManager
                            .getMediaProjection(result.resultCode, result.data!!)
                    }
                }

                startMediaProjection.launch(mediaProjectionManager.createScreenCaptureIntent())



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