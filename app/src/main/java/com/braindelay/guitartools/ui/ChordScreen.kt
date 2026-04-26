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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.OpenChordLibrary
import com.braindelay.guitartools.music.ProgressionViewModel
import com.braindelay.guitartools.music.StandardChordLibrary

@Composable
fun ChordScreen(progressionVm: ProgressionViewModel = viewModel()) {
    var selectedNote by remember { mutableStateOf<Note?>(Note.C) }

    val allVoicings = remember(selectedNote) {
        val note = selectedNote ?: return@remember emptyMap()
        ChordType.entries.associateWith { type -> StandardChordLibrary.getVoicings(note, type) }
    }

    val openChordsMap = remember(selectedNote) {
        val note = selectedNote ?: return@remember emptyMap()
        OpenChordLibrary.getChords(note).associate { it.chordType to it.voicing }
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
