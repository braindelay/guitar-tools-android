package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.braindelay.guitartools.music.ProgressionChord
import com.braindelay.guitartools.music.ProgressionTemplate
import com.braindelay.guitartools.music.ProgressionTemplates
import com.braindelay.guitartools.music.ProgressionViewModel
import com.braindelay.guitartools.music.SavedProgression
import com.braindelay.guitartools.music.Scale
import com.braindelay.guitartools.music.ScaleViewModel
import com.braindelay.guitartools.music.StandardChordLibrary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressionScreen(
    vm: ProgressionViewModel = viewModel(),
    scaleVm: ScaleViewModel = viewModel()
) {
    var selectedNote      by remember { mutableStateOf<Note?>(Note.C) }
    var selectedChordType by remember { mutableStateOf(ChordType.MAJOR) }
    val voicings = remember(selectedNote, selectedChordType) {
        val n = selectedNote ?: return@remember emptyList()
        StandardChordLibrary.getVoicings(n, selectedChordType)
    }

    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    if (isLandscape) {
        Row(Modifier.fillMaxSize().padding(top = topInset + 8.dp)) {
            ChordPicker(
                selectedNote      = selectedNote,
                selectedChordType = selectedChordType,
                voicings          = voicings,
                onNoteSelected    = { selectedNote = it },
                onTypeSelected    = { selectedChordType = it },
                onAdd             = { selectedNote?.let { n -> vm.addChord(n, selectedChordType) } },
                modifier          = Modifier.weight(0.62f).fillMaxHeight()
            )
            VerticalDivider()
            ProgressionList(vm, scaleVm.scale, Modifier.weight(0.38f).fillMaxHeight())
        }
    } else {
        Column(Modifier.fillMaxSize().padding(top = topInset + 8.dp)) {
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
            ProgressionList(vm, scaleVm.scale, Modifier.weight(0.5f).fillMaxWidth())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProgressionList(
    vm: ProgressionViewModel,
    scale: Scale,
    modifier: Modifier = Modifier
) {
    var showTemplates by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var savedExpanded by remember { mutableStateOf(false) }
    var loadConfirmTarget by remember { mutableStateOf<SavedProgression?>(null) }
    var renamingTarget by remember { mutableStateOf<SavedProgression?>(null) }

    Column(
        modifier = modifier.padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Progression", style = MaterialTheme.typography.titleMedium)
                TextButton(
                    onClick = { showTemplates = true },
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Text("Templates", style = MaterialTheme.typography.labelSmall)
                }
                TextButton(
                    onClick = { showSaveDialog = true },
                    enabled = vm.progression.isNotEmpty()
                ) {
                    Text("Save", style = MaterialTheme.typography.labelSmall)
                }
            }
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

        if (vm.savedProgressions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(onClick = { savedExpanded = !savedExpanded }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Saved",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (savedExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
            if (savedExpanded) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    vm.savedProgressions.forEach { saved ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .combinedClickable(
                                    onClick = {
                                        if (vm.progression.isEmpty()) {
                                            vm.loadTemplate(saved.chords)
                                            savedExpanded = false
                                        } else loadConfirmTarget = saved
                                    },
                                    onLongClick = { renamingTarget = saved }
                                )
                                .padding(vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                saved.name,
                                modifier = Modifier.weight(1f),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            IconButton(
                                onClick = { vm.deleteSaved(saved.name) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Delete", Modifier.size(14.dp))
                            }
                        }
                    }
                }
                HorizontalDivider()
            }
        }

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
                        IconButton(
                            onClick = { vm.setChordBeats(index, chord.beats - 1) },
                            modifier = Modifier.size(28.dp),
                            enabled = chord.beats > 1
                        ) {
                            Icon(Icons.Default.Remove, null, Modifier.size(14.dp))
                        }
                        Text(
                            "${chord.beats}b",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        IconButton(
                            onClick = { vm.setChordBeats(index, chord.beats + 1) },
                            modifier = Modifier.size(28.dp),
                            enabled = chord.beats < 8
                        ) {
                            Icon(Icons.Default.Add, null, Modifier.size(14.dp))
                        }
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

    if (showTemplates) {
        TemplatesBottomSheet(
            scale = scale,
            onLoad = { vm.loadTemplate(it) },
            onAppend = { vm.appendTemplate(it) },
            onDismiss = { showTemplates = false }
        )
    }

    if (showSaveDialog) {
        var name by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = { Text("Save Progression") },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { vm.saveProgression(name.trim()); showSaveDialog = false },
                    enabled = name.isNotBlank()
                ) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) { Text("Cancel") }
            }
        )
    }

    loadConfirmTarget?.let { target ->
        AlertDialog(
            onDismissRequest = { loadConfirmTarget = null },
            title = { Text("Load \"${target.name}\"?") },
            text = { Text("This will replace the current progression.") },
            confirmButton = {
                TextButton(onClick = { vm.loadTemplate(target.chords); loadConfirmTarget = null; savedExpanded = false }) {
                    Text("Load")
                }
            },
            dismissButton = {
                TextButton(onClick = { loadConfirmTarget = null }) { Text("Cancel") }
            }
        )
    }

    renamingTarget?.let { target ->
        var name by remember(target) { mutableStateOf(target.name) }
        AlertDialog(
            onDismissRequest = { renamingTarget = null },
            title = { Text("Rename") },
            text = {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { vm.renameSaved(target.name, name.trim()); renamingTarget = null },
                    enabled = name.isNotBlank() && name.trim() != target.name
                ) { Text("Rename") }
            },
            dismissButton = {
                TextButton(onClick = { renamingTarget = null }) { Text("Cancel") }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TemplatesBottomSheet(
    scale: Scale,
    onLoad: (List<ProgressionChord>) -> Unit,
    onAppend: (List<ProgressionChord>) -> Unit,
    onDismiss: () -> Unit
) {
    var selected by remember { mutableStateOf<ProgressionTemplate?>(null) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("Templates", style = MaterialTheme.typography.titleMedium)
            Text(
                "Key: ${scale.root.displayName} ${scale.mode.displayName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            ProgressionTemplates.all.forEach { template ->
                val isSelected = template == selected
                FilterChip(
                    selected = isSelected,
                    onClick = { selected = if (isSelected) null else template },
                    label = { Text(template.name) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            selected?.let { template ->
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                val chords = remember(template, scale) { template.resolve(scale) }
                Text(
                    chords.joinToString("  →  ") { "${it.note.displayName} ${it.chordType.label}" },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { onLoad(chords); onDismiss() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Load") }
                    OutlinedButton(
                        onClick = { onAppend(chords); onDismiss() },
                        modifier = Modifier.weight(1f)
                    ) { Text("Append") }
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
    Column(
        modifier = modifier.padding(12.dp),
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
