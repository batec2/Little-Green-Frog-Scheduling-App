package com.example.f23hopper.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

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
    //unused atm
    val confirmButtonColor: Color = MaterialTheme.colorScheme.tertiaryContainer
    val dismissButtonColor: Color = MaterialTheme.colorScheme.tertiaryContainer
    val confirmTextColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
    val dismissTextColor: Color = MaterialTheme.colorScheme.onTertiaryContainer
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            content = {
                Surface(
                    shape = RoundedCornerShape(15.dp),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(title, style = headerSize)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(message)
                        Spacer(modifier = Modifier.height(32.dp))
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                onClick = onDismiss,
                                shape = RoundedCornerShape(50),
//                                colors = ButtonDefaults.buttonColors(containerColor = dismissButtonColor)
                            ) {
                                Text(dismissButtonText /*color = dismissTextColor*/)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(
                                onClick = onConfirm,
                                shape = RoundedCornerShape(50),
//                                colors = ButtonDefaults.buttonColors(containerColor = confirmButtonColor)
                            ) {
                                Text(confirmButtonText /*color = confirmTextColor*/)
                            }
                        }
                    }
                }
            }
        )
    }
}
