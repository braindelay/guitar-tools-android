package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.audio.GuitarAudioEngine
import com.braindelay.guitartools.music.ChordType
import com.braindelay.guitartools.music.CustomChordType
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.OpenChordLibrary
import com.braindelay.guitartools.music.ProgressionViewModel
import com.braindelay.guitartools.music.StandardChordLibrary

private val INTERVAL_LABELS = listOf("R", "b2", "2", "b3", "3", "4", "b5", "5", "b6", "6", "b7", "7")

@Composable
fun ChordScreen(progressionVm: ProgressionViewModel = viewModel()) {
    var selectedNote by remember { mutableStateOf<Note?>(Note.C) }
    var customName by remember { mutableStateOf("Custom") }
    var selectedOffsets by remember { mutableStateOf(setOf(0)) }

    val allVoicings = remember(selectedNote) {
        val note = selectedNote ?: return@remember emptyMap()
        ChordType.entries.associateWith { type -> StandardChordLibrary.getVoicings(note, type) }
    }

    val openChordsMap = remember(selectedNote) {
        val note = selectedNote ?: return@remember emptyMap()
        OpenChordLibrary.getChords(note).associate { it.chordType to it.voicing }
    }

    val customType = remember(customName, selectedOffsets) {
        val sorted = selectedOffsets.sorted()
        CustomChordType(
            label = customName.ifBlank { "Custom" },
            toneOffsets = sorted,
            noteLabels = sorted.map { INTERVAL_LABELS[it] }
        )
    }

    val customVoicings = remember(selectedNote, selectedOffsets) {
        val note = selectedNote ?: return@remember emptyList()
        val sorted = selectedOffsets.sorted()
        if (sorted.size < 2) emptyList()
        else StandardChordLibrary.getVoicingsForCustomType(note, sorted)
    }

    val isPortrait = LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE
    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    val chordListContent: @Composable (Modifier) -> Unit = { modifier ->
        Column(
            modifier = modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            if (selectedNote == null) {
                Text(
                    "Tap a note on the circle of fifths to see chord shapes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                val note = selectedNote!!
                Text(note.displayName, style = MaterialTheme.typography.titleMedium)

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(ChordType.entries) { type ->
                        val openVoicing = openChordsMap[type]
                        val voicings = (allVoicings[type] ?: emptyList()).filter { it != openVoicing }
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    type.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { selectedNote?.let { n -> progressionVm.addChord(n, type) } },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add to progression",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            HorizontalDivider()
                            if (openVoicing == null && voicings.isEmpty()) {
                                Text(
                                    "No voicings available",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    if (openVoicing != null) {
                                        ChordDiagramView(
                                            voicing   = openVoicing,
                                            root      = note,
                                            chordType = type,
                                            onPlay    = { GuitarAudioEngine.playVoicing(openVoicing) }
                                        )
                                    }
                                    voicings.forEach { v ->
                                        ChordDiagramView(
                                            voicing   = v,
                                            root      = note,
                                            chordType = type,
                                            onPlay    = { GuitarAudioEngine.playVoicing(v) }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "Custom",
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = { progressionVm.addChord(note, customType) },
                                    modifier = Modifier.size(24.dp),
                                    enabled = selectedOffsets.size >= 2
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Add to progression",
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                            HorizontalDivider()
                            OutlinedTextField(
                                value = customName,
                                onValueChange = { customName = it },
                                label = { Text("Name", style = MaterialTheme.typography.labelSmall) },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.fillMaxWidth()
                            )
                            for (row in 0..1) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    for (col in 0..5) {
                                        val offset = row * 6 + col
                                        val isRoot = offset == 0
                                        val isSelected = offset in selectedOffsets
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                if (!isRoot) {
                                                    selectedOffsets = if (isSelected)
                                                        selectedOffsets - offset
                                                    else
                                                        selectedOffsets + offset
                                                }
                                            },
                                            label = {
                                                Text(
                                                    INTERVAL_LABELS[offset],
                                                    style = MaterialTheme.typography.labelSmall
                                                )
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                            if (customVoicings.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    customVoicings.forEach { v ->
                                        ChordDiagramView(
                                            voicing   = v,
                                            root      = note,
                                            chordType = customType,
                                            onPlay    = { GuitarAudioEngine.playVoicing(v) }
                                        )
                                    }
                                }
                            } else if (selectedOffsets.size >= 2) {
                                Text(
                                    "No voicings found",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (isPortrait) {
        Column(modifier = Modifier.fillMaxSize().padding(top = topInset + 8.dp)) {
            CircleOfFifthsView(
                selectedNote = selectedNote,
                onNoteSelected = { selectedNote = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.45f)
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
            HorizontalDivider()
            chordListContent(
                Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize().padding(top = topInset + 8.dp)) {
            Column(
                modifier = Modifier
                    .weight(0.38f)
                    .fillMaxHeight()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                CircleOfFifthsView(
                    selectedNote = selectedNote,
                    onNoteSelected = { selectedNote = it },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                )
            }
            VerticalDivider()
            chordListContent(
                Modifier
                    .weight(0.62f)
                    .fillMaxHeight()
            )
        }
    }
}
