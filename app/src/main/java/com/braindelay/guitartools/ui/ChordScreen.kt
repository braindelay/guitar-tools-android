package com.braindelay.guitartools.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.braindelay.guitartools.audio.GuitarAudioEngine
import com.braindelay.guitartools.music.ChordType
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.StandardChordLibrary

@Composable
fun ChordScreen() {
    val context = LocalContext.current
    DisposableEffect(Unit) {
        val activity = context as? Activity
        val prev = activity?.requestedOrientation
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        onDispose {
            activity?.requestedOrientation = prev ?: ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }

    var selectedNote by remember { mutableStateOf<Note?>(Note.C) }

    val allVoicings = remember(selectedNote) {
        val note = selectedNote ?: return@remember emptyMap()
        ChordType.entries.associateWith { type -> StandardChordLibrary.getVoicings(note, type) }
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left panel: circle of fifths
        Column(
            modifier = Modifier
                .weight(0.38f)
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Circle of Fifths", style = MaterialTheme.typography.titleSmall)
            CircleOfFifthsView(
                selectedNote = selectedNote,
                onNoteSelected = { selectedNote = it },
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }

        VerticalDivider()

        // Right panel: all chord types
        Column(
            modifier = Modifier
                .weight(0.62f)
                .fillMaxHeight()
                .padding(12.dp),
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
                        val voicings = allVoicings[type] ?: emptyList()
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(type.label, style = MaterialTheme.typography.labelMedium)
                            HorizontalDivider()
                            if (voicings.isEmpty()) {
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
}
