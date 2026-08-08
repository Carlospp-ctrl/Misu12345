package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MisuEntity
import com.example.data.MoodLogEntity
import com.example.ui.MisuViewModel
import com.example.ui.components.MisuAvatar
import com.example.ui.components.RoomBackground
import com.example.ui.theme.*

@Composable
fun ChatScreen(
    viewModel: MisuViewModel,
    misuState: MisuEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moodLogs by viewModel.moodLogs.collectAsState()
    val isChatLoading by viewModel.isChatLoading.collectAsState()

    var selectedMoodTag by remember { mutableStateOf("Triste") }
    var inputText by remember { mutableStateOf("") }
    var isRecordingVoice by remember { mutableStateOf(false) }

    val moodChips = remember {
        listOf(
            "Triste" to "😢",
            "Ansioso/a" to "😰",
            "Cansado/a" to "🥱",
            "Feliz" to "😊",
            "Orgulloso/a" to "⭐",
            "Abrumado/a" to "💭"
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        RoomBackground(roomThemeId = misuState.roomThemeId)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            // Header Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "JUGAMOS: Chat Emocional 💬",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Misu te escucha sin juicios y te acompaña",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                // Small Misu Header Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(LilacSoft),
                    contentAlignment = Alignment.Center
                ) {
                    MisuAvatar(
                        size = 50.dp,
                        furColorId = misuState.furColorId,
                        outfitId = misuState.outfitId,
                        accessoryId = misuState.accessoryId,
                        moodState = misuState.moodState
                    )
                }
            }

            // Emotion Tag Picker
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
                Text(
                    text = "1. Selecciona cómo te sientes hoy:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PurpleDark
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(moodChips) { (tag, emoji) ->
                        val isSelected = tag == selectedMoodTag
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedMoodTag = tag },
                            label = { Text("$emoji $tag", fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = LilacPrimary,
                                selectedLabelColor = CardSurface,
                                containerColor = CardSurface,
                                labelColor = TextDark
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Chat Messages History List
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                if (moodLogs.isEmpty() && !isChatLoading) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp)
                            .align(Alignment.Center),
                        colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.9f)),
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🐾", fontSize = 48.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "¡Hola! Estoy aquí listo para escucharte.",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Escribe o graba un audio contando cómo estás. Misu responderá con calma y cariño.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        reverseLayout = true
                    ) {
                        items(moodLogs) { log ->
                            MoodLogBubble(log = log)
                        }
                    }
                }

                if (isChatLoading) {
                    Card(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = PurpleDark),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = CardSurface,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Misu está redactando un abracito...",
                                color = CardSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Input Box Panel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    if (isRecordingVoice) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(PinkAccent)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Escuchando tu voz dulce... 🎙️",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextDark,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            TextButton(
                                onClick = {
                                    isRecordingVoice = false
                                    inputText = "Me siento algo $selectedMoodTag hoy..."
                                }
                            ) {
                                Text("Convertir a texto")
                            }
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { inputText = it },
                                placeholder = { Text("Cuéntame qué pasó hoy...", fontSize = 13.sp) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("chat_input_field"),
                                shape = RoundedCornerShape(20.dp),
                                maxLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = LilacPrimary
                                )
                            )

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = { isRecordingVoice = true },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(LilacSoft)
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Mic,
                                    contentDescription = "Grabar voz",
                                    tint = PurpleDark
                                )
                            }

                            Spacer(modifier = Modifier.width(4.dp))

                            IconButton(
                                onClick = {
                                    if (inputText.isNotBlank() || selectedMoodTag.isNotBlank()) {
                                        viewModel.sendEmotionalMessage(selectedMoodTag, inputText)
                                        inputText = ""
                                    }
                                },
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(LilacPrimary)
                                    .testTag("send_message_button")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.Send,
                                    contentDescription = "Enviar",
                                    tint = CardSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoodLogBubble(log: MoodLogEntity) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // User Message Card (Right Aligned)
        if (log.userNote.isNotBlank() || log.userMood.isNotBlank()) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Card(
                    shape = RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 4.dp,
                        bottomStart = 20.dp,
                        bottomEnd = 20.dp
                    ),
                    colors = CardDefaults.cardColors(containerColor = LilacSoft)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Sentir: ${log.userMood}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PurpleDark)
                        }
                        if (log.userNote.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(log.userNote, fontSize = 13.sp, color = TextDark)
                        }
                    }
                }
            }
        }

        // Misu Reply Card (Left Aligned)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Card(
                shape = RoundedCornerShape(
                    topStart = 4.dp,
                    topEnd = 20.dp,
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                ),
                colors = CardDefaults.cardColors(containerColor = CardSurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🐾 Misu dice:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = PinkAccent)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = log.misuReply,
                        fontSize = 14.sp,
                        color = TextDark,
                        lineHeight = 19.sp
                    )
                }
            }
        }
    }
}
