package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
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
import com.example.data.CustomOption
import com.example.data.CustomizationData
import com.example.data.MisuEntity
import com.example.ui.MisuViewModel
import com.example.ui.components.MisuAvatar
import com.example.ui.components.RoomBackground
import com.example.ui.theme.*

@Composable
fun CustomizationScreen(
    viewModel: MisuViewModel,
    misuState: MisuEntity,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategoryTab by remember { mutableIntStateOf(0) }
    val categories = remember {
        listOf("Pelaje 🐱", "Ropa 🧥", "Accesorios 🎀", "Cojín 🟪", "Cuarto 🛋️", "Collar 🔔")
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
                        text = "Estudio de Apariencia de Misu ✨",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextDark,
                            fontSize = 18.sp
                        )
                    )
                    Text(
                        text = "Elige la ropa y accesorios para Misu y para ti",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                // Stars count badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = YellowWarm,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("⭐", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${misuState.stars}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = TextDark
                            )
                        )
                    }
                }
            }

            // Live Interactive Preview Canvas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(210.dp)
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface.copy(alpha = 0.85f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    MisuAvatar(
                        size = 200.dp,
                        furColorId = misuState.furColorId,
                        outfitId = misuState.outfitId,
                        accessoryId = misuState.accessoryId,
                        cushionColorId = misuState.cushionColorId,
                        collarStyle = misuState.collarStyle,
                        moodState = misuState.moodState,
                        onTapMisu = { viewModel.tapMisu() }
                    )

                    // Self-esteem quote badge overlay
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = PurpleDark.copy(alpha = 0.9f)
                    ) {
                        Text(
                            text = "“Personalizar es decir: ¡Yo elijo mi estilo hoy!” 💖",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = CardSurface,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Scrollable Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedCategoryTab,
                edgePadding = 16.dp,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                categories.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedCategoryTab == index,
                        onClick = { selectedCategoryTab = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedCategoryTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedCategoryTab == index) PurpleDark else TextMuted
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Grid Options
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    when (selectedCategoryTab) {
                        0 -> OptionsGrid(
                            options = CustomizationData.FurColors,
                            selectedId = misuState.furColorId,
                            onSelect = { viewModel.updateCustomization(furColorId = it) }
                        )
                        1 -> OptionsGrid(
                            options = CustomizationData.Outfits,
                            selectedId = misuState.outfitId,
                            onSelect = { viewModel.updateCustomization(outfitId = it) }
                        )
                        2 -> OptionsGrid(
                            options = CustomizationData.Accessories,
                            selectedId = misuState.accessoryId,
                            onSelect = { viewModel.updateCustomization(accessoryId = it) }
                        )
                        3 -> OptionsGrid(
                            options = CustomizationData.Cushions,
                            selectedId = misuState.cushionColorId,
                            onSelect = { viewModel.updateCustomization(cushionColorId = it) }
                        )
                        4 -> OptionsGrid(
                            options = CustomizationData.RoomThemes,
                            selectedId = misuState.roomThemeId,
                            onSelect = { viewModel.updateCustomization(roomThemeId = it) }
                        )
                        5 -> OptionsGrid(
                            options = CustomizationData.Collars,
                            selectedId = misuState.collarStyle,
                            onSelect = { viewModel.updateCustomization(collarStyle = it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionsGrid(
    options: List<CustomOption>,
    selectedId: String,
    onSelect: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(options) { option ->
            val isSelected = option.id == selectedId
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelect(option.id) }
                    .border(
                        width = if (isSelected) 3.dp else 1.dp,
                        color = if (isSelected) LilacPrimary else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) LilacSoft.copy(alpha = 0.5f) else CreamBackground
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = option.iconEmoji,
                            fontSize = 28.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = option.name,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PurpleDark else TextDark,
                                fontSize = 12.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(LilacPrimary),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Check,
                                contentDescription = "Seleccionado",
                                tint = CardSurface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
