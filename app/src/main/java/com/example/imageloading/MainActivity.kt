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
    var width by remember { mutableStateOf(320) }
    var height by remember { mutableStateOf(240) }
    var isLoading by remember { mutableStateOf(false) }
    var imageUrl by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("paris") }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            ImageInput("Width", width) {
                width = it
            }
            Spacer(modifier = Modifier.width(16.dp))
            ImageInput("Height", height) {
                height = it
            }
            Spacer(modifier = Modifier.width(16.dp))
            CategoryInput(category) {
                category = it
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                isLoading = true
                imageUrl = "https://loremflickr.com/$width/$height/$category"
                //imageUrl = "https://api.lorem.space/image/movie?w=$width&h=$height"
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
fun CategoryInput(category: String, onCategoryChange: (String) -> Unit) {
    Column {
        Text("Category")
        var textValue by remember { mutableStateOf(TextFieldValue(category)) }
        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
                onCategoryChange(it.text)
            }
        )
    }
}

@Composable
fun ImageInput(label: String, value: Int, onValueChange: (Int) -> Unit) {
    var error by remember { mutableStateOf<String?>(null) }
    Column {
        Text(label)
        var textValue by remember { mutableStateOf(TextFieldValue(value.toString())) }
        TextField(
            value = textValue,
            onValueChange = {
                textValue = it
                val intValue = it.text.toIntOrNull()
                if (intValue == null || intValue < 1) {
                    error = "Please enter a valid number greater than zero"
                } else {
                    error = null
                    onValueChange(intValue)
                }
            },
            isError = error != null,
            singleLine = true,
        )
        if (error != null) {
            Text(
                text = error!!,
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.padding(start = 8.dp),
            )
        }
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
            //.clip(CircleShape)
            //.size(200.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageLoadingTheme {
        ImageLoader()
    }
}