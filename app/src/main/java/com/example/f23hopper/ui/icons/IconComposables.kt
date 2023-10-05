package com.example.f23hopper.ui.icons

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.example.f23hopper.ui.theme.PurpleGrey40

@Composable
fun rememberSunny(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "sunny",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(PurpleGrey40),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 7.958f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.916f)
                verticalLineTo(3.042f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.396f, 0.917f, -0.396f)
                reflectiveQuadToRelative(0.938f, 0.396f)
                quadToRelative(0.395f, 0.396f, 0.395f, 0.938f)
                verticalLineToRelative(3.625f)
                quadToRelative(0f, 0.541f, -0.395f, 0.916f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveToRelative(8.5f, 3.542f)
                quadToRelative(-0.417f, -0.375f, -0.396f, -0.917f)
                quadToRelative(0.021f, -0.541f, 0.396f, -0.916f)
                lineToRelative(2.542f, -2.584f)
                quadToRelative(0.375f, -0.375f, 0.916f, -0.375f)
                quadToRelative(0.542f, 0f, 0.959f, 0.375f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.917f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                lineTo(30.333f, 11.5f)
                quadToRelative(-0.375f, 0.375f, -0.916f, 0.375f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                close()
                moveToRelative(4.833f, 9.792f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadTo(32.042f, 20f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.916f, -0.395f)
                horizontalLineToRelative(3.625f)
                quadToRelative(0.542f, 0f, 0.938f, 0.395f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.938f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.938f, 0.375f)
                close()
                moveTo(20f, 38.25f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.917f)
                verticalLineToRelative(-3.625f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadTo(20f, 32f)
                quadToRelative(0.542f, 0f, 0.938f, 0.396f)
                quadToRelative(0.395f, 0.396f, 0.395f, 0.937f)
                verticalLineToRelative(3.625f)
                quadToRelative(0f, 0.542f, -0.395f, 0.917f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveTo(9.667f, 11.5f)
                lineTo(7.083f, 8.958f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.916f)
                quadToRelative(0f, -0.542f, 0.417f, -0.959f)
                quadToRelative(0.375f, -0.375f, 0.896f, -0.375f)
                reflectiveQuadToRelative(0.896f, 0.375f)
                lineTo(11.5f, 9.667f)
                quadToRelative(0.375f, 0.375f, 0.396f, 0.916f)
                quadToRelative(0.021f, 0.542f, -0.396f, 0.917f)
                quadToRelative(-0.417f, 0.375f, -0.938f, 0.375f)
                quadToRelative(-0.52f, 0f, -0.895f, -0.375f)
                close()
                moveToRelative(21.375f, 21.417f)
                lineTo(28.5f, 30.333f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.916f)
                quadToRelative(0f, -0.542f, 0.375f, -0.917f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                quadToRelative(0.541f, 0f, 0.916f, 0.375f)
                lineToRelative(2.625f, 2.542f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.916f)
                quadToRelative(0f, 0.542f, -0.416f, 0.959f)
                quadToRelative(-0.375f, 0.375f, -0.917f, 0.395f)
                quadToRelative(-0.542f, 0.021f, -0.958f, -0.395f)
                close()
                moveToRelative(-28f, -11.625f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadTo(1.75f, 20f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.917f, -0.395f)
                horizontalLineToRelative(3.625f)
                quadToRelative(0.541f, 0f, 0.937f, 0.395f)
                quadTo(8f, 19.458f, 8f, 20f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveToRelative(4.041f, 11.625f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.917f)
                reflectiveQuadToRelative(0.375f, -0.917f)
                lineTo(9.667f, 28.5f)
                quadToRelative(0.375f, -0.375f, 0.916f, -0.375f)
                quadToRelative(0.542f, 0f, 0.917f, 0.375f)
                quadToRelative(0.375f, 0.417f, 0.375f, 0.938f)
                quadToRelative(0f, 0.52f, -0.375f, 0.937f)
                lineToRelative(-2.542f, 2.542f)
                quadToRelative(-0.375f, 0.375f, -0.937f, 0.375f)
                quadToRelative(-0.563f, 0f, -0.938f, -0.375f)
                close()
                moveToRelative(12.917f, -3f)
                quadToRelative(-4.125f, 0f, -7.021f, -2.896f)
                reflectiveQuadTo(10.083f, 20f)
                quadToRelative(0f, -4.125f, 2.896f, -7.042f)
                quadToRelative(2.896f, -2.916f, 7.021f, -2.916f)
                reflectiveQuadToRelative(7.042f, 2.916f)
                quadToRelative(2.916f, 2.917f, 2.916f, 7.042f)
                reflectiveQuadToRelative(-2.916f, 7.021f)
                quadTo(24.125f, 29.917f, 20f, 29.917f)
                close()
                moveToRelative(0f, -2.625f)
                quadToRelative(3.042f, 0f, 5.167f, -2.125f)
                reflectiveQuadTo(27.292f, 20f)
                quadToRelative(0f, -3.042f, -2.125f, -5.167f)
                reflectiveQuadTo(20f, 12.708f)
                quadToRelative(-3.042f, 0f, -5.167f, 2.125f)
                reflectiveQuadTo(12.708f, 20f)
                quadToRelative(0f, 3.042f, 2.125f, 5.167f)
                reflectiveQuadTo(20f, 27.292f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberClearNight(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "clear_night",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(PurpleGrey40),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 33.625f)
                quadToRelative(2.25f, 0f, 4.562f, -0.854f)
                quadToRelative(2.313f, -0.854f, 4.396f, -2.563f)
                quadToRelative(-3.916f, -1.375f, -6.77f, -3.75f)
                quadToRelative(-2.855f, -2.375f, -4.563f, -5.458f)
                quadToRelative(-1.708f, -3.083f, -2.208f, -6.708f)
                quadToRelative(-0.5f, -3.625f, 0.25f, -7.542f)
                quadToRelative(-4.292f, 1.667f, -6.875f, 5.167f)
                quadToRelative(-2.584f, 3.5f, -2.584f, 8.083f)
                quadToRelative(0f, 5.875f, 3.959f, 9.75f)
                quadToRelative(3.958f, 3.875f, 9.833f, 3.875f)
                close()
                moveToRelative(0f, 2.625f)
                quadToRelative(-3.5f, 0f, -6.5f, -1.229f)
                reflectiveQuadToRelative(-5.208f, -3.417f)
                quadToRelative(-2.209f, -2.187f, -3.48f, -5.146f)
                quadTo(3.542f, 23.5f, 3.542f, 20f)
                quadToRelative(0f, -6.083f, 3.854f, -10.625f)
                reflectiveQuadToRelative(9.479f, -5.625f)
                quadToRelative(1.083f, -0.208f, 1.604f, 0.479f)
                quadToRelative(0.521f, 0.688f, 0.146f, 1.854f)
                quadToRelative(-1.125f, 3.5f, -0.75f, 7.084f)
                quadToRelative(0.375f, 3.583f, 2.063f, 6.645f)
                quadToRelative(1.687f, 3.063f, 4.562f, 5.271f)
                quadToRelative(2.875f, 2.209f, 6.75f, 2.917f)
                quadToRelative(1.167f, 0.25f, 1.479f, 1f)
                quadToRelative(0.313f, 0.75f, -0.396f, 1.542f)
                quadToRelative(-2.291f, 2.583f, -5.541f, 4.146f)
                quadTo(23.542f, 36.25f, 20f, 36.25f)
                close()
            }
        }.build()
    }
}