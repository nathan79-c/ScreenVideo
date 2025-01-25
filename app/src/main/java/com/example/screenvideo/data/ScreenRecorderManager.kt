package com.example.screenvideo.data

import android.content.Context
import android.content.Intent
import android.hardware.display.VirtualDisplay
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjection.Callback
import android.media.projection.MediaProjectionManager
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import java.io.File

class ScreenRecorderManager(private val context: Context) {

    private val mediaProjectionManager: MediaProjectionManager =
        context.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var mediaRecorder: MediaRecorder? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var outputFilePath: String = ""

    fun getProjectionIntent(): Intent {
        return mediaProjectionManager.createScreenCaptureIntent()
    }

    fun startRecording(resultCode: Int, data: Intent) {
        mediaProjection = (mediaProjectionManager.getMediaProjection(resultCode, data)?.apply {
            try {
                setupMediaRecorder()
                val metrics = getScreenMetrics()

                virtualDisplay = createVirtualDisplay(
                    "ScreenRecorder",
                    metrics.widthPixels,
                    metrics.heightPixels,
                    metrics.densityDpi
                )

                mediaRecorder?.start()
                Toast.makeText(context, "Recording started!", Toast.LENGTH_SHORT).show()

                registerCallback(object : Callback() {
                    override fun onStop() {
                        stopRecording()
                    }
                }, Handler(Looper.getMainLooper()))
            } catch (e: Exception) {
                Log.e("ScreenRecorder", "Failed to start recording: ${e.message}")
                stopRecording()
            }
        } ?: run {
            Toast.makeText(context, "Failed to initialize MediaProjection", Toast.LENGTH_SHORT).show()
        }) as MediaProjection?
    }

    fun stopRecording() {
        try {
            virtualDisplay?.release()
            mediaProjection?.stop()
            mediaRecorder?.apply {
                stop()
                reset()
                release()
            }
            mediaRecorder = null
            Toast.makeText(context, "Recording stopped! Saved at: $outputFilePath", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("ScreenRecorder", "Failed to stop recording: ${e.message}")
        }
    }

    private fun setupMediaRecorder() {
        val videoFile = File(
            context.getExternalFilesDir(Environment.DIRECTORY_MOVIES),
            "screen_record_${System.currentTimeMillis()}.mp4"
        )
        outputFilePath = videoFile.absolutePath

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC) // Optional: record microphone audio
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(outputFilePath)
            setVideoEncoder(MediaRecorder.VideoEncoder.H264)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setVideoSize(getScreenMetrics().widthPixels, getScreenMetrics().heightPixels)
            setVideoFrameRate(30) // 30 FPS
            setVideoEncodingBitRate(5 * 1024 * 1024) // 5 Mbps
            prepare()
        }
    }

    private fun getScreenMetrics(): DisplayMetrics {
        return context.resources.displayMetrics
    }

    private fun createVirtualDisplay(
        name: String,
        width: Int,
        height: Int,
        dpi: Int
    ): VirtualDisplay {
        return mediaProjection!!.createVirtualDisplay(
            name,
            width,
            height,
            dpi,
            0,
            mediaRecorder!!.surface,
            null,
            null
        )
    }
}