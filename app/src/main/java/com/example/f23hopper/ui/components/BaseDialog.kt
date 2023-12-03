package com.example.f23hopper.ui.components


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    showDialog: Boolean = true,
    confirmButtonText: String = "Yes",
    dismissButtonText: String = "No",
    headerSize: TextStyle = MaterialTheme.typography.headlineMedium
) {
    if (showDialog) {
        AlertDialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f),
            onDismissRequest = onDismiss,
            content = {
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = MaterialTheme.colorScheme.surface,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Bold title
                        Text(title, style = headerSize.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                    RoundedCornerShape(10.dp)
                                )
                                .padding(4.dp)
                                .defaultMinSize(minHeight = 200.dp)

                        ) {
                            LazyColumn {
                                item {
                                    Text(
                                        message,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(dismissButtonText)
                            }
                            Spacer(modifier = Modifier.width(38.dp))
                            Button(
                                onClick = onConfirm,
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(confirmButtonText)
                            }
                        }
                    }
                }
            }
        )
    }
}
