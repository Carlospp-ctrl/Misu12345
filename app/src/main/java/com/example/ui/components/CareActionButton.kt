package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

data class CareActionItem(
    val id: String,
    val title: String,
    val misuCareText: String,
    val selfCareReflection: String,
    val emoji: String,
    val color: Color
)

@Composable
fun CareActionButton(
    item: CareActionItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(4.dp, shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(item.color.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.emoji,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    fontSize = 14.sp
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.misuCareText,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextMuted,
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
