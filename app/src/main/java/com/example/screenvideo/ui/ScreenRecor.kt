package com.example.screenvideo.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScreenRecordApp(initialIsPlaying: Boolean = false) {
    var isPlaying by remember { mutableStateOf(initialIsPlaying) }

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(12.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            // Bouton Play/Pause avec Material Icons
            IconButton(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .size(72.dp)
                    .background(Color(0xFFE0F7FA), shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) android.R.drawable.ic_media_pause
                        else android.R.drawable.ic_media_play
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = Color.Green,
                    modifier = Modifier.size(40.dp)
                )
            }

            // Bouton Sauvegarder avec Material Icons
            IconButton(
                onClick = { /* Action de sauvegarde */ },
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFFFFEB3B), shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Sauvegarder",
                    tint = Color.Green,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true, name = "Play State")
@Composable
fun PreviewPlay() {
    ScreenRecordApp(initialIsPlaying = false)
}

@Preview(showBackground = true, name = "Pause State")
@Composable
fun PreviewPause() {
    ScreenRecordApp(initialIsPlaying =true )
}

@Preview(showBackground = true, name = "Full Preview")
@Composable
fun FullPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ScreenRecordApp()
    }
}