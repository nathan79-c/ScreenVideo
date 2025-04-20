package com.example.screenvideo


import android.app.Activity.RESULT_OK
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Handler
import android.os.IBinder
import android.view.Surface
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult

class ScreenCaptureService:Service(){
    val mediaProjectionManager = getSystemService(MediaProjectionManager::class.java)
    var mediaProjection: MediaProjection?= null
    private var virtualDisplay: VirtualDisplay? = null
    val displayMetrics = resources.displayMetrics
    private lateinit var mediaRecorder: MediaRecorder


    private var callback: OnMediaProjectionResult? = null


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
    private fun createVirtualDisplay(): VirtualDisplay? {
      //  val width =  displayMetrics.widthPixels; val height =  displayMetrics.heightPixels

        return mediaProjection?.createVirtualDisplay(
            "Screen",
            displayMetrics.widthPixels,
            displayMetrics.heightPixels,
            resources.displayMetrics.densityDpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder.surface,
            null,
            null
        )
    }


}


// Callback pour le résultat de la permission
interface OnMediaProjectionResult {
    fun onMediaProjectionGranted(mediaProjection: MediaProjection)
    fun onFailure(error: String)
}