package com.braindelay.guitartools.ui

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.braindelay.guitartools.music.ChordType
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.StandardChordLibrary

@OptIn(ExperimentalMaterial3Api::class)
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
    var selectedChordType by remember { mutableStateOf<ChordType?>(ChordType.DOM7) }
    var menuExpanded by remember { mutableStateOf(false) }

    val voicings = remember(selectedNote, selectedChordType) {
        val note = selectedNote
        val type = selectedChordType
        if (note != null && type != null) StandardChordLibrary.getVoicings(note, type) else emptyList()
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // Left panel: chord type selector + circle of fifths below
        Column(
            modifier = Modifier
                .weight(0.38f)
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Chord Type", style = MaterialTheme.typography.titleSmall)

            ExposedDropdownMenuBox(
                expanded = menuExpanded,
                onExpandedChange = { menuExpanded = it }
            ) {
                OutlinedTextField(
                    value = selectedChordType?.label ?: "Select chord type…",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                )
                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false }
                ) {
                    ChordType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.label) },
                            onClick = {
                                selectedChordType = type
                                menuExpanded = false
                            }
                        )
                    }
                }
            }

            Text("Circle of Fifths", style = MaterialTheme.typography.titleSmall)

            CircleOfFifthsView(
                selectedNote = selectedNote,
                onNoteSelected = { selectedNote = it },
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
        }

        VerticalDivider()

        // Right panel: chord diagrams
        Column(
            modifier = Modifier
                .weight(0.62f)
                .fillMaxHeight()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            when {
                selectedNote == null || selectedChordType == null -> Text(
                    "Select a chord type and tap a note on the circle of fifths to see chord shapes.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                else -> {
                    val note = selectedNote!!
                    val type = selectedChordType!!
                    Text(
                        "${note.displayName} ${type.label}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    HorizontalDivider()
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 80.dp),
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(voicings) { v ->
                            ChordDiagramView(v, note, type)
                        }
                    }
                }
            }
        }
    }
}
