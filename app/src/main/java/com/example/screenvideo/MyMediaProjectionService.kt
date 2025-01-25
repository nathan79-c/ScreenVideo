package com.example.screenvideo

import android.app.Activity.RESULT_OK
import android.app.Service
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MyMediaProjectionService :Service() {
    val mediaProjectionManager = ContextCompat.getSystemService(MediaProjectionManager::class.java)
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
