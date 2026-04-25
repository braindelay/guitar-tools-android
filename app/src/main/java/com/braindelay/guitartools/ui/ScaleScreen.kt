package com.braindelay.guitartools.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.braindelay.guitartools.R
import com.braindelay.guitartools.audio.GuitarAudioEngine
import com.braindelay.guitartools.music.Mode
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.Scale
import com.braindelay.guitartools.music.ScaleViewModel
import com.braindelay.guitartools.music.TriadType

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ScaleScreen(vm: ScaleViewModel = viewModel(), isProgressionPlaying: Boolean = false) {
    val scale = vm.scale
    var expanded by rememberSaveable { mutableStateOf(!isProgressionPlaying) }
    val isFullscreen = vm.isFullscreen

    val chordSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showChordSheet by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    // True only when the progression itself triggered fullscreen; survives rotation.
    var progressionEnteredFullscreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(vm.selectedFretPosition) {
        if (vm.selectedFretPosition != null) showChordSheet = true
    }

    LaunchedEffect(isProgressionPlaying) {
        if (isProgressionPlaying) {
            expanded = false
            vm.enterFullscreen()
            progressionEnteredFullscreen = true
        } else if (progressionEnteredFullscreen) {
            vm.exitFullscreen()
            progressionEnteredFullscreen = false
        }
    }

    Scaffold(
        topBar = {
            if (!isFullscreen) {
                TopAppBar(
                    title = { Text("braindelay guitar tools") },
                    actions = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Left-handed toggle
                            IconToggleButton(
                                checked = vm.isLeftHanded,
                                onCheckedChange = { vm.toggleLeftHanded() }
                            ) {
                                Icon(
                                    Icons.Default.SwapHoriz,
                                    contentDescription = "Left-handed mode",
                                    tint = if (vm.isLeftHanded) MaterialTheme.colorScheme.primary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            // Note-name label toggle
                            IconToggleButton(
                                checked = vm.showNoteNames,
                                onCheckedChange = { vm.toggleLabelMode() }
                            ) {
                                Icon(
                                    Icons.Default.TextFields,
                                    contentDescription = "Show note names",
                                    tint = if (vm.showNoteNames) MaterialTheme.colorScheme.primary
                                           else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                text = if (expanded) "Close" else "Choose scale",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            IconButton(onClick = { expanded = !expanded }) {
                                Icon(
                                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp
                                                  else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (expanded) "Collapse" else "Expand"
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
                .padding(horizontal = if (isFullscreen) 0.dp else 16.dp,
                         vertical   = if (isFullscreen) 0.dp else 12.dp),
            verticalArrangement = if (isFullscreen) Arrangement.Center else Arrangement.spacedBy(20.dp)
        ) {
            if (!isFullscreen) {
                AnimatedVisibility(visible = expanded) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Hero image
                        OutlinedCard(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                            Box {
                                Image(
                                    painter = painterResource(id = R.drawable.guitar_lessons),
                                    contentDescription = null,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
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
                                        Text("Master Your Fretboard",
                                            style = MaterialTheme.typography.headlineSmall, color = Color.White)
                                        Text("Visualise scales and diatonic chords",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        }

                        OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text("Root & Scale", style = MaterialTheme.typography.titleMedium)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    CircleOfFifthsView(
                                        selectedNote = vm.selectedNote,
                                        onNoteSelected = { vm.selectNote(it) },
                                        modifier = Modifier.size(160.dp)
                                    )
                                    FlowRow(
                                        modifier = Modifier.weight(1f),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalArrangement   = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Mode.entries.forEach { mode ->
                                            FilterChip(
                                                selected = mode == vm.selectedMode,
                                                onClick  = { vm.selectMode(mode) },
                                                label    = { Text(mode.displayName) }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }

            if (isFullscreen) {
                val isPortrait = LocalConfiguration.current.orientation != Configuration.ORIENTATION_LANDSCAPE
                FullscreenContent(vm, isPortrait)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Title row
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Play button when chord/arpeggio is active
                            val triad = vm.triadNotes
                            if (triad != null) {
                                val pos = vm.selectedFretPosition
                                val triadType = vm.selectedTriadType
                                if (pos != null && triadType != null) {
                                    IconButton(onClick = {
                                        val rootMidi = GuitarAudioEngine.midiFromPosition(pos.string, pos.fret)
                                        GuitarAudioEngine.playMidis(triadType.toneOffsets.map { rootMidi + it })
                                    }) {
                                        Icon(Icons.Default.PlayArrow, contentDescription = "Play chord")
                                    }
                                }
                            }
                            if (vm.selectedFretPosition != null || vm.selectedTriadType != null
                                || vm.arpeggioChordIndex != null) {
                                ElevatedButton(onClick = {
                                    vm.clearFretSelection()
                                    vm.arpeggioChordIndex?.let { vm.selectArpeggioChord(it) }
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = null,
                                        modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(8.dp))
                                    Text("Clear")
                                }
                            }
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
                            vm.diatonicChords.forEachIndexed { idx, chord ->
                                FilterChip(
                                    selected = vm.arpeggioChordIndex == idx,
                                    onClick  = { vm.selectArpeggioChord(idx) },
                                    modifier = Modifier.fillMaxWidth(),
                                    label    = {
                                        Text(
                                            chord,
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
                                "Tap a note to pick chord type · pinch to zoom",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            if (!isFullscreen) Spacer(Modifier.height(40.dp))
        }
    }

    val selectedPos = vm.selectedFretPosition
    if (showChordSheet && selectedPos != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showChordSheet = false
                vm.clearFretSelection()
            },
            sheetState = chordSheetState
        ) {
            val rootNote = vm.noteAt(selectedPos)
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "${rootNote.displayName} — choose chord type",
                    style = MaterialTheme.typography.titleMedium
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement   = Arrangement.spacedBy(8.dp)
                ) {
                    TriadType.entries.forEach { triad ->
                        val isDiatonic = vm.isDiatonic(triad)
                        FilterChip(
                            selected = triad == vm.selectedTriadType,
                            onClick  = {
                                if (vm.selectedTriadType == triad) {
                                    vm.clearTriadType()
                                } else {
                                    vm.selectTriadType(triad)
                                    coroutineScope.launch {
                                        chordSheetState.hide()
                                    }.invokeOnCompletion {
                                        if (!chordSheetState.isVisible) showChordSheet = false
                                    }
                                }
                            },
                            leadingIcon = if (isDiatonic) {
                                { Icon(Icons.Default.Info, null, Modifier.size(14.dp)) }
                            } else null,
                            label = { Text(triad.label) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FullscreenContent(vm: ScaleViewModel, isPortrait: Boolean) {
    // BoxWithConstraints owns its own scope — no ColumnScope leaks in from the call site,
    // so AnimatedVisibility resolves to the top-level overload rather than ColumnScope's.
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val scaleFactor = if (isPortrait) maxWidth / 216.dp else maxHeight / 216.dp
        var chordDrawerOpen by remember { mutableStateOf(false) }

        HorizontalScrollableFretboard(vm, scaleFactor = scaleFactor, portraitRotated = isPortrait)

        // Thin left-edge strip — rightward drag opens the chord drawer
        if (!chordDrawerOpen) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .fillMaxHeight()
                    .align(Alignment.TopStart)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount > 0f) chordDrawerOpen = true
                        }
                    }
            )
        }

        // Scrim — tap anywhere outside the open drawer to close it
        if (chordDrawerOpen) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) { detectTapGestures { chordDrawerOpen = false } }
            )
        }

        // Chord drawer panel — slides in from the left
        AnimatedVisibility(
            visible  = chordDrawerOpen,
            enter    = slideInHorizontally { -it },
            exit     = slideOutHorizontally { -it },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(160.dp)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.95f))
                    .padding(12.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures { _, dragAmount ->
                            if (dragAmount < 0f) chordDrawerOpen = false
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    "Diatonic Chords",
                    style    = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(vm.diatonicChords) { idx, chord ->
                        FilterChip(
                            selected = vm.arpeggioChordIndex == idx,
                            onClick  = { vm.selectArpeggioChord(idx); chordDrawerOpen = false },
                            modifier = Modifier.fillMaxWidth(),
                            label    = {
                                Text(
                                    chord,
                                    style    = MaterialTheme.typography.labelSmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        )
                    }
                }
            }
        }

        val chordLabel = vm.progressionChord?.let { (note, type) -> "${note.displayName} ${type.label}" }
        if (chordLabel != null) {
            Text(
                text  = chordLabel,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                modifier = if (isPortrait) {
                    // Fretboard is rotated 90° CW; player tilts phone so portrait-right becomes
                    // their visual top (above the strings). Rotate the label to match.
                    Modifier
                        .align(Alignment.CenterEnd)
                        .rotateWithLayout(90f)
                        .background(Color.Black.copy(alpha = 0.45f), MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                } else {
                    Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 8.dp)
                        .background(Color.Black.copy(alpha = 0.45f), MaterialTheme.shapes.small)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                }
            )
        }
    }
}

@Composable
fun HorizontalScrollableFretboard(vm: ScaleViewModel, scaleFactor: Float = 1f, portraitRotated: Boolean = false) {
    val scrollState = rememberScrollState()
    val containerModifier = if (portraitRotated) {
        Modifier.rotatePortraitToLandscape().horizontalScroll(scrollState)
    } else {
        Modifier.fillMaxWidth().horizontalScroll(scrollState)
    }
    Box(modifier = containerModifier.pointerInput(vm.isFullscreen) {
        // Non-consuming pinch observer: never calls consume(), so horizontalScroll
        // continues to receive drag events. accumulatedZoom resets per-gesture.
        awaitEachGesture {
            var accumulatedZoom = 1f
            while (true) {
                val event = awaitPointerEvent()
                accumulatedZoom *= event.calculateZoom()
                if (!vm.isFullscreen && accumulatedZoom > 1.2f) {
                    vm.enterFullscreen()
                    accumulatedZoom = 1f
                } else if (vm.isFullscreen && accumulatedZoom < 0.8f) {
                    vm.exitFullscreen()
                    accumulatedZoom = 1f
                }
                if (event.changes.none { it.pressed }) break
            }
        }
    }) {
        FretboardView(
            scale                = vm.scale,
            positions            = vm.fretPositions,
            selectedPosition     = vm.selectedFretPosition,
            triadNotes           = vm.activeOverlay,
            isScaleDegreeOverlay = vm.triadNotes == null,
            nextChordNotes       = vm.nextProgressionArpeggioNotes,
            onFretTapped         = { pos -> vm.selectFretPosition(pos) },
            scaleFactor          = scaleFactor,
            isLeftHanded         = vm.isLeftHanded,
            showNoteNames        = vm.showNoteNames
        )
    }
}

// Rotates a composable by [degrees] and swaps the reported layout dimensions so that
// the parent allocates the correct (post-rotation) bounding box.
private fun Modifier.rotateWithLayout(degrees: Float): Modifier = this.layout { measurable, _ ->
    val placeable = measurable.measure(Constraints())
    layout(placeable.height, placeable.width) {
        placeable.placeWithLayer(
            x = -(placeable.width  - placeable.height) / 2,
            y =  (placeable.width  - placeable.height) / 2
        ) { rotationZ = degrees }
    }
}

private fun Modifier.rotatePortraitToLandscape(): Modifier = this.layout { measurable, constraints ->
    // Give child landscape constraints (swap portrait width/height)
    val swapped = Constraints(
        minWidth = constraints.minHeight,
        maxWidth = constraints.maxHeight,
        minHeight = constraints.minWidth,
        maxHeight = constraints.maxWidth,
    )
    val placeable = measurable.measure(swapped)
    // Report portrait-sized bounds to parent
    layout(placeable.height, placeable.width) {
        // placeWithLayer transforms both rendering and pointer events
        placeable.placeWithLayer(
            x = -(placeable.width - placeable.height) / 2,
            y = (placeable.width - placeable.height) / 2,
        ) {
            rotationZ = 90f  // 90° CCW: nut at top, scroll down for higher frets
        }
    }
}
