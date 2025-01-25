package com.example.screenvideo.ui

import android.app.Activity.RESULT_OK
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat.getSystemService
import com.example.screenvideo.ui.theme.ScreenVideoTheme


class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ScreenVideoTheme {
                ScreenRecordApp(


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