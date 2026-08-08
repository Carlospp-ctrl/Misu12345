package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
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
import com.example.ui.components.RoomBackground
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(
    viewModel: MisuViewModel,
    misuState: MisuEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val moodLogs by viewModel.moodLogs.collectAsState()
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()) }

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

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Diario de Estrellitas y Emociones 🌟",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 17.sp
                        )
                    )
                    Text(
                        text = "Tu historial de autocuidado y acompañamiento",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    )
                }
            }

            // Stars summary banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = YellowWarm.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("⭐ Estrellitas Ganadas", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                        Text("${misuState.stars}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = PurpleDark)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💖 Interacciones de Amor", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextDark)
                        Text("${moodLogs.size}", fontWeight = FontWeight.ExtraBold, fontSize = 24.sp, color = PinkAccent)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (moodLogs.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("📜", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Aún no tienes registros guardados.",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Visita la sección 'JUGAMOS' o realiza acciones de cuidado para ver tus avances de autoestima aquí.",
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
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(moodLogs) { log ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = CardSurface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Estado: ${log.userMood}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PurpleDark
                                        )
                                    )

                                    Text(
                                        text = dateFormat.format(Date(log.timestamp)),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextMuted,
                                            fontSize = 10.sp
                                        )
                                    )
                                }

                                if (log.userNote.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "“${log.userNote}”",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextDark,
                                            fontSize = 12.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Card(
                                    colors = CardDefaults.cardColors(containerColor = LilacSoft.copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = "🐾 Misu: ${log.misuReply}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextDark,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 12.sp
                                        ),
                                        modifier = Modifier.padding(10.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
