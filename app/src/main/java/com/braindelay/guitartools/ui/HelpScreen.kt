package com.braindelay.guitartools.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.braindelay.guitartools.R

private data class HelpSection(val id: String, val title: String, val keywords: String)

private val helpSections = listOf(
    HelpSection("intro", "Guitar Tools",
        "learn scales chord voicings progressions fretboard audio playback overview"),
    HelpSection("scales", "Scales",
        "root note mode major minor dorian phrygian lydian mixolydian locrian harmonic melodic fretboard roman numerals degrees collapse expand selector"),
    HelpSection("fretboard_options", "Fretboard Options",
        "left-handed mirror swap label mode roman numerals note names"),
    HelpSection("chord_voicings", "Chord Voicings",
        "voicing major triad minor dim dom 7 shell overlay chord tones R 3 5 7 play audio clear diatonic"),
    HelpSection("arpeggio", "Arpeggio Overlays",
        "arpeggio diatonic chords 1 3 5 7 highlight summary card clear"),
    HelpSection("fullscreen", "Fullscreen Mode",
        "fullscreen zoom fretboard go back"),
    HelpSection("chords_screen", "Chords",
        "chords circle root voicing diagrams scroll horizontal hear playback"),
    HelpSection("progression_screen", "Progression",
        "progression chord type note add list reorder arrows delete bpm slider tempo playback loop pause stop scales view arpeggios real time")
)

@Composable
fun HelpScreen() {
    var query by remember { mutableStateOf("") }
    val q = query.trim()
    val visible = remember(q) {
        if (q.isBlank()) helpSections
        else helpSections.filter {
            it.title.contains(q, ignoreCase = true) || it.keywords.contains(q, ignoreCase = true)
        }
    }

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            placeholder = { Text("Search help…") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(Icons.Default.Clear, "Clear search")
                    }
                }
            },
            singleLine = true
        )
        if (visible.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    "No results for \"$q\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                items(visible, key = { it.id }) { SectionBlock(it.id) }
            }
        }
    }
}

@Composable
private fun SectionBlock(id: String) {
    when (id) {
        "intro" -> BodyText(
            "A tool to help guitarists learn scales, chord voicings, and progressions. " +
            "Visualise any scale on a 19-fret fretboard, explore diatonic chord shapes, " +
            "and build chord progressions with audio playback."
        )
        "scales" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Scales")
            HelpImg(R.drawable.help_scale_selector, "Root & Scale selector")
            NumberedList(listOf(
                "**Choose a root note** — tap any of the 12 chromatic notes in the Root & Scale card.",
                "**Choose a mode** — tap a mode chip (Major, Minor, Dorian, Harmonic Minor, etc.).",
                "The fretboard updates immediately, showing every scale note labelled with Roman numeral degrees (I–VII). Root notes appear in a distinct colour.",
                "**Collapse the selector** — tap the arrow in the top bar to hide the Root & Scale panel and focus on the fretboard. Tap again to restore it."
            ))
        }
        "fretboard_options" -> Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            SectionHeader("Fretboard Options")
            OptionRow(Icons.Default.SwapHoriz,
                "Toggle **left-handed mode** — mirrors the fretboard so the nut is on the right")
            OptionRow(Icons.Default.TextFields,
                "Toggle **label mode** — switches note labels between Roman numerals (I, b3…) and note names (C, C#…)")
        }
        "chord_voicings" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Chord Voicings")
            HelpImg(R.drawable.help_voicing_picker, "Voicing type picker")
            BulletList(listOf(
                "A scrollable panel to the left of the fretboard lists 10 voicing types (Major Triad, Minor Triad, Dom 7 Shell, etc.).",
                "**Select a voicing type**, then **tap any highlighted note** on the fretboard to overlay that chord's tones (colour-coded R / 3 / 5 / 7)."
            ))
            HelpImg(R.drawable.help_chord_overlay, "Chord tone overlay (R / 3 / 5 / 7)")
            BulletList(listOf(
                "Tap **▶** (Play) to hear the chord via synthesised audio.",
                "Diatonic voicings for the tapped note are marked with an ⓘ icon.",
                "Tap the same voicing chip again, or press **Clear**, to reset the overlay."
            ))
        }
        "arpeggio" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Arpeggio Overlays")
            BulletList(listOf(
                "In the **Diatonic Chords** card, tap any chord chip (e.g. \"II: D Min\") to highlight the full 1-3-5-7 arpeggio of that chord across the entire fretboard.",
                "A summary card shows the chord name, quality, and constituent notes.",
                "Tap the chip again or press **Clear** to remove the overlay."
            ))
        }
        "fullscreen" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Fullscreen Mode")
            BulletList(listOf(
                "Tap anywhere on the fretboard that is **not** a highlighted note to enter fullscreen zoom.",
                "Tap **Go Back** to return to the normal view."
            ))
        }
        "chords_screen" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Chords")
            HelpImg(R.drawable.help_chords, "Chords screen — voicing diagrams")
            NumberedList(listOf(
                "**Tap a note** on the circle (left panel) to set the root.",
                "The right panel shows all 16 chord types at once — scroll vertically to browse them.",
                "Each chord type section contains a horizontally scrollable row of voicing diagrams covering up to 3 octave positions.",
                "**Tap any diagram** to hear it played back."
            ))
        }
        "progression_screen" -> Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            SectionHeader("Progression")
            HelpImg(R.drawable.help_progression, "Progression builder")
            SubHeader("Building a Progression")
            NumberedList(listOf(
                "In the right panel, select a **chord type** from the scrollable chip list.",
                "Tap a **note** on the circle to set the root.",
                "Press **Add** to append the chord to the progression list (left panel).",
                "Repeat to build up a sequence. Use the **← →** arrows to reorder chords and **✕** to delete one."
            ))
            SubHeader("Playback")
            NumberedList(listOf(
                "Set the **BPM** with the slider (20–240). Each chord plays for 4 beats at the chosen tempo.",
                "Press **▶** to start looping through the progression. The active chord is highlighted.",
                "Adjust the BPM slider at any time — the new speed takes effect on the next chord.",
                "Press **⏸** to stop.",
                "While it is playing, you can go back to the scales view, to see the arpeggios being rendered in real time."
            ))
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Column(Modifier.padding(top = 4.dp)) {
        Text(text, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        HorizontalDivider(Modifier.padding(top = 6.dp))
    }
}

@Composable
private fun SubHeader(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 2.dp)
    )
}

@Composable
private fun BodyText(text: String) {
    Text(bold(text), style = MaterialTheme.typography.bodyLarge)
}

@Composable
private fun HelpImg(resId: Int, caption: String = "") {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(resId),
            contentDescription = caption.ifEmpty { null },
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        if (caption.isNotEmpty()) {
            Text(
                caption,
                style = MaterialTheme.typography.labelLarge,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun NumberedList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEachIndexed { i, text ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "${i + 1}.",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(24.dp)
                )
                Text(
                    bold(text),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun BulletList(items: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items.forEach { text ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("•", style = MaterialTheme.typography.bodyLarge, modifier = Modifier.width(16.dp))
                Text(
                    bold(text),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun OptionRow(icon: ImageVector, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(44.dp)
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer)
            }
        }
        Text(
            bold(description),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
    }
}

private fun bold(text: String): AnnotatedString = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        val start = text.indexOf("**", i)
        if (start == -1) { append(text.substring(i)); break }
        append(text.substring(i, start))
        val end = text.indexOf("**", start + 2)
        if (end == -1) { append(text.substring(start)); break }
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append(text.substring(start + 2, end))
        }
        i = end + 2
    }
}
