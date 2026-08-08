package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MisuEntity
import com.example.ui.MisuViewModel
import com.example.ui.components.*
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: MisuViewModel,
    misuState: MisuEntity,
    onNavigateToChat: () -> Unit,
    onNavigateToCustomization: () -> Unit,
    onNavigateToCareMenu: () -> Unit,
    onNavigateToHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentAffirmation by viewModel.currentAffirmation.collectAsState()
    val activeCareDialog by viewModel.activeCareDialog.collectAsState()
    val selfCareMessage by viewModel.selfCareMessage.collectAsState()

    val careItems = remember {
        listOf(
            CareActionItem("feed", "Alimentar", "Le das de comer a Misu", "¿Ya tomaste agua o comiste hoy?", "🐟", YellowPastel),
            CareActionItem("sleep", "Dormir", "Arropas a Misu para la siesta", "Rutina de sueño y respiración 4-7-8", "🌙", LilacSoft),
            CareActionItem("dress", "Vestirte", "Le pones ropa a Misu", "Elige 1 cosa bonita para ti hoy", "👗", PinkPastel),
            CareActionItem("pamper", "Cuidarte", "Lo limpias y mimas con amor", "Micro-hábitos: estirarte, afirmación", "✨", MintPastel),
            CareActionItem("play", "Jugar", "Juegas con pelota y lana", "Pausa activa: 2 min de movimiento", "🧶", BluePastel),
            CareActionItem("accessories", "Accesorios", "Le pones moños y lentes", "Yo también merezco arreglarme", "🎀", YellowPastel)
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Dynamic Room Wallpaper
        RoomBackground(roomThemeId = misuState.roomThemeId)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Misu Name Badge & Mood Badge
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(LilacPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🐾", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = misuState.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        )
                        Text(
                            text = "Estado: ${misuState.moodState.lowercase().replaceFirstChar { it.uppercase() }}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = PurpleDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                // Stars Badge & History Button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = YellowWarm.copy(alpha = 0.3f),
                        modifier = Modifier.clickable { onNavigateToHistory() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⭐", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${misuState.stars}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = onNavigateToHistory,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(CardSurface)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.History,
                            contentDescription = "Historial de Emociones",
                            tint = PurpleDark
                        )
                    }
                }
            }

            // Status Progress Bars (Amor, Alegría, Energía)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.9f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatIndicator(label = "Amor", value = misuState.love, emoji = "💖", color = PinkAccent)
                    StatIndicator(label = "Alegría", value = misuState.happiness, emoji = "🌟", color = YellowWarm)
                    StatIndicator(label = "Energía", value = misuState.energy, emoji = "⚡", color = BluePrimary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Interactive Misu View
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentAlignment = Alignment.Center
            ) {
                MisuAvatar(
                    size = 240.dp,
                    furColorId = misuState.furColorId,
                    outfitId = misuState.outfitId,
                    accessoryId = misuState.accessoryId,
                    cushionColorId = misuState.cushionColorId,
                    collarStyle = misuState.collarStyle,
                    moodState = misuState.moodState,
                    onTapMisu = { viewModel.tapMisu() },
                    modifier = Modifier.testTag("misu_main_avatar")
                )
            }

            // Misu Greeting Speech
            Text(
                text = "¡Hola! Soy Misu. ¿Cómo te sientes hoy? 🐾",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 17.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Chat Action Button: "JUGAMOS - Chat Emocional"
            Button(
                onClick = onNavigateToChat,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(54.dp)
                    .testTag("chat_button"),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LilacPrimary)
            ) {
                Icon(
                    imageVector = Icons.Rounded.ChatBubble,
                    contentDescription = null,
                    tint = CardSurface
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "JUGAMOS: Cuéntame cómo te sientes 💬",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = CardSurface,
                        fontSize = 15.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "Consejo de Misu" Affirmation Cloud Bubble
            ConsejoBubble(
                affirmationText = currentAffirmation,
                onRefresh = { viewModel.nextAffirmation() },
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Section: "¿Qué necesitas ahora?"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Qué necesitas ahora? 🐾",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        fontSize = 18.sp
                    )
                )

                TextButton(onClick = onNavigateToCareMenu) {
                    Text("Ver todos", color = PurpleDark, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 6 Care Grid Buttons
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CareActionButton(
                        item = careItems[0],
                        onClick = { viewModel.openCareDialog(careItems[0]) },
                        modifier = Modifier.weight(1f)
                    )
                    CareActionButton(
                        item = careItems[1],
                        onClick = { viewModel.openCareDialog(careItems[1]) },
                        modifier = Modifier.weight(1f)
                    )
                    CareActionButton(
                        item = careItems[2],
                        onClick = { viewModel.openCareDialog(careItems[2]) },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    CareActionButton(
                        item = careItems[3],
                        onClick = { viewModel.openCareDialog(careItems[3]) },
                        modifier = Modifier.weight(1f)
                    )
                    CareActionButton(
                        item = careItems[4],
                        onClick = { viewModel.openCareDialog(careItems[4]) },
                        modifier = Modifier.weight(1f)
                    )
                    CareActionButton(
                        item = careItems[5],
                        onClick = { viewModel.openCareDialog(careItems[5]) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Customization Banner: "Armario y Apariencia de Misu"
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onNavigateToCustomization() }
                    .testTag("customization_banner"),
                colors = CardDefaults.cardColors(containerColor = PinkPastel)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("👗", fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Armario y Estudio de Misu ✨",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PurpleDark
                                )
                            )
                            Text(
                                text = "Cambia pelaje, ropa, lentes, moños, cojín y habitación",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextDark.copy(alpha = 0.8f),
                                    fontSize = 12.sp
                                )
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.Rounded.ChevronRight,
                        contentDescription = "Personalizar Misu",
                        tint = PurpleDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Message
            Text(
                text = "“Juega, cuida y vuelve a contarme con tu mejor amigo: Misu” 🐾",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextMuted,
                    fontSize = 12.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            )
        }

        // Care Interaction Dialog
        val dialogItem = activeCareDialog
        if (dialogItem != null) {
            AlertDialog(
                onDismissRequest = { viewModel.closeCareDialog() },
                icon = { Text(dialogItem.emoji, fontSize = 40.sp) },
                title = {
                    Text(
                        text = dialogItem.title,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "🐾 Cuidado de Misu: ${dialogItem.misuCareText}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = PurpleDark
                            ),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Card(
                            colors = CardDefaults.cardColors(containerColor = LilacSoft.copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "💖 Reflejo para ti:\n${dialogItem.selfCareReflection}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextDark,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 13.sp
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.performCareAction(dialogItem.id) },
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleDark)
                    ) {
                        Text("Cuidar a Misu y a Mí 💖")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.closeCareDialog() }) {
                        Text("Cerrar")
                    }
                }
            )
        }

        // Toast Snackbar for self-care reflections
        val snackbarMsg = selfCareMessage
        if (snackbarMsg != null) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { viewModel.dismissSelfCareMessage() }) {
                        Text("OK 🐾", color = YellowWarm)
                    }
                },
                containerColor = PurpleDark,
                contentColor = CardSurface
            ) {
                Text(snackbarMsg, fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun StatIndicator(
    label: String,
    value: Int,
    emoji: String,
    color: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$value%",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        LinearProgressIndicator(
            progress = { value / 100f },
            modifier = Modifier
                .width(70.dp)
                .height(6.dp)
                .clip(CircleShape),
            color = color,
            trackColor = color.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                color = TextMuted
            )
        )
    }
}
