package com.example.androidassignment2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidassignment2.ui.theme.AndroidAssignment2Theme
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidAssignment2Theme {
                var legoText by remember { mutableStateOf("") }
                var gwsText by remember { mutableStateOf("") }
                var volvoText by remember { mutableStateOf("") }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val lego = R.drawable.lego
                        LoadImageFromResource(imageResourceId = lego)

                        Text(text = "Text: $legoText")

                        Button(onClick = {
                            runTextRecognition(lego) {
                                legoText = it
                            }
                        }) {
                            Text("Process")
                        }

                        val gws = R.drawable.gws
                        LoadImageFromResource(imageResourceId = gws)

                        Text(text = "Text: $gwsText")

                        Button(onClick = {
                            runTextRecognition(gws) {
                                gwsText = it
                            }
                        }) {
                            Text("Process")
                        }

                        val volvo = R.drawable.volvo
                        LoadImageFromResource(imageResourceId = volvo)

                        Text(text = "Text: $volvoText")

                        Button(onClick = {
                            runTextRecognition(volvo) {
                                volvoText = it
                            }
                        }) {
                            Text("Process")
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LoadImageFromResource(imageResourceId: Int) {
        val painter = painterResource(id = imageResourceId)

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(16.dp)
        )
    }

    private fun runTextRecognition(image: Int, callback: (String) -> Unit) {

        var selectedImage = BitmapFactory.decodeResource(resources, image)

        val image = InputImage.fromBitmap(selectedImage, 0)
        var textRecognizerOptions = TextRecognizerOptions.Builder().build()
        val recognizer = TextRecognition.getClient(textRecognizerOptions)
        recognizer.process(image)
            .addOnSuccessListener { texts ->
                processTextRecognitionResult(texts, callback)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text, callback: (String) -> Unit) {
        val blocks: List<Text.TextBlock> = texts.getTextBlocks()
        if (blocks.size == 0) {
            return
        }

        for (i in blocks.indices) {
            val lines: List<Text.Line> = blocks[i].getLines()
            for (j in lines.indices) {
                val elements: List<Text.Element> = lines[j].getElements()

                for (k in elements.indices) {

                    Log.i("MLKITDEBUG",elements[k].text + " " + elements[k].confidence.toString())

                    callback(elements[k].text)

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidAssignment2Theme {
    }
}