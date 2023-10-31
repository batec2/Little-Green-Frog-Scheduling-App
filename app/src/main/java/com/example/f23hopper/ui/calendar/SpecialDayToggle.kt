package com.example.f23hopper.ui.calendar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ToggleSpecialDayButton(
    toggleSpecialDay: suspend () -> Unit,
    isSpecialDay: Boolean,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    IconButton(
        modifier = modifier,
        onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                toggleSpecialDay()
            }
        }) {
        if (isSpecialDay) {
            Icon(imageVector = Icons.Outlined.Favorite, contentDescription = "Special Day")
        } else {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = "Not Special Day"
            )
        }
    }
}
