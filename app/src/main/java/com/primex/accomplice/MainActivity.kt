package com.primex.accomplice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.primex.accomplice.ui.theme.AccompliceTheme
import com.primex.foundation.shadow.LightSource
import com.primex.foundation.shadow.RoundedCornerOutline
import com.primex.foundation.shadow.shadow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccompliceTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFf2f2f2)
                ) {
                    Box(
                        modifier = Modifier
                            .requiredSize(100.dp)
                            .shadow(
                                outline = RoundedCornerOutline(12.dp),
                                light = Color.White,
                                dark = Color.Black.copy(0.1f),
                                elevation = 10.dp,
                                source = LightSource.TOP_LEFT,
                                intensity = 0.1f
                            )
                            .background(color = Color(0xFFf2f2f2))
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AccompliceTheme {
        Greeting("Android")
    }
}