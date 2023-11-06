package com.example.f23hopper.ui.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import com.example.compose.CustomColor

enum class ShiftViewColors {
    COLOR1,
    COLOR2,
    COLOR3,
    COLOR4,
    COLOR5,
    COLOR6
    ;

    val color: Color
    @Composable
    @ReadOnlyComposable
    get() = when(this) {
        COLOR1 -> CustomColor.shiftView1
        COLOR2 -> CustomColor.shiftView2
        COLOR3 -> CustomColor.shiftView3
        COLOR4 -> CustomColor.shiftView4
        COLOR5 -> CustomColor.shiftView5
        COLOR6 -> CustomColor.shiftView6
    }
}