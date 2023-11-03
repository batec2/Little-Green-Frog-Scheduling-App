package com.example.f23hopper.ui.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.f23hopper.R

@Composable
fun fullShiftIcon(): ImageVector = ImageVector.vectorResource(id = R.drawable.fullshift)

@Composable
fun unlockIcon(): ImageVector = ImageVector.vectorResource(id = R.drawable.lock_open_variant_outline)

@Composable
fun FullShiftIcon(modifier: Modifier = Modifier, tint: Color = Color.Black, size: Dp = 40.dp) {
    Icon(
        painter = painterResource(id = R.drawable.fullshift),
        contentDescription = "Full Shift Icon",
        modifier = modifier.size(size, size),
        tint = tint
    )
}

@Composable
fun dayShiftIcon(): ImageVector {
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    return remember {
        ImageVector.Builder(
            name = "sunny",
            defaultWidth = 40.0.dp,
            defaultHeight = 40.0.dp,
            viewportWidth = 40.0f,
            viewportHeight = 40.0f
        ).apply {
            path(
                fill = SolidColor(onPrimaryColor),
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
fun nightShiftIcon(): ImageVector {
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

@Composable
fun rememberLockOpen(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "lock_open",
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
                moveTo(9.542f, 36.375f)
                quadToRelative(-1.084f, 0f, -1.855f, -0.771f)
                quadToRelative(-0.77f, -0.771f, -0.77f, -1.854f)
                verticalLineTo(16.292f)
                quadToRelative(0f, -1.084f, 0.77f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.855f, -0.771f)
                horizontalLineToRelative(15.666f)
                verticalLineToRelative(-3.75f)
                quadToRelative(0f, -2.167f, -1.5f, -3.688f)
                quadToRelative(-1.5f, -1.521f, -3.708f, -1.521f)
                quadToRelative(-1.875f, 0f, -3.271f, 1.125f)
                reflectiveQuadToRelative(-1.812f, 2.834f)
                quadToRelative(-0.125f, 0.583f, -0.521f, 0.937f)
                quadToRelative(-0.396f, 0.354f, -0.896f, 0.354f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.416f)
                quadToRelative(-0.375f, -0.417f, -0.292f, -0.959f)
                quadToRelative(0.5f, -2.791f, 2.667f, -4.645f)
                quadTo(17.083f, 2.083f, 20f, 2.083f)
                quadToRelative(3.25f, 0f, 5.562f, 2.292f)
                quadToRelative(2.313f, 2.292f, 2.313f, 5.583f)
                verticalLineToRelative(3.709f)
                horizontalLineToRelative(2.583f)
                quadToRelative(1.084f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.77f, 0.771f, 1.854f)
                verticalLineTo(33.75f)
                quadToRelative(0f, 1.083f, -0.771f, 1.854f)
                quadToRelative(-0.77f, 0.771f, -1.854f, 0.771f)
                close()
                moveToRelative(0f, -2.625f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(16.292f)
                horizontalLineTo(9.542f)
                verticalLineTo(33.75f)
                close()
                moveTo(20f, 28.208f)
                quadToRelative(1.292f, 0f, 2.229f, -0.916f)
                quadToRelative(0.938f, -0.917f, 0.938f, -2.209f)
                quadToRelative(0f, -1.25f, -0.938f, -2.229f)
                quadToRelative(-0.937f, -0.979f, -2.229f, -0.979f)
                reflectiveQuadToRelative(-2.229f, 0.979f)
                quadToRelative(-0.938f, 0.979f, -0.938f, 2.229f)
                quadToRelative(0f, 1.292f, 0.938f, 2.209f)
                quadToRelative(0.937f, 0.916f, 2.229f, 0.916f)
                close()
                moveTo(9.542f, 16.292f)
                verticalLineTo(33.75f)
                verticalLineTo(16.292f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberLock(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "lock",
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
                moveTo(9.542f, 36.375f)
                quadToRelative(-1.084f, 0f, -1.855f, -0.771f)
                quadToRelative(-0.77f, -0.771f, -0.77f, -1.854f)
                verticalLineTo(16.292f)
                quadToRelative(0f, -1.084f, 0.77f, -1.854f)
                quadToRelative(0.771f, -0.771f, 1.855f, -0.771f)
                horizontalLineToRelative(2.583f)
                verticalLineTo(9.958f)
                quadToRelative(0f, -3.291f, 2.292f, -5.583f)
                quadTo(16.708f, 2.083f, 20f, 2.083f)
                quadToRelative(3.292f, 0f, 5.583f, 2.292f)
                quadToRelative(2.292f, 2.292f, 2.292f, 5.583f)
                verticalLineToRelative(3.709f)
                horizontalLineToRelative(2.583f)
                quadToRelative(1.084f, 0f, 1.854f, 0.771f)
                quadToRelative(0.771f, 0.77f, 0.771f, 1.854f)
                verticalLineTo(33.75f)
                quadToRelative(0f, 1.083f, -0.771f, 1.854f)
                quadToRelative(-0.77f, 0.771f, -1.854f, 0.771f)
                close()
                moveToRelative(5.25f, -22.708f)
                horizontalLineToRelative(10.416f)
                verticalLineToRelative(-3.75f)
                quadToRelative(0f, -2.167f, -1.5f, -3.688f)
                quadToRelative(-1.5f, -1.521f, -3.708f, -1.521f)
                quadToRelative(-2.167f, 0f, -3.688f, 1.521f)
                quadToRelative(-1.52f, 1.521f, -1.52f, 3.729f)
                close()
                moveTo(9.542f, 33.75f)
                horizontalLineToRelative(20.916f)
                verticalLineTo(16.292f)
                horizontalLineTo(9.542f)
                verticalLineTo(33.75f)
                close()
                moveTo(20f, 28.208f)
                quadToRelative(1.292f, 0f, 2.229f, -0.916f)
                quadToRelative(0.938f, -0.917f, 0.938f, -2.209f)
                quadToRelative(0f, -1.25f, -0.938f, -2.229f)
                quadToRelative(-0.937f, -0.979f, -2.229f, -0.979f)
                reflectiveQuadToRelative(-2.229f, 0.979f)
                quadToRelative(-0.938f, 0.979f, -0.938f, 2.229f)
                quadToRelative(0f, 1.292f, 0.938f, 2.209f)
                quadToRelative(0.937f, 0.916f, 2.229f, 0.916f)
                close()
                moveToRelative(0f, -3.166f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberFilterList(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "filter_list",
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
                moveTo(18.083f, 29.333f)
                quadToRelative(-0.583f, 0f, -0.958f, -0.395f)
                quadToRelative(-0.375f, -0.396f, -0.375f, -0.938f)
                quadToRelative(0f, -0.542f, 0.375f, -0.938f)
                quadToRelative(0.375f, -0.395f, 0.958f, -0.395f)
                horizontalLineToRelative(3.834f)
                quadToRelative(0.541f, 0f, 0.937f, 0.395f)
                quadToRelative(0.396f, 0.396f, 0.396f, 0.938f)
                quadToRelative(0f, 0.583f, -0.396f, 0.958f)
                reflectiveQuadToRelative(-0.937f, 0.375f)
                close()
                moveTo(6.5f, 12.792f)
                quadToRelative(-0.542f, 0f, -0.917f, -0.375f)
                reflectiveQuadToRelative(-0.375f, -0.959f)
                quadToRelative(0f, -0.541f, 0.375f, -0.937f)
                reflectiveQuadToRelative(0.917f, -0.396f)
                horizontalLineToRelative(26.958f)
                quadToRelative(0.584f, 0f, 0.959f, 0.396f)
                reflectiveQuadToRelative(0.375f, 0.937f)
                quadToRelative(0f, 0.584f, -0.375f, 0.959f)
                reflectiveQuadToRelative(-0.959f, 0.375f)
                close()
                moveToRelative(4.958f, 8.25f)
                quadToRelative(-0.541f, 0f, -0.937f, -0.375f)
                reflectiveQuadToRelative(-0.396f, -0.959f)
                quadToRelative(0f, -0.541f, 0.396f, -0.916f)
                reflectiveQuadToRelative(0.937f, -0.375f)
                horizontalLineToRelative(17.084f)
                quadToRelative(0.541f, 0f, 0.916f, 0.395f)
                quadToRelative(0.375f, 0.396f, 0.375f, 0.938f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                reflectiveQuadToRelative(-0.916f, 0.375f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberError(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "error",
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
                moveTo(20.125f, 21.875f)
                quadToRelative(0.542f, 0f, 0.917f, -0.396f)
                reflectiveQuadToRelative(0.375f, -0.937f)
                verticalLineToRelative(-7.667f)
                quadToRelative(0f, -0.5f, -0.396f, -0.875f)
                reflectiveQuadToRelative(-0.938f, -0.375f)
                quadToRelative(-0.541f, 0f, -0.916f, 0.375f)
                reflectiveQuadToRelative(-0.375f, 0.917f)
                verticalLineToRelative(7.666f)
                quadToRelative(0f, 0.542f, 0.396f, 0.917f)
                quadToRelative(0.395f, 0.375f, 0.937f, 0.375f)
                close()
                moveTo(20f, 28.208f)
                quadToRelative(0.625f, 0f, 1.021f, -0.416f)
                quadToRelative(0.396f, -0.417f, 0.396f, -1f)
                quadToRelative(0f, -0.625f, -0.396f, -1.021f)
                quadToRelative(-0.396f, -0.396f, -1.021f, -0.396f)
                quadToRelative(-0.625f, 0f, -1.021f, 0.396f)
                quadToRelative(-0.396f, 0.396f, -0.396f, 1.021f)
                quadToRelative(0f, 0.583f, 0.396f, 1f)
                quadToRelative(0.396f, 0.416f, 1.021f, 0.416f)
                close()
                moveToRelative(0f, 8.167f)
                quadToRelative(-3.458f, 0f, -6.458f, -1.25f)
                reflectiveQuadToRelative(-5.209f, -3.458f)
                quadToRelative(-2.208f, -2.209f, -3.458f, -5.209f)
                quadToRelative(-1.25f, -3f, -1.25f, -6.458f)
                reflectiveQuadToRelative(1.25f, -6.437f)
                quadToRelative(1.25f, -2.98f, 3.458f, -5.188f)
                quadToRelative(2.209f, -2.208f, 5.209f, -3.479f)
                quadToRelative(3f, -1.271f, 6.458f, -1.271f)
                reflectiveQuadToRelative(6.438f, 1.271f)
                quadToRelative(2.979f, 1.271f, 5.187f, 3.479f)
                reflectiveQuadToRelative(3.479f, 5.188f)
                quadToRelative(1.271f, 2.979f, 1.271f, 6.437f)
                reflectiveQuadToRelative(-1.271f, 6.458f)
                quadToRelative(-1.271f, 3f, -3.479f, 5.209f)
                quadToRelative(-2.208f, 2.208f, -5.187f, 3.458f)
                quadToRelative(-2.98f, 1.25f, -6.438f, 1.25f)
                close()
                moveTo(20f, 20f)
                close()
                moveToRelative(0f, 13.75f)
                quadToRelative(5.667f, 0f, 9.708f, -4.042f)
                quadTo(33.75f, 25.667f, 33.75f, 20f)
                reflectiveQuadToRelative(-4.042f, -9.708f)
                quadTo(25.667f, 6.25f, 20f, 6.25f)
                reflectiveQuadToRelative(-9.708f, 4.042f)
                quadTo(6.25f, 14.333f, 6.25f, 20f)
                reflectiveQuadToRelative(4.042f, 9.708f)
                quadTo(14.333f, 33.75f, 20f, 33.75f)
                close()
            }
        }.build()
    }
}


@Composable
fun rememberUndo(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "undo",
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
                moveTo(13.125f, 31.458f)
                quadToRelative(-0.542f, 0f, -0.937f, -0.375f)
                quadToRelative(-0.396f, -0.375f, -0.396f, -0.916f)
                quadToRelative(0f, -0.584f, 0.396f, -0.959f)
                quadToRelative(0.395f, -0.375f, 0.937f, -0.375f)
                horizontalLineToRelative(10.458f)
                quadToRelative(2.834f, 0f, 4.875f, -1.854f)
                quadToRelative(2.042f, -1.854f, 2.042f, -4.604f)
                quadToRelative(0f, -2.708f, -2.042f, -4.563f)
                quadToRelative(-2.041f, -1.854f, -4.875f, -1.854f)
                horizontalLineTo(11.875f)
                lineToRelative(3.708f, 3.667f)
                quadToRelative(0.375f, 0.417f, 0.375f, 0.958f)
                quadToRelative(0f, 0.542f, -0.375f, 0.917f)
                quadToRelative(-0.416f, 0.417f, -0.958f, 0.417f)
                reflectiveQuadToRelative(-0.917f, -0.417f)
                lineToRelative(-5.916f, -5.958f)
                quadToRelative(-0.209f, -0.167f, -0.313f, -0.396f)
                quadToRelative(-0.104f, -0.229f, -0.104f, -0.521f)
                quadToRelative(0f, -0.25f, 0.104f, -0.458f)
                quadToRelative(0.104f, -0.209f, 0.313f, -0.459f)
                lineToRelative(5.916f, -5.916f)
                quadToRelative(0.375f, -0.375f, 0.917f, -0.375f)
                reflectiveQuadToRelative(0.958f, 0.375f)
                quadToRelative(0.375f, 0.416f, 0.375f, 0.937f)
                quadToRelative(0f, 0.521f, -0.375f, 0.938f)
                lineToRelative(-3.708f, 3.666f)
                horizontalLineToRelative(11.667f)
                quadToRelative(3.958f, 0f, 6.77f, 2.605f)
                quadToRelative(2.813f, 2.604f, 2.813f, 6.437f)
                quadToRelative(0f, 3.875f, -2.813f, 6.479f)
                quadToRelative(-2.812f, 2.604f, -6.77f, 2.604f)
                close()
            }
        }.build()
    }
}

@Composable
fun rememberRedo(): ImageVector {
    return remember {
        ImageVector.Builder(
            name = "redo",
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
                moveTo(16.458f, 31.458f)
                quadToRelative(-3.958f, 0f, -6.77f, -2.604f)
                quadToRelative(-2.813f, -2.604f, -2.813f, -6.479f)
                quadToRelative(0f, -3.833f, 2.813f, -6.437f)
                quadToRelative(2.812f, -2.605f, 6.77f, -2.605f)
                horizontalLineToRelative(11.667f)
                lineToRelative(-3.667f, -3.666f)
                quadToRelative(-0.416f, -0.375f, -0.416f, -0.938f)
                quadToRelative(0f, -0.562f, 0.416f, -0.937f)
                quadToRelative(0.375f, -0.375f, 0.917f, -0.375f)
                reflectiveQuadToRelative(0.917f, 0.375f)
                lineToRelative(5.916f, 5.916f)
                quadToRelative(0.209f, 0.25f, 0.313f, 0.459f)
                quadToRelative(0.104f, 0.208f, 0.104f, 0.458f)
                quadToRelative(0f, 0.292f, -0.104f, 0.521f)
                quadToRelative(-0.104f, 0.229f, -0.313f, 0.396f)
                lineTo(26.292f, 21.5f)
                quadToRelative(-0.375f, 0.417f, -0.917f, 0.417f)
                reflectiveQuadToRelative(-0.917f, -0.417f)
                quadToRelative(-0.416f, -0.375f, -0.416f, -0.917f)
                quadToRelative(0f, -0.541f, 0.416f, -0.958f)
                lineToRelative(3.667f, -3.667f)
                horizontalLineTo(16.417f)
                quadToRelative(-2.834f, 0f, -4.875f, 1.854f)
                quadTo(9.5f, 19.667f, 9.5f, 22.375f)
                quadToRelative(0f, 2.792f, 2.042f, 4.625f)
                quadToRelative(2.041f, 1.833f, 4.875f, 1.833f)
                horizontalLineToRelative(10.458f)
                quadToRelative(0.542f, 0f, 0.937f, 0.375f)
                quadToRelative(0.396f, 0.375f, 0.396f, 0.959f)
                quadToRelative(0f, 0.541f, -0.396f, 0.916f)
                quadToRelative(-0.395f, 0.375f, -0.937f, 0.375f)
                close()
            }
        }.build()
    }
}
