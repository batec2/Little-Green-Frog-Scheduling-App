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

@Composable
fun rememberWbSunny(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "wb_sunny",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(20f, 6.5f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.396f)
                reflectiveQuadToRelative(-0.375f, -0.937f)
                verticalLineTo(3.208f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadTo(20f, 1.875f)
                quadToRelative(0.542f, 0f, 0.938f, 0.396f)
                quadToRelative(0.395f, 0.396f, 0.395f, 0.937f)
                verticalLineToRelative(1.959f)
                quadToRelative(0f, 0.541f, -0.395f, 0.937f)
                quadToRelative(-0.396f, 0.396f, -0.938f, 0.396f)
                close()
                moveToRelative(0f, 31.583f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.958f)
                verticalLineToRelative(-1.958f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.396f, 0.917f, -0.396f)
                reflectiveQuadToRelative(0.938f, 0.396f)
                quadToRelative(0.395f, 0.396f, 0.395f, 0.938f)
                verticalLineToRelative(1.958f)
                quadToRelative(0f, 0.583f, -0.395f, 0.958f)
                quadToRelative(-0.396f, 0.375f, -0.938f, 0.375f)
                close()
                moveToRelative(14.792f, -16.791f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                quadToRelative(0f, -0.541f, 0.375f, -0.916f)
                reflectiveQuadToRelative(0.917f, -0.375f)
                horizontalLineToRelative(2f)
                quadToRelative(0.541f, 0f, 0.937f, 0.395f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.938f)
                quadToRelative(0f, 0.542f, -0.396f, 0.917f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveToRelative(-31.584f, 0f)
                quadToRelative(-0.541f, 0f, -0.916f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                quadToRelative(0f, -0.541f, 0.375f, -0.916f)
                reflectiveQuadToRelative(0.916f, -0.375f)
                horizontalLineToRelative(2f)
                quadToRelative(0.542f, 0f, 0.917f, 0.395f)
                quadToRelative(0.375f, 0.396f, 0.375f, 0.938f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.917f, 0.375f)
                close()
                moveToRelative(26.709f, -11.25f)
                quadToRelative(-0.417f, -0.375f, -0.417f, -0.938f)
                quadToRelative(0f, -0.562f, 0.417f, -0.937f)
                lineToRelative(0.875f, -0.917f)
                quadToRelative(0.375f, -0.333f, 0.937f, -0.354f)
                quadToRelative(0.563f, -0.021f, 0.938f, 0.354f)
                quadToRelative(0.416f, 0.417f, 0.416f, 0.979f)
                quadToRelative(0f, 0.563f, -0.416f, 0.938f)
                lineToRelative(-0.875f, 0.916f)
                quadToRelative(-0.417f, 0.375f, -0.938f, 0.375f)
                quadToRelative(-0.521f, 0f, -0.937f, -0.416f)
                close()
                moveTo(7.333f, 32.667f)
                quadToRelative(-0.375f, -0.417f, -0.375f, -0.979f)
                quadToRelative(0f, -0.563f, 0.375f, -0.938f)
                lineToRelative(0.875f, -0.875f)
                quadToRelative(0.375f, -0.333f, 0.938f, -0.354f)
                quadToRelative(0.562f, -0.021f, 0.979f, 0.396f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.916f)
                quadToRelative(0f, 0.542f, -0.375f, 0.959f)
                lineToRelative(-0.917f, 0.875f)
                quadToRelative(-0.375f, 0.416f, -0.916f, 0.416f)
                quadToRelative(-0.542f, 0f, -0.959f, -0.416f)
                close()
                moveToRelative(23.417f, 0f)
                lineToRelative(-0.875f, -0.875f)
                quadToRelative(-0.375f, -0.375f, -0.375f, -0.938f)
                quadToRelative(0f, -0.562f, 0.375f, -0.979f)
                quadToRelative(0.417f, -0.375f, 0.958f, -0.375f)
                quadToRelative(0.542f, 0f, 0.959f, 0.417f)
                lineToRelative(0.875f, 0.875f)
                quadToRelative(0.375f, 0.375f, 0.395f, 0.916f)
                quadToRelative(0.021f, 0.542f, -0.395f, 0.959f)
                quadToRelative(-0.375f, 0.416f, -0.938f, 0.416f)
                quadToRelative(-0.562f, 0f, -0.979f, -0.416f)
                close()
                moveTo(8.208f, 10.042f)
                lineToRelative(-0.875f, -0.875f)
                quadToRelative(-0.375f, -0.375f, -0.395f, -0.938f)
                quadToRelative(-0.021f, -0.562f, 0.395f, -0.979f)
                quadToRelative(0.375f, -0.375f, 0.938f, -0.375f)
                quadToRelative(0.562f, 0f, 0.979f, 0.375f)
                lineToRelative(0.875f, 0.917f)
                quadToRelative(0.375f, 0.375f, 0.375f, 0.916f)
                quadToRelative(0f, 0.542f, -0.375f, 0.959f)
                quadToRelative(-0.417f, 0.375f, -0.958f, 0.396f)
                quadToRelative(-0.542f, 0.02f, -0.959f, -0.396f)
                close()
                moveTo(20f, 29.375f)
                quadToRelative(-3.917f, 0f, -6.646f, -2.75f)
                reflectiveQuadToRelative(-2.729f, -6.667f)
                quadToRelative(0f, -3.875f, 2.729f, -6.604f)
                reflectiveQuadTo(20f, 10.625f)
                quadToRelative(3.917f, 0f, 6.646f, 2.729f)
                reflectiveQuadTo(29.375f, 20f)
                quadToRelative(0f, 3.875f, -2.729f, 6.625f)
                reflectiveQuadTo(20f, 29.375f)
                close()
                moveToRelative(0f, -2.667f)
                quadToRelative(2.833f, 0f, 4.792f, -1.958f)
                quadTo(26.75f, 22.792f, 26.75f, 20f)
                quadToRelative(0f, -2.833f, -1.958f, -4.792f)
                quadTo(22.833f, 13.25f, 20f, 13.25f)
                quadToRelative(-2.833f, 0f, -4.792f, 1.958f)
                quadToRelative(-1.958f, 1.959f, -1.958f, 4.75f)
                quadToRelative(0f, 2.834f, 1.958f, 4.792f)
                quadToRelative(1.959f, 1.958f, 4.792f, 1.958f)
                close()
                moveToRelative(0f, -6.75f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberPartlyCloudyNight(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "partly_cloudy_night",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(Color.Black),
                fillAlpha = 1f,
                stroke = null,
                strokeAlpha = 1f,
                strokeLineWidth = 1.0f,
                strokeLineCap = StrokeCap.Butt,
                strokeLineJoin = StrokeJoin.Miter,
                strokeLineMiter = 1f,
                pathFillType = PathFillType.NonZero
            ) {
                moveTo(23.542f, 36.5f)
                quadToRelative(-1.959f, 0f, -3.917f, -0.438f)
                quadToRelative(-1.958f, -0.437f, -3.458f, -1.145f)
                horizontalLineTo(10f)
                quadToRelative(-3.417f, 0f, -5.833f, -2.417f)
                quadToRelative(-2.417f, -2.417f, -2.417f, -5.833f)
                quadToRelative(0f, -2.542f, 1.375f, -4.605f)
                quadTo(4.5f, 20f, 6.833f, 19.042f)
                quadTo(7f, 14.125f, 10.396f, 9.688f)
                quadToRelative(3.396f, -4.438f, 8.854f, -5.855f)
                quadToRelative(0.333f, -0.083f, 0.792f, -0.208f)
                quadToRelative(0.458f, -0.125f, 0.75f, -0.125f)
                quadToRelative(0.666f, 0f, 1.083f, 0.5f)
                quadToRelative(0.417f, 0.5f, 0.417f, 1.125f)
                quadToRelative(0f, 0.125f, -0.042f, 0.25f)
                lineToRelative(-0.083f, 0.25f)
                quadToRelative(-0.542f, 1.417f, -0.75f, 2.979f)
                quadToRelative(-0.209f, 1.563f, -0.209f, 3.063f)
                quadToRelative(0f, 6f, 3.959f, 10.791f)
                quadToRelative(3.958f, 4.792f, 10.041f, 5.792f)
                quadToRelative(0.584f, 0.083f, 0.938f, 0.479f)
                quadToRelative(0.354f, 0.396f, 0.354f, 0.896f)
                quadToRelative(0f, 0.25f, -0.104f, 0.5f)
                reflectiveQuadToRelative(-0.313f, 0.5f)
                quadToRelative(-2.25f, 2.625f, -5.604f, 4.25f)
                reflectiveQuadTo(23.542f, 36.5f)
                close()
                moveTo(10f, 32.292f)
                horizontalLineToRelative(7.5f)
                quadToRelative(1.292f, 0f, 2.208f, -0.917f)
                quadToRelative(0.917f, -0.917f, 0.917f, -2.208f)
                quadToRelative(0f, -1.292f, -0.917f, -2.209f)
                quadToRelative(-0.916f, -0.916f, -2.208f, -0.916f)
                horizontalLineToRelative(-1.958f)
                quadToRelative(-0.542f, -2.417f, -1.98f, -3.709f)
                quadToRelative(-1.437f, -1.291f, -3.562f, -1.291f)
                quadToRelative(-2.333f, 0f, -3.979f, 1.646f)
                quadToRelative(-1.646f, 1.645f, -1.646f, 3.979f)
                quadToRelative(0f, 2.333f, 1.646f, 3.979f)
                reflectiveQuadTo(10f, 32.292f)
                close()
                moveToRelative(11.083f, 1.375f)
                quadToRelative(0.459f, 0.083f, 1.146f, 0.145f)
                quadToRelative(0.688f, 0.063f, 1.313f, 0.063f)
                quadToRelative(2.458f, 0f, 4.916f, -1.021f)
                quadToRelative(2.459f, -1.021f, 4.167f, -2.479f)
                quadToRelative(-6.292f, -2f, -10.167f, -7.125f)
                reflectiveQuadToRelative(-3.916f, -11.583f)
                quadToRelative(0f, -1f, 0.187f, -2.479f)
                quadToRelative(0.188f, -1.48f, 0.396f, -2.521f)
                quadToRelative(-4.208f, 1.666f, -6.708f, 4.75f)
                quadToRelative(-2.5f, 3.083f, -2.917f, 7f)
                quadToRelative(2.25f, -0.167f, 4.479f, 1.083f)
                reflectiveQuadToRelative(3.396f, 3.875f)
                quadToRelative(2.375f, 0.083f, 4.146f, 1.771f)
                quadToRelative(1.771f, 1.687f, 1.771f, 4.021f)
                quadToRelative(0f, 1.416f, -0.604f, 2.583f)
                quadToRelative(-0.605f, 1.167f, -1.605f, 1.917f)
                close()
            }
        }.build()
    }
}