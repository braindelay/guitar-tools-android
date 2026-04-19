package com.braindelay.guitartools.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.MetronomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetronomeScreen(vm: MetronomeViewModel = viewModel()) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Metronome") }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // BPM display
            Text(
                text = "${vm.bpm}",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Bold
            )
            Text("BPM", style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)

            // Beat indicators
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                repeat(vm.beatsPerBar) { i ->
                    val isActive = vm.isPlaying && i == vm.currentBeat
                    val scale by animateFloatAsState(
                        targetValue = if (isActive) 1.4f else 1f,
                        animationSpec = tween(80),
                        label = "beat_scale"
                    )
                    Box(
                        modifier = Modifier
                            .scale(scale)
                            .size(20.dp)
                            .background(
                                color = if (i == 0) MaterialTheme.colorScheme.tertiary
                                        else if (isActive) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.surfaceVariant,
                                shape = CircleShape
                            )
                    )
                }
            }

            // BPM slider
            Slider(
                value = vm.bpm.toFloat(),
                onValueChange = { vm.setBpm(it.toInt()) },
                valueRange = 40f..240f,
            )

            // Fine-tune buttons
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { vm.setBpm(vm.bpm - 10) }) { Text("−10") }
                OutlinedButton(onClick = { vm.setBpm(vm.bpm - 1)  }) { Text("−1")  }
                OutlinedButton(onClick = { vm.setBpm(vm.bpm + 1)  }) { Text("+1")  }
                OutlinedButton(onClick = { vm.setBpm(vm.bpm + 10) }) { Text("+10") }
            }

            // Time signature
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(2, 3, 4, 6).forEach { beats ->
                    FilterChip(
                        selected = vm.beatsPerBar == beats,
                        onClick  = { vm.setBeatsPerBar(beats) },
                        label    = { Text("$beats/4") }
                    )
                }
            }

            // Start / Stop
            Button(
                onClick = { if (vm.isPlaying) vm.stop() else vm.start() },
                modifier = Modifier.size(72.dp),
                shape = CircleShape,
                colors = if (vm.isPlaying)
                    ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                else ButtonDefaults.buttonColors()
            ) {
                Icon(
                    imageVector = if (vm.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (vm.isPlaying) "Stop" else "Start",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(Modifier.height(8.dp))

            // Tap tempo
            OutlinedButton(
                onClick = { vm.tapTempo() },
                modifier = Modifier.width(160.dp).height(56.dp)
            ) {
                Text("Tap Tempo", style = MaterialTheme.typography.titleSmall)
            }
        }
    }
}
