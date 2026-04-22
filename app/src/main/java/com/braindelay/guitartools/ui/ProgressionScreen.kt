package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.music.ChordType
import com.braindelay.guitartools.music.ChordVoicing
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.ProgressionViewModel
import com.braindelay.guitartools.music.StandardChordLibrary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionScreen(vm: ProgressionViewModel = viewModel()) {
    var selectedNote      by remember { mutableStateOf<Note?>(Note.C) }
    var selectedChordType by remember { mutableStateOf(ChordType.MAJOR) }
    val voicings = remember(selectedNote, selectedChordType) {
        val n = selectedNote ?: return@remember emptyList()
        StandardChordLibrary.getVoicings(n, selectedChordType)
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(Modifier.fillMaxSize()) {
            ProgressionList(vm, Modifier.weight(0.38f).fillMaxHeight())
            VerticalDivider()
            ChordPicker(
                selectedNote      = selectedNote,
                selectedChordType = selectedChordType,
                voicings          = voicings,
                onNoteSelected    = { selectedNote = it },
                onTypeSelected    = { selectedChordType = it },
                onAdd             = { selectedNote?.let { n -> vm.addChord(n, selectedChordType) } },
                modifier          = Modifier.weight(0.62f).fillMaxHeight()
            )
        }
    } else {
        Column(Modifier.fillMaxSize()) {
            ChordPicker(
                selectedNote      = selectedNote,
                selectedChordType = selectedChordType,
                voicings          = voicings,
                onNoteSelected    = { selectedNote = it },
                onTypeSelected    = { selectedChordType = it },
                onAdd             = { selectedNote?.let { n -> vm.addChord(n, selectedChordType) } },
                modifier          = Modifier.weight(0.5f).fillMaxWidth()
            )
            HorizontalDivider()
            ProgressionList(vm, Modifier.weight(0.5f).fillMaxWidth())
        }
    }
}

@Composable
private fun ProgressionList(vm: ProgressionViewModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Progression", style = MaterialTheme.typography.titleMedium)
            Row {
                IconToggleButton(
                    checked = vm.isMuted,
                    onCheckedChange = { vm.toggleMute() }
                ) {
                    Icon(
                        imageVector = if (vm.isMuted) Icons.AutoMirrored.Filled.VolumeOff
                                      else Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = if (vm.isMuted) "Unmute" else "Mute",
                        tint = if (vm.isMuted) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = { if (vm.isPlaying) vm.stopPlayback() else vm.playProgression() }
                ) {
                    Icon(
                        imageVector = if (vm.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (vm.isPlaying) "Stop" else "Play"
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("${vm.chordBpm} BPM", style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.width(52.dp))
            Slider(
                value = vm.chordBpm.toFloat(),
                onValueChange = { vm.setChordBpm(it.toInt()) },
                valueRange = 20f..240f,
                modifier = Modifier.weight(1f)
            )
        }

        HorizontalDivider()

        if (vm.progression.isEmpty()) {
            Text(
                "Select a note and chord type, then tap Add.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(vm.progression) { index, chord ->
                val isPlaying = vm.playingIndex == index
                Card(
                    colors = if (isPlaying)
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    else CardDefaults.cardColors()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${chord.note.displayName} ${chord.chordType.label}",
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        IconButton(onClick = { vm.moveChordLeft(index) },
                            modifier = Modifier.size(28.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, Modifier.size(14.dp))
                        }
                        IconButton(onClick = { vm.moveChordRight(index) },
                            modifier = Modifier.size(28.dp)) {
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, null, Modifier.size(14.dp))
                        }
                        IconButton(onClick = { vm.removeChord(index) },
                            modifier = Modifier.size(28.dp)) {
                            Icon(Icons.Default.Close, null, Modifier.size(14.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChordPicker(
    selectedNote: Note?,
    selectedChordType: ChordType,
    voicings: List<ChordVoicing>,
    onNoteSelected: (Note?) -> Unit,
    onTypeSelected: (ChordType) -> Unit,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier
                .width(90.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Type", style = MaterialTheme.typography.labelSmall)
            ChordType.entries.forEach { type ->
                FilterChip(
                    selected = type == selectedChordType,
                    onClick  = { onTypeSelected(type) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(type.label, style = MaterialTheme.typography.labelSmall,
                            maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "${selectedNote?.displayName ?: "–"} ${selectedChordType.label}",
                    style = MaterialTheme.typography.titleSmall
                )
                Button(onClick = onAdd, enabled = selectedNote != null) {
                    Icon(Icons.Default.Add, null, Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Add")
                }
            }

            Row(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CircleOfFifthsView(
                    selectedNote = selectedNote,
                    onNoteSelected = onNoteSelected,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )

                if (voicings.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .width(80.dp)
                            .fillMaxHeight()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Voicings", style = MaterialTheme.typography.labelSmall)
                        voicings.take(6).forEach { v ->
                            selectedNote?.let { n ->
                                ChordDiagramView(
                                    voicing   = v,
                                    root      = n,
                                    chordType = selectedChordType,
                                    onPlay    = {
                                        com.braindelay.guitartools.audio.GuitarAudioEngine.playVoicing(v)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
