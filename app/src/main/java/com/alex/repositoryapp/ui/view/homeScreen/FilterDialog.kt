package com.alex.repositoryapp.ui.view.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FilterDialog(onDismiss: () -> Unit, onFilterSelected: (String?) -> Unit) {
    val languages = listOf("All", "Kotlin", "Java", "Python", "JavaScript")
    var selectedLanguage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Filter by Language") },
        text = {
            Column {
                languages.forEach { language ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                selectedLanguage = if (language == "All") null else language
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLanguage == language || (selectedLanguage == null && language == "All"),
                            onClick = {
                                selectedLanguage = if (language == "All") null else language
                            }
                        )
                        Text(text = language, modifier = Modifier.padding(start = 8.dp))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onFilterSelected(selectedLanguage)
                    onDismiss()
                }
            ) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}