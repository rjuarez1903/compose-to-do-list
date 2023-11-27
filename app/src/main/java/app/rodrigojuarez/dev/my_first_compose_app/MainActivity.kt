package app.rodrigojuarez.dev.my_first_compose_app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.rodrigojuarez.dev.my_first_compose_app.ui.theme.MyfirstcomposeappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val prefs = getPreferences(Context.MODE_PRIVATE)
            val savedItems = prefs.getStringSet("items", null) ?: setOf<String>()
            App(savedItems.toList())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(initialList: List<String>) {
    var itemTdoAdd by remember { mutableStateOf("") }
    var collection by remember { mutableStateOf(initialList) }

    MyfirstcomposeappTheme {
        Surface(
            modifier = Modifier
                .background(Color.DarkGray),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Column {
                    TopAppBar(
                        title = { Text("To Do List") },
                        colors = TopAppBarDefaults.smallTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Column(modifier = Modifier.padding(16.dp)) {
                        ItemInput(itemToAdd = itemTdoAdd, onItemAdded = {
                            if (it.isNotBlank()) {
                                collection += it
                                itemTdoAdd = ""
                            }
                        }, onItemChanged = {
                            itemTdoAdd = it
                        })
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }

                if (collection.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "The list is empty",
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    LazyColumn {
                        items(collection.size, itemContent = { index ->
                            ToDoItem(item = collection[index], onItemDeleted = {
                                collection -= it
                            })
                        })
                    }
                }
            }
        }
    }
}


@Composable
fun ToDoItem(item: String, onItemDeleted: (String) -> Unit) {
    var isChecked by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isChecked, onCheckedChange = { isChecked = it })
        Text(text = item)
        Spacer(modifier = Modifier.weight(1F))
        IconButton(onClick = { onItemDeleted(item) }) {
            Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemInput(itemToAdd: String, onItemAdded: (String) -> Unit, onItemChanged: (String) -> Unit) {
    OutlinedTextField(
        value = itemToAdd,
        onValueChange = onItemChanged,
        label = { Text(text = "Item") },
        placeholder = { Text(text = "Please enter your item to add to the list") },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        Button(onClick = { onItemAdded(itemToAdd) }) {
            Text(text = "Add")
        }
    }
}
