package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.MisuViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MiSuTheme

enum class Screen {
    HOME,
    CHAT,
    CUSTOMIZATION,
    CARE_MENU,
    HISTORY
}

class MainActivity : ComponentActivity() {

    private val viewModel: MisuViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MiSuTheme {
                var currentScreen by remember { mutableStateOf(Screen.HOME) }
                val misuState by viewModel.misuState.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Crossfade(
                        targetState = currentScreen,
                        modifier = Modifier.padding(innerPadding),
                        label = "screen_transition"
                    ) { screen ->
                        when (screen) {
                            Screen.HOME -> HomeScreen(
                                viewModel = viewModel,
                                misuState = misuState,
                                onNavigateToChat = { currentScreen = Screen.CHAT },
                                onNavigateToCustomization = { currentScreen = Screen.CUSTOMIZATION },
                                onNavigateToCareMenu = { currentScreen = Screen.CARE_MENU },
                                onNavigateToHistory = { currentScreen = Screen.HISTORY }
                            )
                            Screen.CHAT -> ChatScreen(
                                viewModel = viewModel,
                                misuState = misuState,
                                onBack = { currentScreen = Screen.HOME }
                            )
                            Screen.CUSTOMIZATION -> CustomizationScreen(
                                viewModel = viewModel,
                                misuState = misuState,
                                onBack = { currentScreen = Screen.HOME }
                            )
                            Screen.CARE_MENU -> CareMenuScreen(
                                viewModel = viewModel,
                                misuState = misuState,
                                onBack = { currentScreen = Screen.HOME }
                            )
                            Screen.HISTORY -> HistoryScreen(
                                viewModel = viewModel,
                                misuState = misuState,
                                onBack = { currentScreen = Screen.HOME }
                            )
                        }
                    }
                }
            }
        }
    }
}
