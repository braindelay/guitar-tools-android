package com.braindelay.guitartools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Piano
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.ProgressionViewModel
import com.braindelay.guitartools.music.ScaleViewModel
import com.braindelay.guitartools.ui.ChordScreen
import com.braindelay.guitartools.ui.ExercisesScreen
import com.braindelay.guitartools.ui.HelpScreen
import com.braindelay.guitartools.ui.ProgressionScreen
import com.braindelay.guitartools.ui.ScaleScreen
import com.braindelay.guitartools.ui.theme.GuitarToolsTheme
import kotlinx.coroutines.delay

enum class AppMode { SCALES, CHORDS, PROGRESSION, EXERCISES, HELP }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GuitarToolsTheme {
                var isLoading by rememberSaveable { mutableStateOf(true) }
                LaunchedEffect(Unit) {
                    delay(2000)
                    isLoading = false
                }
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Crossfade(targetState = isLoading,
                        animationSpec = tween(500), label = "loading_fade") { loading ->
                        if (loading) LoadingScreen() else MainContent()
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    var appMode by rememberSaveable { mutableStateOf(AppMode.SCALES) }
    val progressionVm: ProgressionViewModel = viewModel()
    val scaleVm: ScaleViewModel = viewModel()
    val isFullscreen = scaleVm.isFullscreen
    val hideNavBar = isFullscreen && appMode == AppMode.SCALES
    var navBarVisible by remember { mutableStateOf(true) }

    LaunchedEffect(hideNavBar) { navBarVisible = !hideNavBar }

    LaunchedEffect(progressionVm.playingIndex) {
        val idx = progressionVm.playingIndex
        if (idx != null && idx in progressionVm.progression.indices) {
            val chord = progressionVm.progression[idx]
            scaleVm.setProgressionChord(chord.note, chord.chordType)
        } else {
            scaleVm.clearProgressionChord()
        }
    }

    LaunchedEffect(progressionVm.nextChordIndex) {
        val idx = progressionVm.nextChordIndex
        if (idx != null && idx in progressionVm.progression.indices) {
            val chord = progressionVm.progression[idx]
            scaleVm.setNextProgressionChord(chord.note, chord.chordType)
        } else {
            scaleVm.clearNextProgressionChord()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(1f)
                .pointerInput(hideNavBar) {
                    if (!hideNavBar) return@pointerInput
                    var accumulated = 0f
                    detectVerticalDragGestures(
                        onDragStart = { accumulated = 0f },
                        onVerticalDrag = { _, amount ->
                            accumulated += amount
                            if (accumulated < -100f) {
                                navBarVisible = true
                                accumulated = 0f
                            } else if (accumulated > 100f) {
                                navBarVisible = false
                                accumulated = 0f
                            }
                        }
                    )
                }
        ) {
            when (appMode) {
                AppMode.SCALES      -> ScaleScreen(isProgressionPlaying = progressionVm.playingIndex != null)
                AppMode.CHORDS      -> ChordScreen()
                AppMode.PROGRESSION -> ProgressionScreen()
                AppMode.EXERCISES   -> ExercisesScreen()
                AppMode.HELP        -> HelpScreen()
            }
        }
        AnimatedVisibility(
            visible = navBarVisible,
            enter = slideInVertically { it },
            exit  = slideOutVertically { it }
        ) {
            NavigationBar {
                NavigationBarItem(
                    selected = appMode == AppMode.SCALES,
                    onClick  = { appMode = AppMode.SCALES },
                    icon     = { Icon(Icons.Default.MusicNote, contentDescription = null) },
                    label    = { Text("Scales") }
                )
                NavigationBarItem(
                    selected = appMode == AppMode.CHORDS,
                    onClick  = { appMode = AppMode.CHORDS },
                    icon     = { Icon(Icons.Default.Piano, contentDescription = null) },
                    label    = { Text("Chords") }
                )
                NavigationBarItem(
                    selected = appMode == AppMode.PROGRESSION,
                    onClick  = { appMode = AppMode.PROGRESSION },
                    icon     = { Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = null) },
                    label    = { Text("Progression") }
                )
                NavigationBarItem(
                    selected = appMode == AppMode.EXERCISES,
                    onClick  = { appMode = AppMode.EXERCISES },
                    icon     = { Icon(Icons.Default.FitnessCenter, contentDescription = null) },
                    label    = { Text("Exercises") }
                )
                NavigationBarItem(
                    selected = appMode == AppMode.HELP,
                    onClick  = { appMode = AppMode.HELP },
                    icon     = { Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null) },
                    label    = { Text("Help") }
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.guitar_lessons),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Black.copy(alpha = 0.3f), Color.Black.copy(alpha = 0.7f))
                    )
                ),
            contentAlignment = Alignment.BottomCenter
        ) {
            Text(
                text = "braindelay guitar tools",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 48.dp)
            )
        }
    }
}
