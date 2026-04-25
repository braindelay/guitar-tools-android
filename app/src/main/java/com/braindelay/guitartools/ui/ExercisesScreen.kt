package com.braindelay.guitartools.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private enum class Difficulty(val label: String) {
    BEGINNER("Beginner"),
    INTERMEDIATE("Intermediate"),
    ADVANCED("Advanced")
}

private data class Exercise(
    val id: String,
    val title: String,
    val difficulty: Difficulty,
    val summary: String,
    val steps: List<String>,
    val tip: String? = null
)

private val exercises = listOf(

    Exercise(
        id = "arpeggio_shapes",
        title = "Triad Arpeggios Across the Neck",
        difficulty = Difficulty.BEGINNER,
        summary = "Play the three chord tones — root, 3rd, 5th — in sequence across the fretboard. " +
                "Builds essential finger memory for finding chord tones in any position.",
        steps = listOf(
            "Pick a chord — start with C major.",
            "Find the root (C) on the low E string at fret 8. Play it, then find the 3rd (E) and 5th (G) on the next two strings.",
            "Ascend: R → 3 → 5, then descend: 5 → 3 → R. Use alternate picking.",
            "Repeat starting from the A string, then the D string, climbing up the neck.",
            "Work through all five CAGED positions: E-shape (fret 8), A-shape (fret 3), G-shape (fret 5), C-shape (fret 8), D-shape (fret 10).",
            "Use a metronome. Start at 60 BPM. Aim for clean, even tone before increasing speed."
        ),
        tip = "On the Chords screen: select C → tap a Major voicing to see exactly where the R, 3, and 5 land."
    ),

    Exercise(
        id = "three_string_triads",
        title = "Three-String Triad Shapes",
        difficulty = Difficulty.BEGINNER,
        summary = "Compact three-string shapes give you a triad you can grab instantly anywhere on the neck — " +
                "the backbone of modern chord-tone soloing.",
        steps = listOf(
            "On strings 1–2–3 (high e, B, G), find the three inversions of C major: root position (C on the G string), 1st inversion (E on the G string), 2nd inversion (G on the G string).",
            "Play each inversion as a broken arpeggio — lowest string first, then middle, then highest.",
            "Repeat on strings 2–3–4, 3–4–5, and 4–5–6.",
            "Connect the inversions: move from root position up to 1st inversion, then 2nd inversion, climbing the neck.",
            "Try the same shapes for C minor — notice only the 3rd changes (E → Eb).",
            "Practise switching between major and minor shapes until you can find any inversion immediately."
        ),
        tip = "Three-string shapes are used extensively by Eric Johnson, John Frusciante, and Guthrie Govan. They are the most direct way to outline a chord while soloing."
    ),

    Exercise(
        id = "approach_notes",
        title = "Approach Notes to Chord Tones",
        difficulty = Difficulty.INTERMEDIATE,
        summary = "Make any note sound intentional by approaching a chord tone by a half-step. " +
                "This single technique transforms scale runs into musical phrases.",
        steps = listOf(
            "Choose a chord tone as your target — start with the root of C major (any C on the fretboard).",
            "Play the note one fret below your target, then resolve up to it. This is a chromatic approach from below.",
            "Play the note one fret above, then resolve down. This is a chromatic approach from above.",
            "Combine both: below → above → target (encircling the chord tone).",
            "Now apply the same idea to the 3rd (E) and 5th (G) of C major.",
            "Practise over a slow I–IV–V progression. Target the root of each chord as it changes, using an approach note just before landing."
        ),
        tip = "Approaching from a semitone below sounds naturally bluesy. Try it over a 12-bar blues to hear how strongly it pulls toward the chord tone."
    ),

    Exercise(
        id = "chord_tone_landing",
        title = "Scale Runs Landing on Chord Tones",
        difficulty = Difficulty.INTERMEDIATE,
        summary = "Any scale note sounds good over a chord — if you resolve to a chord tone. " +
                "This exercise trains your ear to hear the harmony while playing through the scale.",
        steps = listOf(
            "Set the Scales screen to C Major. Enable the Major chord overlay on C to see the chord tones highlighted.",
            "Play through the C major scale ascending from the low E. As you end each phrase, consciously land on C, E, or G.",
            "Vary where you land: the root (most stable), the 5th (slightly open), the 3rd (warm). Notice how each landing feels different.",
            "Build a C–F–G progression in the Progression screen. Now play the C major scale, but change target chord tone when the chord changes: land on F or A over the F chord, G or B over the G chord.",
            "Start slowly — one held chord tone per chord is fine. The goal is to hear the difference between landing on a chord tone versus a non-chord-tone.",
            "Gradually add more notes between the landings as your ear becomes reliable."
        ),
        tip = "Landing on the 7th (B over C major) sounds tense. Landing on the 3rd (E) sounds warm and complete. Landing on the root (C) sounds final. Use these qualities intentionally."
    ),

    Exercise(
        id = "triad_pairs",
        title = "Triad Pairs",
        difficulty = Difficulty.INTERMEDIATE,
        summary = "Alternate between two adjacent diatonic triads to create flowing, " +
                "sophisticated-sounding lines — without thinking about scales at all.",
        steps = listOf(
            "In C major, pick two diatonic triads: C major (I) and D minor (ii). Both come from the key.",
            "On one string set, play the C major three-string triad ascending, then immediately play D minor ascending. Descend in the same order.",
            "Notice how the lines move smoothly — the triads share notes and sit next to each other on the neck.",
            "Try other pairs: G major + A minor (V + vi), F major + G major (IV + V).",
            "Over a C major chord, the C–Dm pair sounds inside (safe). Over G7, try G–Am or F–G for a more outside, modern flavour.",
            "Record a simple backing at 70 BPM and improvise using only triad pairs — no other notes. Constraint forces musical choices."
        ),
        tip = "The Dm–Em pair over C major sounds especially smooth and is widely used in rock and jazz. Listen to Robben Ford or Scott Henderson for examples."
    ),

    Exercise(
        id = "voice_leading_changes",
        title = "Voice Leading Through Chord Changes",
        difficulty = Difficulty.ADVANCED,
        summary = "Solo on chord tones only, moving to the nearest available chord tone on each change. " +
                "Trains your ear to hear and follow every chord in a progression.",
        steps = listOf(
            "Build a I–vi–IV–V in C in the Progression screen: C Maj → A Min → F Maj → G Maj.",
            "Map the chord tones: Cmaj (C–E–G), Am (A–C–E), Fmaj (F–A–C), Gmaj (G–B–D).",
            "Choose a starting note — say E (the 3rd of C major). On the change to Am, find the nearest Am chord tone. E is the 5th of Am, so stay on E. Smooth.",
            "From E on Am, the next chord is F major. E is not in F major. The nearest tones are F (one semitone up) or A. Move to F — a half-step resolution.",
            "Continue to G major. Nearest G major tone to F is G (whole step up) or the 3rd, B.",
            "Repeat the progression starting from different notes. Aim to never jump more than a major third between chord changes.",
            "Once comfortable, add one passing scale tone between chord tones to smooth the line."
        ),
        tip = "This is how jazz musicians learn to 'play the changes'. Even very simple melodies sound harmonically sophisticated when every note connects logically to the chord."
    ),

    Exercise(
        id = "caged_position_soloing",
        title = "CAGED Position Soloing",
        difficulty = Difficulty.ADVANCED,
        summary = "Restrict yourself to one CAGED chord shape at a time and solo using only those three notes. " +
                "Breaks the habit of running scales linearly and develops real phrasing.",
        steps = listOf(
            "On the Chords screen, select G major and study the E-shape voicing at fret 3. Memorise where R, 3, and 5 sit on the strings.",
            "Set a metronome to 60 BPM. Improvise for 8 bars using ONLY the three notes of that G major shape — no other notes.",
            "Focus entirely on rhythm, dynamics, and space rather than melody. You have three notes; use them musically.",
            "After 8 bars, shift to the A-shape at fret 10. Continue the solo, staying within that new shape.",
            "Work through all five CAGED positions for G major, spending 8 bars on each.",
            "Build a I–IV–V (G–C–D) in the Progression screen and change your CAGED shape on every chord change, always staying inside the active chord's own triad shape."
        ),
        tip = "Limiting to three notes per position sounds like a constraint — but it forces you to develop phrasing, timing, and dynamics instead of relying on fast scale runs."
    ),
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ExercisesScreen() {
    var filterDifficulty by remember { mutableStateOf<Difficulty?>(null) }

    val visible = remember(filterDifficulty) {
        if (filterDifficulty == null) exercises
        else exercises.filter { it.difficulty == filterDifficulty }
    }

    val topInset = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp, end = 16.dp,
            top = topInset + 12.dp, bottom = 32.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(key = "header") {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Exercises", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(
                    "Techniques for soloing on chord tones and triads.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        item(key = "filter") {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FilterChip(
                    selected = filterDifficulty == null,
                    onClick = { filterDifficulty = null },
                    label = { Text("All") }
                )
                Difficulty.entries.forEach { diff ->
                    FilterChip(
                        selected = filterDifficulty == diff,
                        onClick = { filterDifficulty = if (filterDifficulty == diff) null else diff },
                        label = { Text(diff.label) }
                    )
                }
            }
        }

        items(visible, key = { it.id }) { exercise ->
            ExerciseCard(exercise)
        }
    }
}

@Composable
private fun ExerciseCard(exercise: Exercise) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    exercise.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                DifficultyBadge(exercise.difficulty)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    modifier = Modifier.size(20.dp).padding(start = 4.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                exercise.summary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))

                    exercise.steps.forEachIndexed { i, step ->
                        Row(
                            modifier = Modifier.padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                "${i + 1}.",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.width(22.dp)
                            )
                            Text(step, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                        }
                    }

                    exercise.tip?.let { tip ->
                        Spacer(Modifier.height(4.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                        Spacer(Modifier.height(8.dp))
                        Text(
                            "Tip: $tip",
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DifficultyBadge(difficulty: Difficulty) {
    val (bg, fg) = when (difficulty) {
        Difficulty.BEGINNER     -> MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
        Difficulty.INTERMEDIATE -> MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
        Difficulty.ADVANCED     -> MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
    }
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = bg
    ) {
        Text(
            difficulty.label,
            style = MaterialTheme.typography.labelSmall,
            color = fg,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}
