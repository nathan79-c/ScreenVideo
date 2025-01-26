package com.example.screenvideo

import android.app.Activity.RESULT_OK
import android.app.Service
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import android.content.Intent
import android.graphics.PixelFormat
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaMuxer
import android.os.Environment
import android.os.IBinder
import android.util.DisplayMetrics
import android.util.Log
import android.view.Surface
import android.media.MediaFormat
import java.io.File
import java.io.IOException

class ScreenCaptureService : Service() {

    private var mediaProjectionManager: MediaProjectionManager? = null
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var surface: Surface? = null
    private var mediaCodec: MediaCodec? = null
    private var mediaMuxer: MediaMuxer? = null

    private val TAG = "ScreenCaptureService"

    override fun onCreate() {
        super.onCreate()
        mediaProjectionManager = ContextCompat.getSystemService(this, MediaProjectionManager::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startScreenCapture()
        return START_NOT_STICKY
    }

    private fun startScreenCapture() {
        val captureIntent = mediaProjectionManager?.createScreenCaptureIntent()
        startActivityForResult(captureIntent)
    }

    private fun startActivityForResult(intent: Intent?) {
        // This requires an Activity context for ActivityResult APIs. Use a PendingIntent or manage activity transitions here.
        Log.d(TAG, "You need to implement ActivityResult handling for screen capture.")
    }

    private fun initMediaProjection(resultCode: Int, data: Intent?) {
        mediaProjection = mediaProjectionManager?.getMediaProjection(resultCode, data!!)
        mediaProjection?.let {
            setupVirtualDisplay()
        }
    }

    private fun setupVirtualDisplay() {
        val metrics = DisplayMetrics()
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        val density = metrics.densityDpi

        try {
            // Create the Surface for VirtualDisplay
            val surface = setupMediaCodec(width, height)
            virtualDisplay = mediaProjection?.createVirtualDisplay(
                "ScreenCapture",
                width,
                height,
                density,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                surface,
                null,
                null
            )
        } catch (e: IOException) {
            Log.e(TAG, "Error setting up virtual display: ${e.message}")
        }
    }

    private fun setupMediaCodec(width: Int, height: Int): Surface {
        val outputFile = File(
            getExternalFilesDir(Environment.DIRECTORY_MOVIES),
            "screencapture_${System.currentTimeMillis()}.mp4"
        )

        mediaCodec = MediaCodec.createEncoderByType("video/avc").apply {
            // Configure codec
            val format = MediaFormat.createVideoFormat("video/avc", width, height)
            format.setInteger(MediaFormat.KEY_BIT_RATE, 6000000) // Débit binaire
            format.setInteger(MediaFormat.KEY_FRAME_RATE, 30)    // Taux de rafraîchissement
            format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface)
            format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1) // Intervalle des images clés

            configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            surface = createInputSurface()
            start()
        }

        mediaMuxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        return surface!!
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        virtualDisplay?.release()
        mediaProjection?.stop()
        mediaCodec?.stop()
        mediaCodec?.release()
        mediaMuxer?.stop()
        mediaMuxer?.release()
    }
}
