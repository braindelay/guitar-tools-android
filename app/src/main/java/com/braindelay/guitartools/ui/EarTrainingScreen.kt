package com.braindelay.guitartools.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.EarChordQuality
import com.braindelay.guitartools.music.EarDrill
import com.braindelay.guitartools.music.EarTraining
import com.braindelay.guitartools.music.EarTrainingViewModel
import com.braindelay.guitartools.music.IntervalMode
import com.braindelay.guitartools.music.Note

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EarTrainingScreen(vm: EarTrainingViewModel = viewModel()) {
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    var settingsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .padding(top = topInset + 12.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "Ear Training",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        // Drill selector
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            EarDrill.entries.forEach { d ->
                FilterChip(
                    selected = vm.drill == d,
                    onClick = { vm.selectDrill(d) },
                    label = { Text(d.displayName) }
                )
            }
        }

        // Score / streak / reset
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "Score ${vm.score} / ${vm.attempts}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Streak ${vm.streak}  ·  Best ${vm.bestStreak}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            TextButton(onClick = { vm.resetSession() }) {
                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("Reset")
            }
        }

        // Question + replay
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    promptText(vm.drill),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { vm.playCurrent() }, enabled = vm.hasQuestion) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                        Spacer(Modifier.width(6.dp))
                        Text("Replay")
                    }
                    FilledTonalButton(onClick = {
                        settingsExpanded = false
                        vm.nextQuestion()
                    }) {
                        Text(if (vm.hasQuestion) "Next question" else "Start")
                    }
                }
                val res = vm.lastResult
                if (res != null) {
                    Text(
                        if (res) "Correct" else "Try again — listen carefully",
                        style = MaterialTheme.typography.titleSmall,
                        color = if (res) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        // Answers
        if (vm.hasQuestion) {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                vm.choices.forEachIndexed { idx, label ->
                    val answered = vm.lastResult != null
                    val isCorrect = answered && vm.correctChoiceIndex == idx
                    val isPickedWrong =
                        answered && vm.pickedChoiceIndex == idx && vm.lastResult == false
                    FilterChip(
                        selected = isCorrect || isPickedWrong,
                        enabled = !answered,
                        onClick = { vm.submit(idx) },
                        label = { Text(label) },
                        colors = if (isCorrect) {
                            FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        } else if (isPickedWrong) {
                            FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        } else FilterChipDefaults.filterChipColors()
                    )
                }
            }
        }

        // Settings (collapsible)
        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    TextButton(onClick = { settingsExpanded = !settingsExpanded }) {
                        Text(if (settingsExpanded) "Hide" else "Show")
                    }
                }
                if (settingsExpanded) {
                    Spacer(Modifier.size(8.dp))
                    when (vm.drill) {
                        EarDrill.INTERVAL -> IntervalSettings(vm)
                        EarDrill.SCALE_DEGREE -> ScaleDegreeSettings(vm)
                        EarDrill.CHORD_QUALITY -> ChordQualitySettings(vm)
                    }
                }
            }
        }
    }
}

private fun promptText(drill: EarDrill): String = when (drill) {
    EarDrill.INTERVAL -> "Two notes will play. Identify the interval between them."
    EarDrill.SCALE_DEGREE -> "Root, then a scale degree. Identify which degree you heard."
    EarDrill.CHORD_QUALITY -> "A chord will play. Identify its quality."
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun IntervalSettings(vm: EarTrainingViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Playback", style = MaterialTheme.typography.labelMedium)
            TextButton(
                onClick = { vm.clearIntervalSettings() },
                enabled = vm.intervalPool.isNotEmpty() || vm.intervalModes.isNotEmpty()
            ) {
                Text("Clear")
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            IntervalMode.entries.forEach { m ->
                FilterChip(
                    selected = m in vm.intervalModes,
                    onClick = { vm.toggleIntervalMode(m) },
                    label = { Text(m.displayName) }
                )
            }
        }
        Text("Interval pool", style = MaterialTheme.typography.labelMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            EarTraining.INTERVAL_SEMITONES.forEachIndexed { i, semi ->
                FilterChip(
                    selected = semi in vm.intervalPool,
                    onClick = { vm.toggleInterval(semi) },
                    label = { Text(EarTraining.INTERVAL_LABELS[i]) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScaleDegreeSettings(vm: EarTrainingViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Root note", style = MaterialTheme.typography.labelMedium)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Note.entries.forEach { n ->
                FilterChip(
                    selected = vm.scaleDegreeRoot == n,
                    onClick = { vm.selectScaleDegreeRoot(n) },
                    label = { Text(n.displayName) }
                )
            }
        }
        Text(
            "Drill always uses the major scale.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ChordQualitySettings(vm: EarTrainingViewModel) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Quality pool", style = MaterialTheme.typography.labelMedium)
            TextButton(
                onClick = { vm.clearChordQualityPool() },
                enabled = vm.chordQualityPool.isNotEmpty()
            ) {
                Text("Clear")
            }
        }
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            EarChordQuality.entries.forEach { q ->
                FilterChip(
                    selected = q in vm.chordQualityPool,
                    onClick = { vm.toggleChordQuality(q) },
                    label = { Text(q.label) }
                )
            }
        }
    }
}
