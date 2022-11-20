package com.primex.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import com.primex.preferences.IntSaver
import com.primex.preferences.Preferences
import com.primex.preferences.get
import com.primex.preferences.intPreferenceKey
import com.primex.sample.ui.theme.SampleTheme
import com.primex.ui.Button
import com.primex.ui.Preference
import com.primex.ui.SliderPreference


private val KEY_COUNTER =
    intPreferenceKey("counter3", defaultValue = true, saver = object : IntSaver<Boolean> {
        override fun save(value: Boolean): Int = if (value) 1 else 0

        override fun restore(value: Int): Boolean = value != 0
    })

class MainActivity : ComponentActivity() {

    private lateinit var preferences: Preferences

    @OptIn(ExperimentalComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = Preferences(this)
        setContent {
            SampleTheme {
                Box(modifier = Modifier.fillMaxSize()) {

                    val counter by preferences[KEY_COUNTER]

                    Button(label = "$counter", onClick = {
                        preferences[KEY_COUNTER] = !counter
                    })

                }
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
@Preview(showBackground = true, backgroundColor = 0xffe0e0e1/* widthDp = 360, heightDp = 720*/)
fun DefaultPreview() {

    SampleTheme {
        PreviewPerf()
    }
}


@Composable
private fun PreviewPerf() {
    Column {

        SliderPreference(
            title = AnnotatedString("Color Secondary"),
            icon = Icons.Default.AddCircle,
            summery = AnnotatedString("Select your favourite secondary color."),
            defaultValue = 0.2f,
            onValueChange = {},
            steps = 5,
            iconChange = Icons.Default.Favorite
        )

        Preference(
            title = AnnotatedString("Color Secondary"),
            summery = AnnotatedString("Select your favourite secondary color."),
        )

    }
}
