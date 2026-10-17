package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.MetronomeViewModel

@Composable
fun MetronomeScreen(vm: MetronomeViewModel = viewModel()) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = topInset + 8.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BpmDisplay(vm)
                Slider(
                    value = vm.bpm.toFloat(),
                    onValueChange = { vm.setBpm(it.toInt()) },
                    valueRange = 20f..300f,
                    modifier = Modifier.fillMaxWidth()
                )
                BpmAdjustRow(vm)
                BeatsPerBarSelector(vm)
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BeatDots(vm)
                TapTempoButton(
                    vm,
                    Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
                PlaybackControls(vm)
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = topInset + 16.dp, bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    "Metronome",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                BpmDisplay(vm)
                Slider(
                    value = vm.bpm.toFloat(),
                    onValueChange = { vm.setBpm(it.toInt()) },
                    valueRange = 20f..300f,
                    modifier = Modifier.fillMaxWidth()
                )
                TapTempoButton(vm, Modifier.fillMaxWidth())
                BpmAdjustRow(vm)
                BeatDots(vm)
                BeatsPerBarSelector(vm)
            }
            PlaybackControls(vm)
        }
    }
}

@Composable
private fun BpmDisplay(vm: MetronomeViewModel) {
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
}

@Composable
private fun TapTempoButton(vm: MetronomeViewModel, modifier: Modifier = Modifier) {
    Button(onClick = { vm.tapTempo() }, modifier = modifier) {
        Text("Tap Tempo")
    }
}

@Composable
private fun BpmAdjustRow(vm: MetronomeViewModel) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(-5, -1, 1, 5).forEach { delta ->
            OutlinedButton(
                onClick = { vm.setBpm(vm.bpm + delta) },
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(horizontal = 4.dp)
            ) { Text(if (delta < 0) "−${-delta}" else "+$delta") }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BeatDots(vm: MetronomeViewModel) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(vm.beatsPerBar) { i ->
            BeatDot(
                active = vm.isPlaying && vm.currentBeat == i,
                isDownbeat = i == 0
            )
        }
    }
}

@Composable
private fun BeatsPerBarSelector(vm: MetronomeViewModel) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            "Beats per bar",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = { vm.setBeatsPerBar(vm.beatsPerBar - 1) },
                enabled = vm.beatsPerBar > 1,
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.width(48.dp)
            ) { Text("−1") }
            val choices = if (vm.beatsPerBar > 4) listOf(1, 2, 3, 4, vm.beatsPerBar) else listOf(1, 2, 3, 4)
            choices.forEach { n ->
                FilterChip(
                    selected = vm.beatsPerBar == n,
                    onClick = { vm.setBeatsPerBar(n) },
                    label = { Text("$n") }
                )
            }
            OutlinedButton(
                onClick = { vm.setBeatsPerBar(vm.beatsPerBar + 1) },
                enabled = vm.beatsPerBar < 12,
                contentPadding = PaddingValues(horizontal = 8.dp),
                modifier = Modifier.width(48.dp)
            ) { Text("+1") }
        }
    }
}

@Composable
private fun PlaybackControls(vm: MetronomeViewModel) {
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
            active -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
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
    bpm < 60 -> "Largo"
    bpm < 66 -> "Larghetto"
    bpm < 76 -> "Adagio"
    bpm < 108 -> "Andante"
    bpm < 120 -> "Moderato"
    bpm < 156 -> "Allegro"
    bpm < 176 -> "Vivace"
    bpm < 200 -> "Presto"
    else -> "Prestissimo"
}
