package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun BreathingGuide(
    modifier: Modifier = Modifier,
    onCompleteCycle: () -> Unit
) {
    var isRunning by remember { mutableStateOf(false) }
    var phase by remember { mutableStateOf("Inhala en 4s") } // Inhala (4s), Mantén (7s), Exhala (8s)
    var countdown by remember { mutableIntStateOf(4) }
    var cycleCount by remember { mutableIntStateOf(0) }

    // Scale animation for breathing circle
    val circleScale by animateFloatAsState(
        targetValue = when {
            !isRunning -> 1f
            phase.startsWith("Inhala") -> 1.5f
            phase.startsWith("Mantén") -> 1.5f
            else -> 0.9f // Exhala
        },
        animationSpec = tween(
            durationMillis = when {
                phase.startsWith("Inhala") -> 4000
                phase.startsWith("Mantén") -> 100
                else -> 8000
            },
            easing = LinearEasing
        ),
        label = "circleScale"
    )

    LaunchedEffect(isRunning, phase, countdown) {
        if (isRunning) {
            if (countdown > 1) {
                delay(1000)
                countdown -= 1
            } else {
                when {
                    phase.startsWith("Inhala") -> {
                        phase = "Mantén el aire (7s)"
                        countdown = 7
                    }
                    phase.startsWith("Mantén") -> {
                        phase = "Exhala suavemente (8s)"
                        countdown = 8
                    }
                    else -> {
                        cycleCount += 1
                        onCompleteCycle()
                        if (cycleCount >= 3) {
                            isRunning = false
                            phase = "¡Excelente! Misu y tú están relajados 😴"
                            countdown = 0
                        } else {
                            phase = "Inhala hondo (4s)"
                            countdown = 4
                        }
                    }
                }
            }
        }
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = LilacSoft.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🌙 Rutina de Sueño 4-7-8 con Misu",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PurpleDark
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Breathing Circle Visualizer
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .scale(circleScale)
                    .clip(CircleShape)
                    .background(LilacPrimary.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(LilacPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (countdown > 0) "$countdown" else "🐾",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = CardSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = phase,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextDark,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    if (isRunning) {
                        isRunning = false
                        phase = "Preparados para relajarse"
                        countdown = 4
                    } else {
                        isRunning = true
                        cycleCount = 0
                        phase = "Inhala hondo (4s)"
                        countdown = 4
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = PurpleDark)
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Rounded.Stop else Icons.Rounded.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (isRunning) "Detener Respuestas" else "Iniciar Respiración 4-7-8")
            }
        }
    }
}
