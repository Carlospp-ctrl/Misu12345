package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class MisuRepository(private val dao: MisuDao) {

    val misuState: Flow<MisuEntity?> = dao.getMisuState()
    val moodLogs: Flow<List<MoodLogEntity>> = dao.getAllMoodLogs()

    suspend fun getOrCreateMisuState(): MisuEntity {
        val existing = dao.getMisuStateSync()
        if (existing != null) return existing
        val defaultState = MisuEntity()
        dao.saveMisuState(defaultState)
        return defaultState
    }

    suspend fun updateMisuState(state: MisuEntity) {
        dao.saveMisuState(state)
    }

    suspend fun updateCustomization(
        furColorId: String? = null,
        outfitId: String? = null,
        accessoryId: String? = null,
        cushionColorId: String? = null,
        roomThemeId: String? = null,
        collarStyle: String? = null
    ) {
        val current = getOrCreateMisuState()
        val updated = current.copy(
            furColorId = furColorId ?: current.furColorId,
            outfitId = outfitId ?: current.outfitId,
            accessoryId = accessoryId ?: current.accessoryId,
            cushionColorId = cushionColorId ?: current.cushionColorId,
            roomThemeId = roomThemeId ?: current.roomThemeId,
            collarStyle = collarStyle ?: current.collarStyle,
            lastInteractionTime = System.currentTimeMillis()
        )
        dao.saveMisuState(updated)
    }

    suspend fun addCareInteractions(
        hungerBoost: Int = 0,
        happinessBoost: Int = 0,
        energyBoost: Int = 0,
        loveBoost: Int = 0,
        starsEarned: Int = 5
    ) {
        val current = getOrCreateMisuState()
        val newHunger = (current.hunger + hungerBoost).coerceIn(0, 100)
        val newHappiness = (current.happiness + happinessBoost).coerceIn(0, 100)
        val newEnergy = (current.energy + energyBoost).coerceIn(0, 100)
        val newLove = (current.love + loveBoost).coerceIn(0, 100)
        val newStars = current.stars + starsEarned

        val newMood = calculateMoodState(newHappiness, newHunger, newEnergy)

        val updated = current.copy(
            hunger = newHunger,
            happiness = newHappiness,
            energy = newEnergy,
            love = newLove,
            stars = newStars,
            moodState = newMood,
            lastInteractionTime = System.currentTimeMillis()
        )
        dao.saveMisuState(updated)
    }

    suspend fun logMoodAndReply(
        userMood: String,
        userNote: String,
        misuReply: String,
        selfCareCompleted: String = "",
        starsEarned: Int = 10
    ) {
        dao.insertMoodLog(
            MoodLogEntity(
                userMood = userMood,
                userNote = userNote,
                misuReply = misuReply,
                selfCareCompleted = selfCareCompleted,
                starsEarned = starsEarned
            )
        )
        addCareInteractions(happinessBoost = 15, loveBoost = 10, starsEarned = starsEarned)
    }

    private fun calculateMoodState(happiness: Int, hunger: Int, energy: Int): String {
        return when {
            hunger < 35 -> "HAMBRIENTO"
            energy < 35 -> "CANSADO"
            happiness < 40 -> "TRISTE"
            happiness > 80 -> "EMOCIONADO"
            else -> "FELIZ"
        }
    }
}
