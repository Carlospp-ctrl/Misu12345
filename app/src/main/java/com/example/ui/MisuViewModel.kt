package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.ui.components.CareActionItem
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MisuViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: MisuRepository = MisuRepository(
        MisuDatabase.getDatabase(application).misuDao()
    )

    val misuState: StateFlow<MisuEntity> = repository.misuState
        .map { it ?: MisuEntity() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MisuEntity()
        )

    val moodLogs: StateFlow<List<MoodLogEntity>> = repository.moodLogs
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Current daily affirmation
    private val affirmations = listOf(
        "Eres suficiente exactamente tal como eres hoy 💖",
        "Está bien pedir ayuda y tomarte un descanso 🐾",
        "Hoy hiciste lo mejor que pudiste, y eso es súper valioso ✨",
        "Cuida bien de mí y siempre estaré aquí para acompañarte 🐱",
        "Tus emociones importan y mereces tratarte con amor 🌸",
        "Trátate hoy con la misma ternura con la que cuidas de Misu 💖",
        "Cada pequeño logro cuenta, celebra tu camino ✨"
    )

    private val _currentAffirmation = MutableStateFlow(affirmationTextRandom())
    val currentAffirmation: StateFlow<String> = _currentAffirmation.asStateFlow()

    // Active Care Dialog State
    private val _activeCareDialog = MutableStateFlow<CareActionItem?>(null)
    val activeCareDialog: StateFlow<CareActionItem?> = _activeCareDialog.asStateFlow()

    // Self Care Toast Message
    private val _selfCareMessage = MutableStateFlow<String?>(null)
    val selfCareMessage: StateFlow<String?> = _selfCareMessage.asStateFlow()

    // Chat State
    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getOrCreateMisuState()
        }
    }

    private fun affirmationTextRandom(): String {
        return affirmations.random()
    }

    fun nextAffirmation() {
        var next = affirmations.random()
        while (next == _currentAffirmation.value) {
            next = affirmations.random()
        }
        _currentAffirmation.value = next
    }

    fun openCareDialog(item: CareActionItem) {
        _activeCareDialog.value = item
    }

    fun closeCareDialog() {
        _activeCareDialog.value = null
    }

    fun dismissSelfCareMessage() {
        _selfCareMessage.value = null
    }

    fun performCareAction(actionId: String) {
        viewModelScope.launch {
            when (actionId) {
                "feed" -> {
                    repository.addCareInteractions(hungerBoost = 25, happinessBoost = 10, starsEarned = 5)
                    _selfCareMessage.value = "🍎 ¡Misu comió feliz! Recordatorio para ti: ¿Ya tomaste agüita o comiste algo rico hoy? 💧"
                }
                "sleep" -> {
                    repository.addCareInteractions(energyBoost = 35, loveBoost = 10, starsEarned = 5)
                    _selfCareMessage.value = "🌙 Arropaste a Misu para la siesta. Recuerda regalarte una pausa de respiración profunda."
                }
                "dress" -> {
                    repository.addCareInteractions(happinessBoost = 15, loveBoost = 10, starsEarned = 5)
                    _selfCareMessage.value = "👗 ¡Misu se ve radiante! Reto de hoy: Elige 1 prenda o detalle que te haga sentir cómodo/a y especial ✨."
                }
                "pamper" -> {
                    repository.addCareInteractions(happinessBoost = 20, loveBoost = 15, starsEarned = 5)
                    _selfCareMessage.value = "✨ ¡Acariciaste a Misu! Micro-hábito: Estira tus brazos, sonríe y repite: 'Merezco paz'."
                }
                "play" -> {
                    repository.addCareInteractions(happinessBoost = 30, energyBoost = -5, starsEarned = 10)
                    _selfCareMessage.value = "🧶 ¡Misu persiguió el ovillo de lana! Pausa activa: Muévete 2 minutos o haz un garabato divertido 🎨."
                }
                "accessories" -> {
                    repository.addCareInteractions(happinessBoost = 15, starsEarned = 5)
                    _selfCareMessage.value = "🎀 ¡Qué hermoso se ve Misu! 'Yo también merezco arreglarme y brillar a mi manera'."
                }
            }
            closeCareDialog()
        }
    }

    fun updateCustomization(
        furColorId: String? = null,
        outfitId: String? = null,
        accessoryId: String? = null,
        cushionColorId: String? = null,
        roomThemeId: String? = null,
        collarStyle: String? = null
    ) {
        viewModelScope.launch {
            repository.updateCustomization(
                furColorId = furColorId,
                outfitId = outfitId,
                accessoryId = accessoryId,
                cushionColorId = cushionColorId,
                roomThemeId = roomThemeId,
                collarStyle = collarStyle
            )
        }
    }

    fun sendEmotionalMessage(moodTag: String, noteText: String) {
        if (noteText.isBlank() && moodTag.isBlank()) return

        viewModelScope.launch {
            _isChatLoading.value = true
            val reply = GeminiService.generateEmpatheticResponse(noteText, moodTag)
            _isChatLoading.value = false

            repository.logMoodAndReply(
                userMood = moodTag,
                userNote = noteText,
                misuReply = reply,
                starsEarned = 10
            )

            _selfCareMessage.value = "💖 Misu escuchó tu sentir. ¡Ganaste +10 estrellitas por expresarte!"
        }
    }

    fun tapMisu() {
        viewModelScope.launch {
            repository.addCareInteractions(happinessBoost = 5, loveBoost = 5, starsEarned = 1)
        }
    }
}
