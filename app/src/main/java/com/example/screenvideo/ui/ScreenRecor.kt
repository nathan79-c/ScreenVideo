package com.example.screenvideo.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenRecordApp(onStartRecording: () -> Unit, onStopRecording: () -> Unit) {
    Scaffold{
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onStartRecording, modifier = Modifier.fillMaxWidth()) {
                Text("Start Screen Recording")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onStopRecording, modifier = Modifier.fillMaxWidth()) {
                Text("Stop Screen Recording")
            }
        }
    }
}