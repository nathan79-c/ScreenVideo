package com.example.screenvideo.ui.learnScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ColorPicker(color: MutableState<Color>) {
    val red = color.value.red
    val green = color.value.green
    val blue = color.value.blue
    Column {Slider(
        value = red,
        onValueChange = { color.value = Color(it, green,
            blue)
        })
        Slider(
            value = green,
            onValueChange = { color.value = Color(red, it, blue) })
        Slider(
            value = blue,
            onValueChange = { color.value = Color(red, green, it)
            })
    }
}

@Preview
@Composable
fun ViewPreview(){
    val color = remember { mutableStateOf(Color.Red) }
    ColorPicker(color)
}