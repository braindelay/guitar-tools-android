package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.MetronomeViewModel

@Composable
fun MetronomeScreen(vm: MetronomeViewModel = viewModel()) {
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .statusBarsPadding()
                .padding(top = 8.dp, bottom = 12.dp),
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
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 24.dp),
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
                BeatsPerBarSelector(vm)
            }
            PlaybackControls(vm)
        }
    }
}

@Composable
private fun BpmDisplay(vm: MetronomeViewModel) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${vm.bpm}",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold
            )
            BarProgressCircle(vm)
        }
        Text(
            tempoName(vm.bpm),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Pie that fills clockwise from 12 o'clock over the course of one bar. */
@Composable
private fun BarProgressCircle(vm: MetronomeViewModel) {
    var progress by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(vm.isPlaying) {
        if (!vm.isPlaying) {
            progress = 0f
            return@LaunchedEffect
        }
        while (true) {
            withFrameNanos {
                val beatNanos = 60_000_000_000f / vm.bpm
                val withinBeat = ((System.nanoTime() - vm.beatStartNanos) / beatNanos).coerceIn(0f, 1f)
                progress = (vm.currentBeat + withinBeat) / vm.beatsPerBar
            }
        }
    }
    val fill = MaterialTheme.colorScheme.primary
    val track = MaterialTheme.colorScheme.outlineVariant
    val tickOnFill = MaterialTheme.colorScheme.onPrimary
    val tickOnTrack = MaterialTheme.colorScheme.outline
    val beats = vm.beatsPerBar
    Canvas(Modifier.size(48.dp)) {
        val stroke = 2.dp.toPx()
        val radius = (size.minDimension - stroke) / 2
        drawCircle(track, radius = radius, style = Stroke(stroke))
        drawArc(fill, startAngle = -90f, sweepAngle = 360f * progress, useCenter = true)
        // One inward spike per beat, starting at 12 o'clock
        val tickLength = radius * 0.3f
        repeat(beats) { i ->
            val fraction = i.toFloat() / beats
            val angle = 2 * PI * fraction - PI / 2
            val dir = Offset(cos(angle).toFloat(), sin(angle).toFloat())
            drawLine(
                color = if (progress > fraction) tickOnFill else tickOnTrack,
                start = center + dir * radius,
                end = center + dir * (radius - tickLength),
                strokeWidth = 1.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
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
            Text(
                "${vm.beatsPerBar}",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(48.dp)
            )
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
