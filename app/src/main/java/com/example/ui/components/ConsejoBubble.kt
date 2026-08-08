package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FormatQuote
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun ConsejoBubble(
    modifier: Modifier = Modifier,
    affirmationText: String,
    onRefresh: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, shape = RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .clickable { onRefresh() },
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.Favorite,
                        contentDescription = null,
                        tint = PinkAccent,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CONSEJO DE MISU",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PurpleDark,
                            letterSpacing = 1.sp
                        )
                    )
                }

                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = "Cambiar afirmación",
                        tint = PurpleDark.copy(alpha = 0.6f),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "“$affirmationText”",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextDark,
                    fontSize = 15.sp,
                    lineHeight = 21.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "✨ Toca para leer otra afirmación",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp
                )
            )
        }
    }
}
