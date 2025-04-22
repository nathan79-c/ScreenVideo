package com.example.screenvideo


import android.app.Activity.RESULT_OK
import android.app.Service
import android.content.ComponentName
import android.content.ContentValues
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjection.Callback
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream


class ScreenCaptureService:Service(){
    val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
    var mediaProjection: MediaProjection?= null
    private var virtualDisplay: VirtualDisplay? = null
    val displayMetrics = resources.displayMetrics
    private var callback: OnMediaProjectionResult? = null
    private val mediaRecorder by lazy {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(applicationContext)
        } else {
            MediaRecorder()
        }
    }


    override fun onBind(p0: Intent?): IBinder? {
        return null

    }
    override fun startService(intent: Intent?): ComponentName? {
        super.startService(intent)

        return TODO("Provide the return value")
    }


    // Fonction pour démarrer la capture
    fun startScreenCapture(callback: OnMediaProjectionResult) {
        this.callback = callback

        // Nécessite une Activity pour lancer la permission
        val intent = mediaProjectionManager.createScreenCaptureIntent().apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Obligatoire depuis un Service
        }

        startActivity(intent)
    }


    // À appeler depuis l'Activity qui gère le résultat
    fun handleActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && data != null) {
            mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
            callback?.onMediaProjectionGranted(mediaProjection!!)
        } else {
            callback?.onFailure("Permission refusée ou données manquantes")
        }
    }
    override fun onDestroy() {
        mediaProjection?.stop()
        super.onDestroy()
    }
    private val mediaProjectionCallback = object : MediaProjection.Callback() {
        override fun onStop() {
            super.onStop()
            releaseResources()
            stopService()
            saveToGallery()
        }
    }
    private fun stopService() {
        _isServiceRunning.value = false
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    private fun createVirtualDisplay(): VirtualDisplay? {
       // val width =  displayMetrics.widthPixels; val height =  displayMetrics.heightPixels

        return mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder.surface,
            null,
           null
        )
    }



    private fun releaseResources() {
        mediaRecorder.release()
        virtualDisplay?.release()
        mediaProjection?.unregisterCallback(callback as Callback)
        mediaProjection = null
    }
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val outputFile by lazy {
        File(cacheDir, "tmp.mp4")
    }
    private fun saveToGallery() {
        serviceScope.launch {
            val contentValues = ContentValues().apply {
                put(MediaStore.Video.Media.DISPLAY_NAME, "video_${System.currentTimeMillis()}.mp4")
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/Recordings2")
            }
            val videoCollection = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }

            contentResolver.insert(videoCollection, contentValues)?.let { uri ->
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    FileInputStream(outputFile).use { inputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
            }
        }
    }
    companion object {
        private val _isServiceRunning = MutableStateFlow(false)
        val isServiceRunning = _isServiceRunning.asStateFlow()

        private const val VIDEO_FRAME_RATE = 30
        private const val VIDEO_BIT_RATE_KILOBITS = 512

        const val START_RECORDING = "START_RECORDING"
        const val STOP_RECORDING = "STOP_RECORDING"
        const val KEY_RECORDING_CONFIG = "KEY_RECORDING_CONFIG"
    }

}


// Callback pour le résultat de la permission
interface OnMediaProjectionResult {
    fun onMediaProjectionGranted(mediaProjection: MediaProjection)
    fun onFailure(error: String)
}