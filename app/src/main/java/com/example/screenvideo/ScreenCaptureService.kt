package com.example.screenvideo


import android.hardware.display.VirtualDisplay
import android.util.DisplayMetrics
import android.view.Surface
import android.os.Binder
import android.view.WindowManager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.IBinder
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService

class ScreenCaptureService:Service(){
    override fun onBind(p0: Intent?): IBinder? {
        return null

    }


}


