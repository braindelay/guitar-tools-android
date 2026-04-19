package com.braindelay.guitartools.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.R
import com.braindelay.guitartools.music.Mode
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.Scale
import com.braindelay.guitartools.music.ScaleViewModel
import com.braindelay.guitartools.music.TriadType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScaleScreen(vm: ScaleViewModel = viewModel()) {
    val scale = vm.scale
    var expanded by rememberSaveable { mutableStateOf(true) }
    val isFullscreen = vm.isFullscreen

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = { Text("braindelay guitar tools") },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (expanded) "Close" else "Choose scale",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                                                  else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (expanded) "Collapse options" else "Expand options"
                                )
                            }
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(if (isFullscreen) androidx.compose.foundation.layout.PaddingValues(0.dp) else innerPadding)
                .then(if (isFullscreen) Modifier.fillMaxSize() else Modifier.fillMaxWidth())
                .then(if (!isFullscreen) Modifier.verticalScroll(rememberScrollState()) else Modifier)
                .padding(horizontal = if (isFullscreen) 0.dp else 16.dp, vertical = if (isFullscreen) 0.dp else 12.dp),
            verticalArrangement = if (isFullscreen) Arrangement.Center else Arrangement.spacedBy(20.dp)
        ) {
            if (!isFullscreen) {
                // Hero Image Banner
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Box {
                        Image(
                            painter = painterResource(id = R.drawable.guitar_lessons),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        // Gradient overlay for text readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                        startY = 100f
                                    )
                                ),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Master Your Fretboard",
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White
                                )
                                Text(
                                    "Visualise scales and diatonic chords",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }

                AnimatedVisibility(visible = expanded) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Root & Scale", style = MaterialTheme.typography.titleMedium)
                                
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(Note.entries) { note ->
                                        FilterChip(
                                            selected = note == vm.selectedNote,
                                            onClick = { vm.selectNote(note) },
                                            label = { Text(note.displayName) }
                                        )
                                    }
                                }

                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Mode.entries.forEach { mode ->
                                        FilterChip(
                                            selected = mode == vm.selectedMode,
                                            onClick = { vm.selectMode(mode) },
                                            label = { Text(mode.displayName) }
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Diatonic Chords", style = MaterialTheme.typography.titleMedium)
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    vm.diatonicChords.forEach { chord ->
                                        AssistChip(
                                            onClick = { },
                                            label = { Text(chord) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (isFullscreen) {
                BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                    val scaleFactor = maxHeight / 216.dp
                    HorizontalScrollableFretboard(vm, scaleFactor = scaleFactor)
                    ElevatedButton(
                        onClick = { vm.exitFullscreen() },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    ) {
                        Text("Go Back")
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Fretboard", style = MaterialTheme.typography.headlineSmall)
                            Text(
                                "Highlighting: ${vm.selectedNote.displayName} ${vm.selectedMode.displayName}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (vm.selectedFretPosition != null || vm.selectedTriadType != null) {
                            ElevatedButton(onClick = { vm.clearFretSelection() }) {
                                Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.width(8.dp))
                                Text("Clear")
                            }
                        }
                    }

                    vm.triadSummary?.let { summary ->
                        OutlinedCard(
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                            )
                        ) {
                            Text(
                                summary,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier
                                .width(120.dp)
                                .height(216.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            TriadType.entries.forEach { triad ->
                                val isDiatonic = vm.isDiatonic(triad)
                                FilterChip(
                                    selected = triad == vm.selectedTriadType,
                                    onClick = {
                                        if (vm.selectedTriadType == triad) vm.clearTriadType()
                                        else vm.selectTriadType(triad)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    leadingIcon = if (isDiatonic) {
                                        { Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp)) }
                                    } else null,
                                    label = {
                                        Text(
                                            triad.label,
                                            style = MaterialTheme.typography.labelSmall,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                )
                            }
                        }

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            HorizontalScrollableFretboard(vm)
                            Text(
                                "Click fretboard to zoom",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                            )
                        }
                    }
                }
            }
            
            if (!isFullscreen) {
                Spacer(Modifier.height(40.dp))
            }
        }
    }

}

@Composable
fun HorizontalScrollableFretboard(vm: ScaleViewModel, scaleFactor: Float = 1f) {
    val scrollState = rememberScrollState()
    androidx.compose.foundation.layout.Box(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState)
    ) {
        FretboardView(
            scale = vm.scale,
            positions = vm.fretPositions,
            selectedPosition = vm.selectedFretPosition,
            triadNotes = vm.triadNotes,
            onFretTapped = { pos -> vm.selectFretPosition(pos) },
            onOtherTapped = { vm.enterFullscreen() },
            scaleFactor = scaleFactor
        )
    }
}
