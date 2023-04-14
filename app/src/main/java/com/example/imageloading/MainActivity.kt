package com.example.imageloading

import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.modifier.modifierLocalConsumer
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
    var category by remember { mutableStateOf("Dog") }
    var showDialog by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(modifier = Modifier.padding(30.dp)) {
            ImageInput("Width", width) {
                width = it
            }
            Spacer(modifier = Modifier.padding(6.dp))
            ImageInput("Height", height) {
                height = it
            }
            Spacer(modifier = Modifier.padding(10.dp))
            CategoryInput("Category"){
                category = it
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (width > 1000 || height > 1000) {
                    showDialog = true
                } else {
                    isLoading = true
                    imageUrl = "https://loremflickr.com/$width/$height/$category"
                    //imageUrl = "https://api.lorem.space/image/movie?w=$width&h=$height"
                }
            }
        ) {
            Text("Load Image")
        }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Image Too Large") },
                text = { Text("The image you are trying to load is too large. Please select an image with a width and height of 1000 pixels or less.") },
                confirmButton = {
                    Button(
                        onClick = { showDialog = false },
                    ) {
                        Text("OK")
                    }
                }
            )
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
    var error by remember { mutableStateOf<String?>(null) }
    Column {
        Text(label)
        //Spacer(modifier = Modifier.padding(16.dp))
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategoryInput(label: String,onItemSelected: (String) -> Unit) {
    val listItems = arrayOf("Dog","Movie","Game","Album","Book","Face","Fashion","Watch","Furniture", "Cat", "Shoe", "Paris")
    val contextForToast = LocalContext.current.applicationContext

    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(listItems[0]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        //Spacer(modifier = Modifier.height(16.dp))
        TextField(

            value = selectedItem,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = "Category") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listItems.forEach { item ->
                DropdownMenuItem(onClick = {
                    selectedItem = item
                    expanded = false
                    onItemSelected(item) // call the callback function
                }) {
                    Text(text = item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ImageLoadingTheme {
        ImageLoader()
    }
}