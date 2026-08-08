package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ui.theme.*

@Composable
fun RoomBackground(
    roomThemeId: String = "cozy_room",
    modifier: Modifier = Modifier
) {
    val gradientColors = when (roomThemeId) {
        "flower_garden" -> listOf(Color(0xFFE8F8F5), Color(0xFFD4F5E9), Color(0xFFC7F3E5))
        "starry_night" -> listOf(Color(0xFF1E1A29), Color(0xFF2B2240), Color(0xFF382B55))
        "warm_sunset" -> listOf(Color(0xFFFFF0ED), Color(0xFFFFD8D0), Color(0xFFFFC6FF))
        "pastel_dream" -> listOf(Color(0xFFF3E5F5), Color(0xFFE1BEE7), Color(0xFFD1C4E9))
        else -> listOf(Color(0xFFF7F4FE), Color(0xFFEFE8FF), Color(0xFFE5DAFF)) // cozy_room
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // Wall Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = 0f,
                endY = height * 0.75f
            )
        )

        // Floor (Wooden Warm Tint)
        val floorTop = height * 0.72f
        val floorColor = when (roomThemeId) {
            "starry_night" -> Color(0xFF231D33)
            "flower_garden" -> Color(0xFFE2D4C3)
            else -> Color(0xFFF3E5D8)
        }
        drawRect(
            color = floorColor,
            topLeft = Offset(0f, floorTop),
            size = Size(width, height - floorTop)
        )
        // Floor Base Board Line
        drawLine(
            color = TextDark.copy(alpha = 0.15f),
            start = Offset(0f, floorTop),
            end = Offset(width, floorTop),
            strokeWidth = 4f
        )

        // Window or Decor Accent
        if (roomThemeId == "starry_night") {
            // Glowing Moon Window
            drawCircle(
                color = YellowWarm.copy(alpha = 0.8f),
                radius = 35f,
                center = Offset(width * 0.8f, height * 0.18f)
            )
            // Little stars
            drawCircle(Color.White.copy(alpha = 0.9f), radius = 3f, center = Offset(width * 0.2f, height * 0.12f))
            drawCircle(Color.White.copy(alpha = 0.8f), radius = 4f, center = Offset(width * 0.35f, height * 0.22f))
            drawCircle(Color.White.copy(alpha = 0.85f), radius = 3f, center = Offset(width * 0.65f, height * 0.08f))
        } else {
            // Soft Sun/Lamp Glow Window
            drawCircle(
                color = YellowPastel.copy(alpha = 0.6f),
                radius = 45f,
                center = Offset(width * 0.82f, height * 0.18f)
            )
        }
    }
}
