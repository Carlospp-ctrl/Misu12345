package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.data.CustomizationData
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun MisuAvatar(
    modifier: Modifier = Modifier,
    size: Dp = 240.dp,
    furColorId: String = "white",
    outfitId: String = "hoodie_purple",
    accessoryId: String = "pink_bow",
    cushionColorId: String = "purple",
    collarStyle: String = "bell_red",
    moodState: String = "FELIZ",
    onTapMisu: () -> Unit = {}
) {
    // Interactive purr / bounce animation
    var isTapped by remember { mutableStateOf(false) }
    val bounceAnimation by animateFloatAsState(
        targetValue = if (isTapped) 12f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        finishedListener = { isTapped = false },
        label = "bounce"
    )

    // Breathing pulse
    val infiniteTransition = rememberInfiniteTransition(label = "breathe")
    val breatheOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breatheOffset"
    )

    val furColor = CustomizationData.FurColors.find { it.id == furColorId }?.color ?: Color(0xFFFAFAFA)
    val cushionColor = CustomizationData.Cushions.find { it.id == cushionColorId }?.color ?: CushionPurple

    Box(
        modifier = modifier
            .size(size)
            .clickable {
                isTapped = true
                onTapMisu()
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = this.size.width
            val canvasHeight = this.size.height
            val centerX = canvasWidth / 2f
            val centerY = canvasHeight / 2f + 10f - bounceAnimation - breatheOffset / 2f

            // 1. Draw Cushion
            drawCushion(this, centerX, centerY + 65f, cushionColor)

            // 2. Draw Tail (Swinging)
            drawTail(this, centerX + 60f, centerY + 30f, furColor)

            // 3. Draw Body
            drawMisuBody(this, centerX, centerY + 25f, furColor)

            // 4. Draw Outfit (if selected)
            drawOutfit(this, centerX, centerY + 25f, outfitId)

            // 5. Draw Collar
            drawCollar(this, centerX, centerY - 15f, collarStyle)

            // 6. Draw Head & Ears
            drawMisuHead(this, centerX, centerY - 35f, furColor)

            // 7. Draw Ears Inner Pink/Purple
            drawMisuEars(this, centerX, centerY - 35f, furColor)

            // 8. Draw Face (Eyes, Nose, Mouth, Blush Cheeks according to mood)
            drawMisuFace(this, centerX, centerY - 35f, moodState)

            // 9. Draw Accessory (Bow, Glasses, Hat, Headphones, etc.)
            drawAccessory(this, centerX, centerY - 35f, accessoryId)

            // 10. Draw Floating Purr Hearts if tapped
            if (isTapped) {
                drawFloatingHearts(this, centerX, centerY - 110f)
            }
        }
    }
}

private fun drawCushion(drawScope: DrawScope, centerX: Float, centerY: Float, color: Color) {
    val path = Path().apply {
        addOval(
            androidx.compose.ui.geometry.Rect(
                left = centerX - 110f,
                top = centerY - 25f,
                right = centerX + 110f,
                bottom = centerY + 45f
            )
        )
    }
    // Shadow
    drawScope.drawOval(
        color = Color.Black.copy(alpha = 0.12f),
        topLeft = Offset(centerX - 100f, centerY + 20f),
        size = Size(200f, 30f)
    )
    // Cushion Base
    drawScope.drawPath(path, color)
    // Cushion Border/Pattern
    drawScope.drawPath(
        path = path,
        color = Color.White.copy(alpha = 0.4f),
        style = Stroke(width = 4f)
    )
}

private fun drawMisuBody(drawScope: DrawScope, centerX: Float, centerY: Float, furColor: Color) {
    val bodyPath = Path().apply {
        moveTo(centerX - 55f, centerY + 45f)
        cubicTo(
            centerX - 65f, centerY - 10f,
            centerX + 65f, centerY - 10f,
            centerX + 55f, centerY + 45f
        )
        cubicTo(
            centerX + 50f, centerY + 65f,
            centerX - 50f, centerY + 65f,
            centerX - 55f, centerY + 45f
        )
    }
    drawScope.drawPath(bodyPath, furColor)
    // Dark outline
    drawScope.drawPath(
        path = bodyPath,
        color = TextDark.copy(alpha = 0.25f),
        style = Stroke(width = 3.5f)
    )

    // Paws (front paws)
    drawScope.drawRoundRect(
        color = furColor,
        topLeft = Offset(centerX - 35f, centerY + 30f),
        size = Size(28f, 22f),
        cornerRadius = CornerRadius(12f, 12f)
    )
    drawScope.drawRoundRect(
        color = furColor,
        topLeft = Offset(centerX + 7f, centerY + 30f),
        size = Size(28f, 22f),
        cornerRadius = CornerRadius(12f, 12f)
    )
}

private fun drawTail(drawScope: DrawScope, startX: Float, startY: Float, furColor: Color) {
    val tailPath = Path().apply {
        moveTo(startX, startY)
        cubicTo(
            startX + 40f, startY - 20f,
            startX + 50f, startY - 60f,
            startX + 35f, startY - 70f
        )
        cubicTo(
            startX + 20f, startY - 60f,
            startX + 25f, startY - 10f,
            startX, startY
        )
    }
    drawScope.drawPath(tailPath, furColor)
    drawScope.drawPath(tailPath, TextDark.copy(alpha = 0.2f), style = Stroke(width = 3f))
}

private fun drawMisuHead(drawScope: DrawScope, centerX: Float, centerY: Float, furColor: Color) {
    // Oval Head
    drawScope.drawOval(
        color = furColor,
        topLeft = Offset(centerX - 75f, centerY - 55f),
        size = Size(150f, 110f)
    )
    drawScope.drawOval(
        color = TextDark.copy(alpha = 0.2f),
        topLeft = Offset(centerX - 75f, centerY - 55f),
        size = Size(150f, 110f),
        style = Stroke(width = 3.5f)
    )
}

private fun drawMisuEars(drawScope: DrawScope, centerX: Float, centerY: Float, furColor: Color) {
    // Left Ear
    val leftEar = Path().apply {
        moveTo(centerX - 65f, centerY - 30f)
        lineTo(centerX - 75f, centerY - 85f)
        lineTo(centerX - 30f, centerY - 50f)
        close()
    }
    // Right Ear
    val rightEar = Path().apply {
        moveTo(centerX + 65f, centerY - 30f)
        lineTo(centerX + 75f, centerY - 85f)
        lineTo(centerX + 30f, centerY - 50f)
        close()
    }

    drawScope.drawPath(leftEar, furColor)
    drawScope.drawPath(rightEar, furColor)

    // Inner Ears (Pink)
    val leftInner = Path().apply {
        moveTo(centerX - 60f, centerY - 35f)
        lineTo(centerX - 68f, centerY - 76f)
        lineTo(centerX - 36f, centerY - 50f)
        close()
    }
    val rightInner = Path().apply {
        moveTo(centerX + 60f, centerY - 35f)
        lineTo(centerX + 68f, centerY - 76f)
        lineTo(centerX + 36f, centerY - 50f)
        close()
    }
    drawScope.drawPath(leftInner, PinkAccent.copy(alpha = 0.7f))
    drawScope.drawPath(rightInner, PinkAccent.copy(alpha = 0.7f))

    drawScope.drawPath(leftEar, TextDark.copy(alpha = 0.2f), style = Stroke(width = 3f))
    drawScope.drawPath(rightEar, TextDark.copy(alpha = 0.2f), style = Stroke(width = 3f))
}

private fun drawMisuFace(drawScope: DrawScope, centerX: Float, centerY: Float, moodState: String) {
    // Blush Cheeks (Pink Circles)
    drawScope.drawCircle(
        color = PinkAccent.copy(alpha = 0.45f),
        radius = 12f,
        center = Offset(centerX - 42f, centerY + 8f)
    )
    drawScope.drawCircle(
        color = PinkAccent.copy(alpha = 0.45f),
        radius = 12f,
        center = Offset(centerX + 42f, centerY + 8f)
    )

    // Nose
    val nose = Path().apply {
        moveTo(centerX - 5f, centerY - 2f)
        lineTo(centerX + 5f, centerY - 2f)
        lineTo(centerX, centerY + 4f)
        close()
    }
    drawScope.drawPath(nose, PinkAccent)

    when (moodState.uppercase()) {
        "CANSADO" -> {
            // Sleeping closed eyes (Arcs)
            val leftEye = Path().apply {
                moveTo(centerX - 35f, centerY - 10f)
                quadraticTo(centerX - 25f, centerY - 2f, centerX - 15f, centerY - 10f)
            }
            val rightEye = Path().apply {
                moveTo(centerX + 15f, centerY - 10f)
                quadraticTo(centerX + 25f, centerY - 2f, centerX + 35f, centerY - 10f)
            }
            drawScope.drawPath(leftEye, TextDark, style = Stroke(width = 4f, cap = StrokeCap.Round))
            drawScope.drawPath(rightEye, TextDark, style = Stroke(width = 4f, cap = StrokeCap.Round))

            // Small Mouth "o"
            drawScope.drawCircle(TextDark, radius = 4f, center = Offset(centerX, centerY + 12f))
        }

        "TRISTE" -> {
            // Big teary eyes with pupil
            drawScope.drawCircle(TextDark, radius = 12f, center = Offset(centerX - 25f, centerY - 10f))
            drawScope.drawCircle(TextDark, radius = 12f, center = Offset(centerX + 25f, centerY - 10f))
            // White highlights
            drawScope.drawCircle(Color.White, radius = 4f, center = Offset(centerX - 27f, centerY - 13f))
            drawScope.drawCircle(Color.White, radius = 4f, center = Offset(centerX + 23f, centerY - 13f))

            // Tear drops
            drawScope.drawCircle(BluePrimary.copy(alpha = 0.8f), radius = 5f, center = Offset(centerX - 35f, centerY + 5f))

            // Sad mouth arc upside down
            val sadMouth = Path().apply {
                moveTo(centerX - 8f, centerY + 14f)
                quadraticTo(centerX, centerY + 8f, centerX + 8f, centerY + 14f)
            }
            drawScope.drawPath(sadMouth, TextDark, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
        }

        "EMOCIONADO" -> {
            // Starry / Sparkle Happy Eyes ^_^
            val leftEye = Path().apply {
                moveTo(centerX - 35f, centerY - 6f)
                lineTo(centerX - 25f, centerY - 16f)
                lineTo(centerX - 15f, centerY - 6f)
            }
            val rightEye = Path().apply {
                moveTo(centerX + 15f, centerY - 6f)
                lineTo(centerX + 25f, centerY - 16f)
                lineTo(centerX + 35f, centerY - 6f)
            }
            drawScope.drawPath(leftEye, TextDark, style = Stroke(width = 4.5f, cap = StrokeCap.Round))
            drawScope.drawPath(rightEye, TextDark, style = Stroke(width = 4.5f, cap = StrokeCap.Round))

            // Big open happy mouth
            val happyMouth = Path().apply {
                moveTo(centerX - 10f, centerY + 8f)
                quadraticTo(centerX, centerY + 22f, centerX + 10f, centerY + 8f)
                close()
            }
            drawScope.drawPath(happyMouth, TextDark)
            drawScope.drawPath(happyMouth, PinkAccent)
        }

        "HAMBRIENTO" -> {
            // Big round shiny eyes looking up
            drawScope.drawCircle(TextDark, radius = 13f, center = Offset(centerX - 25f, centerY - 10f))
            drawScope.drawCircle(TextDark, radius = 13f, center = Offset(centerX + 25f, centerY - 10f))
            drawScope.drawCircle(Color.White, radius = 5f, center = Offset(centerX - 23f, centerY - 12f))
            drawScope.drawCircle(Color.White, radius = 5f, center = Offset(centerX + 27f, centerY - 12f))

            // Open mouth for food :D
            drawScope.drawArc(
                color = PinkAccent,
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(centerX - 10f, centerY + 6f),
                size = Size(20f, 16f)
            )
        }

        else -> {
            // FELIZ / Standard Kawaii Eyes
            drawScope.drawCircle(TextDark, radius = 11f, center = Offset(centerX - 25f, centerY - 10f))
            drawScope.drawCircle(TextDark, radius = 11f, center = Offset(centerX + 25f, centerY - 10f))
            // Eye shine
            drawScope.drawCircle(Color.White, radius = 4.5f, center = Offset(centerX - 27f, centerY - 13f))
            drawScope.drawCircle(Color.White, radius = 4.5f, center = Offset(centerX + 23f, centerY - 13f))

            // Cute cat mouth :3
            val leftMouth = Path().apply {
                moveTo(centerX - 10f, centerY + 6f)
                quadraticTo(centerX - 5f, centerY + 12f, centerX, centerY + 6f)
            }
            val rightMouth = Path().apply {
                moveTo(centerX, centerY + 6f)
                quadraticTo(centerX + 5f, centerY + 12f, centerX + 10f, centerY + 6f)
            }
            drawScope.drawPath(leftMouth, TextDark, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
            drawScope.drawPath(rightMouth, TextDark, style = Stroke(width = 3.5f, cap = StrokeCap.Round))
        }
    }
}

private fun drawOutfit(drawScope: DrawScope, centerX: Float, centerY: Float, outfitId: String) {
    val outfitColor = CustomizationData.Outfits.find { it.id == outfitId }?.color ?: Color.Unspecified
    if (outfitColor == Color.Unspecified || outfitId == "none") return

    when (outfitId) {
        "hoodie_purple", "tee_heart", "sweater_striped" -> {
            val outfitPath = Path().apply {
                moveTo(centerX - 48f, centerY + 10f)
                cubicTo(
                    centerX - 55f, centerY + 20f,
                    centerX - 55f, centerY + 50f,
                    centerX - 45f, centerY + 55f
                )
                lineTo(centerX + 45f, centerY + 55f)
                cubicTo(
                    centerX + 55f, centerY + 50f,
                    centerX + 55f, centerY + 20f,
                    centerX + 48f, centerY + 10f
                )
                close()
            }
            drawScope.drawPath(outfitPath, outfitColor)

            if (outfitId == "tee_heart") {
                drawScope.drawCircle(Color.White, radius = 8f, center = Offset(centerX, centerY + 30f))
            } else if (outfitId == "sweater_striped") {
                drawScope.drawLine(Color.White, Offset(centerX - 40f, centerY + 25f), Offset(centerX + 40f, centerY + 25f), strokeWidth = 5f)
                drawScope.drawLine(Color.White, Offset(centerX - 42f, centerY + 40f), Offset(centerX + 42f, centerY + 40f), strokeWidth = 5f)
            }
        }
        "hero_cape" -> {
            val capePath = Path().apply {
                moveTo(centerX - 40f, centerY + 5f)
                lineTo(centerX - 70f, centerY + 60f)
                quadraticTo(centerX, centerY + 70f, centerX + 70f, centerY + 60f)
                lineTo(centerX + 40f, centerY + 5f)
                close()
            }
            drawScope.drawPath(capePath, outfitColor)
        }
        else -> {
            drawScope.drawRoundRect(
                color = outfitColor,
                topLeft = Offset(centerX - 45f, centerY + 10f),
                size = Size(90f, 45f),
                cornerRadius = CornerRadius(16f, 16f)
            )
        }
    }
}

private fun drawCollar(drawScope: DrawScope, centerX: Float, centerY: Float, style: String) {
    if (style == "none") return

    val collarPath = Path().apply {
        moveTo(centerX - 40f, centerY - 5f)
        quadraticTo(centerX, centerY + 12f, centerX + 40f, centerY - 5f)
    }

    when (style) {
        "bell_red" -> {
            drawScope.drawPath(collarPath, PinkAccent, style = Stroke(width = 6f, cap = StrokeCap.Round))
            // Yellow Bell
            drawScope.drawCircle(YellowWarm, radius = 6f, center = Offset(centerX, centerY + 8f))
        }
        "pendant_heart" -> {
            drawScope.drawPath(collarPath, PurpleDark, style = Stroke(width = 6f, cap = StrokeCap.Round))
            // Gold heart
            drawScope.drawCircle(YellowWarm, radius = 7f, center = Offset(centerX, centerY + 8f))
        }
        "ribbon_gold" -> {
            drawScope.drawPath(collarPath, YellowWarm, style = Stroke(width = 7f, cap = StrokeCap.Round))
        }
    }
}

private fun drawAccessory(drawScope: DrawScope, centerX: Float, centerY: Float, accessoryId: String) {
    val accColor = CustomizationData.Accessories.find { it.id == accessoryId }?.color ?: Color.Unspecified

    when (accessoryId) {
        "pink_bow" -> {
            // Ribbon at ear
            val bowCenter = Offset(centerX + 45f, centerY - 50f)
            drawScope.drawCircle(accColor, radius = 6f, center = bowCenter)
            drawScope.drawCircle(accColor, radius = 10f, center = Offset(bowCenter.x - 12f, bowCenter.y))
            drawScope.drawCircle(accColor, radius = 10f, center = Offset(bowCenter.x + 12f, bowCenter.y))
        }

        "glasses" -> {
            // Round glasses over eyes
            drawScope.drawCircle(Color.Black, radius = 16f, center = Offset(centerX - 25f, centerY - 10f), style = Stroke(width = 3.5f))
            drawScope.drawCircle(Color.Black, radius = 16f, center = Offset(centerX + 25f, centerY - 10f), style = Stroke(width = 3.5f))
            drawScope.drawLine(Color.Black, Offset(centerX - 9f, centerY - 10f), Offset(centerX + 9f, centerY - 10f), strokeWidth = 3.5f)
        }

        "frog_hat" -> {
            // Frog eyes hat on top of head
            drawScope.drawArc(
                color = accColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(centerX - 50f, centerY - 75f),
                size = Size(100f, 40f)
            )
            // Frog eyes
            drawScope.drawCircle(accColor, radius = 12f, center = Offset(centerX - 30f, centerY - 75f))
            drawScope.drawCircle(accColor, radius = 12f, center = Offset(centerX + 30f, centerY - 75f))
            drawScope.drawCircle(Color.White, radius = 6f, center = Offset(centerX - 30f, centerY - 75f))
            drawScope.drawCircle(Color.White, radius = 6f, center = Offset(centerX + 30f, centerY - 75f))
            drawScope.drawCircle(Color.Black, radius = 3f, center = Offset(centerX - 30f, centerY - 75f))
            drawScope.drawCircle(Color.Black, radius = 3f, center = Offset(centerX + 30f, centerY - 75f))
        }

        "crown" -> {
            val crownPath = Path().apply {
                moveTo(centerX - 25f, centerY - 55f)
                lineTo(centerX - 30f, centerY - 85f)
                lineTo(centerX - 12f, centerY - 65f)
                lineTo(centerX, centerY - 90f)
                lineTo(centerX + 12f, centerY - 65f)
                lineTo(centerX + 30f, centerY - 85f)
                lineTo(centerX + 25f, centerY - 55f)
                close()
            }
            drawScope.drawPath(crownPath, YellowWarm)
            drawScope.drawPath(crownPath, TextDark.copy(alpha = 0.3f), style = Stroke(width = 2.5f))
        }

        "cherry_flower" -> {
            val flowerCenter = Offset(centerX - 42f, centerY - 48f)
            for (i in 0 until 5) {
                val angle = i * 72f * (Math.PI / 180f)
                val petX = flowerCenter.x + (12f * sin(angle)).toFloat()
                val petY = flowerCenter.y + (12f * kotlin.math.cos(angle)).toFloat()
                drawScope.drawCircle(PinkPastel, radius = 8f, center = Offset(petX, petY))
            }
            drawScope.drawCircle(YellowWarm, radius = 6f, center = flowerCenter)
        }

        "headphones" -> {
            // Arch across head
            drawScope.drawArc(
                color = accColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(centerX - 65f, centerY - 75f),
                size = Size(130f, 80f),
                style = Stroke(width = 8f)
            )
            // Ear cups
            drawScope.drawRoundRect(accColor, topLeft = Offset(centerX - 75f, centerY - 30f), size = Size(18f, 35f), cornerRadius = CornerRadius(8f, 8f))
            drawScope.drawRoundRect(accColor, topLeft = Offset(centerX + 57f, centerY - 30f), size = Size(18f, 35f), cornerRadius = CornerRadius(8f, 8f))
        }
    }
}

private fun drawFloatingHearts(drawScope: DrawScope, centerX: Float, centerY: Float) {
    val heartColor = PinkAccent.copy(alpha = 0.85f)
    drawScope.drawCircle(heartColor, radius = 8f, center = Offset(centerX - 35f, centerY - 15f))
    drawScope.drawCircle(heartColor, radius = 12f, center = Offset(centerX, centerY - 35f))
    drawScope.drawCircle(heartColor, radius = 9f, center = Offset(centerX + 35f, centerY - 20f))
}
