package com.primex.accomplice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.primex.accomplice.ui.theme.AccompliceTheme
import com.primex.ui.SliderPreference

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