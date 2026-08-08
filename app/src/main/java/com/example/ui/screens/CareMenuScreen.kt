package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MisuEntity
import com.example.ui.MisuViewModel
import com.example.ui.components.BreathingGuide
import com.example.ui.components.CareActionItem
import com.example.ui.components.RoomBackground
import com.example.ui.theme.*

@Composable
fun CareMenuScreen(
    viewModel: MisuViewModel,
    misuState: MisuEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showBreathingGuide by remember { mutableStateOf(false) }

    val fullCareItems = remember {
        listOf(
            CareActionItem(
                id = "feed",
                title = "1. Alimentario (Comida & Hidratación)",
                misuCareText = "Le das de comer a Misu su pescado o leche favorita",
                selfCareReflection = "¿Ya tomaste agüita o comiste algo rico hoy? Así como alimentas a Misu, alimenta tus logros.",
                emoji = "🐟",
                color = YellowPastel
            ),
            CareActionItem(
                id = "sleep",
                title = "2. Dormir & Calma (Rutina de Sueño)",
                misuCareText = "Arropas a Misu con una cobijita para su siesta",
                selfCareReflection = "Rutina de sueño: Suelta los hombros y practica la respiración 4-7-8.",
                emoji = "🌙",
                color = LilacSoft
            ),
            CareActionItem(
                id = "dress",
                title = "3. Vestirte & Amor Propio",
                misuCareText = "Le pones un suéter o hoodie cómodo a Misu",
                selfCareReflection = "Reto: Elige 1 prenda o detalle que te haga sentir cómodo/a y seguro/a hoy.",
                emoji = "👗",
                color = PinkPastel
            ),
            CareActionItem(
                id = "pamper",
                title = "4. Cuidarte & Micro-hábitos",
                misuCareText = "Lo bañas suavemente y lo acaricias",
                selfCareReflection = "Micro-hábitos: Estira tu espalda 1 minuto, sonríe y regálate una palabra amable.",
                emoji = "✨",
                color = MintPastel
            ),
            CareActionItem(
                id = "play",
                title = "5. Jugar & Pausa Activa",
                misuCareText = "Juegas con el ovillo de lana y pelota",
                selfCareReflection = "Pausa activa: 2 minutos de movimiento ligero o un dibujo sin presiones.",
                emoji = "🧶",
                color = BluePastel
            ),
            CareActionItem(
                id = "accessories",
                title = "6. Accesorios & Expresión Personal",
                misuCareText = "Le pones sus lentes o moños coquette",
                selfCareReflection = "Personalización: 'Yo también merezco arreglarme y brillar sin pedir permiso'.",
                emoji = "🎀",
                color = YellowWarm
            )
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        RoomBackground(roomThemeId = misuState.roomThemeId)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(CardSurface)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Volver",
                        tint = TextDark
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "¿Qué necesitas ahora? 🐾",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "El cuidado de Misu es tu propio reflejo de autocuidado",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            if (showBreathingGuide) {
                BreathingGuide(
                    onCompleteCycle = { viewModel.performCareAction("sleep") },
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                items(fullCareItems) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(item.color),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(item.emoji, fontSize = 24.sp)
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark,
                                        fontSize = 15.sp
                                    ),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "🐾 Cuidado de Misu: ${item.misuCareText}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = PurpleDark,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Card(
                                colors = CardDefaults.cardColors(containerColor = LilacSoft.copy(alpha = 0.5f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "💖 Reflejo para ti: ${item.selfCareReflection}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextDark,
                                        lineHeight = 18.sp
                                    ),
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                if (item.id == "sleep") {
                                    OutlinedButton(
                                        onClick = { showBreathingGuide = !showBreathingGuide },
                                        shape = RoundedCornerShape(20.dp)
                                    ) {
                                        Text(if (showBreathingGuide) "Ocultar Guía 🌙" else "Guía Respiración 4-7-8 🌙")
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                }

                                Button(
                                    onClick = { viewModel.performCareAction(item.id) },
                                    colors = ButtonDefaults.buttonColors(containerColor = PurpleDark),
                                    shape = RoundedCornerShape(20.dp)
                                ) {
                                    Text("Realizar Cuidado ✨")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
