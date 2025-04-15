package com.example.screenvideo


import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.os.IBinder

class ScreenCaptureService:Service(){
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    val displayMetrics = resources.displayMetrics
    private lateinit var mediaRecorder: MediaRecorder

    override fun onBind(p0: Intent?): IBinder? {
        return null

    }
    override fun startService(intent: Intent?): ComponentName? {
        super.startService(intent)

        return TODO("Provide the return value")
    }

}


