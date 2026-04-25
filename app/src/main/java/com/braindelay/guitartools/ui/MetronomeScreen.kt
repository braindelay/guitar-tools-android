package com.braindelay.guitartools.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.MetronomeViewModel

@Composable
fun MetronomeScreen(vm: MetronomeViewModel = viewModel()) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(top = topInset + 16.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            "Metronome",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "${vm.bpm}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                tempoName(vm.bpm),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Slider(
            value = vm.bpm.toFloat(),
            onValueChange = { vm.setBpm(it.toInt()) },
            valueRange = 20f..300f,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { vm.setBpm(vm.bpm - 5) },
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) { Text("−5") }
            OutlinedButton(
                onClick = { vm.setBpm(vm.bpm - 1) },
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) { Text("−1") }
            Button(
                onClick = { vm.tapTempo() },
                modifier = Modifier.weight(1f)
            ) { Text("Tap Tempo") }
            OutlinedButton(
                onClick = { vm.setBpm(vm.bpm + 1) },
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) { Text("+1") }
            OutlinedButton(
                onClick = { vm.setBpm(vm.bpm + 5) },
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) { Text("+5") }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(vm.beatsPerBar) { i ->
                BeatDot(
                    active = vm.isPlaying && vm.currentBeat == i,
                    isDownbeat = i == 0
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                "Beats per bar",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                listOf(2, 3, 4, 5, 6, 7, 8).forEach { n ->
                    FilterChip(
                        selected = vm.beatsPerBar == n,
                        onClick = { vm.setBeatsPerBar(n) },
                        label = { Text("$n") }
                    )
                }
            }
        }

        Spacer(Modifier.weight(1f))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(onClick = { vm.toggleMute() }) {
                Icon(
                    imageVector = if (vm.isMuted) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null
                )
                Spacer(Modifier.width(6.dp))
                Text(if (vm.isMuted) "Muted" else "Sound on")
            }
            Button(
                onClick = { vm.togglePlay() },
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    imageVector = if (vm.isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(Modifier.width(6.dp))
                Text(if (vm.isPlaying) "Stop" else "Start")
            }
        }
    }
}

@Composable
private fun BeatDot(active: Boolean, isDownbeat: Boolean) {
    val size by animateDpAsState(
        targetValue = if (active) 24.dp else 14.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "dot_size"
    )
    val color by animateColorAsState(
        targetValue = when {
            active && isDownbeat -> MaterialTheme.colorScheme.tertiary
            active               -> MaterialTheme.colorScheme.primary
            else                 -> MaterialTheme.colorScheme.outlineVariant
        },
        label = "dot_color"
    )
    Box(
        modifier = Modifier.size(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .background(color, CircleShape)
        )
    }
}

private fun tempoName(bpm: Int): String = when {
    bpm < 60  -> "Largo"
    bpm < 66  -> "Larghetto"
    bpm < 76  -> "Adagio"
    bpm < 108 -> "Andante"
    bpm < 120 -> "Moderato"
    bpm < 156 -> "Allegro"
    bpm < 176 -> "Vivace"
    bpm < 200 -> "Presto"
    else      -> "Prestissimo"
}
