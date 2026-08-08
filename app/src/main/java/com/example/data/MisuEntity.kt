package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "misu_state")
data class MisuEntity(
    @PrimaryKey val id: Int = 1,
    val name: String = "MiSu",
    val happiness: Int = 85,
    val hunger: Int = 75,
    val energy: Int = 80,
    val love: Int = 90,
    val stars: Int = 50,
    val moodState: String = "FELIZ", // FELIZ, HAMBRIENTO, CANSADO, TRISTE, EMOCIONADO
    
    // Customization / Apariencia
    val furColorId: String = "white",
    val outfitId: String = "hoodie_purple",
    val accessoryId: String = "pink_bow",
    val cushionColorId: String = "purple",
    val roomThemeId: String = "cozy_room",
    val collarStyle: String = "bell_red",
    
    val lastInteractionTime: Long = System.currentTimeMillis()
)

@Entity(tableName = "mood_logs")
data class MoodLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val userMood: String,
    val userNote: String,
    val misuReply: String,
    val selfCareCompleted: String = "",
    val starsEarned: Int = 5
)
