package com.example.imageloading

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.imageloading.ui.theme.ImageLoadingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageLoadingTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ImageLoader()
                }
            }
        }
    }
}

@Composable
fun ImageLoader() {
    var width by remember { mutableStateOf(150) }
    var height by remember { mutableStateOf(220) }
    var isLoading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            ImageInput("Width", width) {
                width = it
            }
            Spacer(modifier = Modifier.width(16.dp))
            ImageInput("Height", height) {
                height = it
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                imageUrl = "https://api.lorem.space/image/movie?w=$width&h=$height"
            }
        ) {
            Text("Load Image")
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (isLoading) {
            DisplayLoading(imageUrl)
        } else if (imageUrl.isNotBlank()) {
            DisplayImage(imageUrl)
        }
    }
}

@Composable
fun ImageInput(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column {
        Text(label)
        var textValue by remember { mutableStateOf(TextFieldValue(value.toString())) }
        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
                val intValue = it.text.toIntOrNull() ?: value
                onValueChange(intValue)
            }
        )
    }
}

@Composable
fun DisplayImage(src: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(src)
            .crossfade(true)
            .build(),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
    )
}

@Composable
fun DisplayLoading(src: String) {
    Text("Loading...")
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(src)
            .crossfade(true)
            .build(),
        contentDescription = "",
        //contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(CircleShape)
            .size(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageLoadingTheme {
        ImageLoader()
    }
}