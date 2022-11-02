package com.primex.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenu
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.primex.core.rememberState
import com.primex.sample.ui.theme.SampleTheme
import com.primex.ui.Label
import com.primex.ui.Preference
import com.primex.ui.SliderPreference
import com.primex.ui.dialog.BottomSheetDialog

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SampleTheme {
                var state by rememberState(initial = false)
                Box(modifier = Modifier.fillMaxSize()) {

                    Button(onClick = { state = !state }) {
                        Label(text = "Click Me")
                    }

                    BottomSheetDialog(
                        expanded = state,
                        onDismissRequest = { state = false }
                    ) {
                        Column {
                            PreviewPerf()
                            PreviewPerf()
                            PreviewPerf()
                        }
                    }
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
