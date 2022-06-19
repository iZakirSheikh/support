package com.primex.accomplice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.primex.accomplice.ui.theme.AccompliceTheme
import com.primex.core.rememberState
import com.primex.core.shadow.SpotLight
import com.primex.core.shadow.shadow
import com.primex.ui.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccompliceTheme {

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

val bg = Color(0xffe0e0e0)

@Composable
@Preview(showBackground = true, backgroundColor = 0xffe0e0e0/* widthDp = 360, heightDp = 720*/)
fun DefaultPreview() {

    AccompliceTheme {
        PreviewPerf()
    }
}


@Composable
private fun PreviewPerf() {

    SliderPreference(
        title = "Color Secondary",
        icon = Icons.Default.AddCircle,
        summery = "Select your favourite secondary color.",
        defaultValue = 0.2f,
        onValueChange = {},
        steps = 5,
        iconChange = Icons.Default.Favorite
    )
}