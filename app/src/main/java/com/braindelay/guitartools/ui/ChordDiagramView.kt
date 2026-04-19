package com.braindelay.guitartools.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braindelay.guitartools.music.ChordType
import com.braindelay.guitartools.music.ChordVoicing
import com.braindelay.guitartools.music.Fretboard
import com.braindelay.guitartools.music.Note

private val FRETBOARD = Fretboard(19)
private const val DISPLAY_FRETS = 4

@Composable
fun ChordDiagramView(
    voicing: ChordVoicing,
    root: Note,
    chordType: ChordType,
    modifier: Modifier = Modifier,
    onPlay: (() -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()
    val noteToLabel: Map<Note, String> = chordType.toneOffsets
        .mapIndexed { i, offset -> root.transpose(offset) to chordType.noteLabels[i] }
        .toMap()

    val primaryColor   = MaterialTheme.colorScheme.primary
    val tertiaryColor  = MaterialTheme.colorScheme.tertiary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    val clickModifier = if (onPlay != null) Modifier.clickable { onPlay() } else Modifier

    Canvas(modifier = modifier.then(clickModifier).size(72.dp, 92.dp)) {
        val leftPad    = size.width * 0.18f
        val topPad     = size.height * 0.20f
        val rightEdge  = size.width - size.width * 0.06f
        val bottomEdge = size.height - size.height * 0.08f
        val gridWidth  = rightEdge - leftPad
        val gridHeight = bottomEdge - topPad
        val stringSpacing = gridWidth / 5f
        val fretSpacing   = gridHeight / DISPLAY_FRETS.toFloat()

        val baseFret      = voicing.baseFret
        val isOpenPosition = baseFret <= 1

        if (!isOpenPosition) {
            val label = "$baseFret"
            val style = TextStyle(fontSize = 8.sp, color = onSurfaceColor)
            val m = textMeasurer.measure(label, style)
            drawText(textMeasurer, label,
                topLeft = Offset(2f, topPad - m.size.height / 2f), style = style)
        }

        if (isOpenPosition) {
            drawRect(onSurfaceColor, topLeft = Offset(leftPad, topPad - 3f), size = Size(gridWidth, 4f))
        }

        for (f in 0..DISPLAY_FRETS) {
            val y = topPad + f * fretSpacing
            drawLine(onSurfaceColor.copy(alpha = 0.4f), Offset(leftPad, y), Offset(rightEdge, y), 1f)
        }

        for (s in 0..5) {
            val x = leftPad + s * stringSpacing
            drawLine(onSurfaceColor.copy(alpha = 0.5f), Offset(x, topPad), Offset(x, bottomEdge), 1f)
        }

        for (s in 0..5) {
            val fret = voicing.stringFrets[s]
            val x = leftPad + s * stringSpacing
            when {
                fret == null -> {
                    val y = topPad - fretSpacing * 0.6f
                    drawLine(Color(0xFFE53935), Offset(x - 5f, y - 5f), Offset(x + 5f, y + 5f), 2f)
                    drawLine(Color(0xFFE53935), Offset(x + 5f, y - 5f), Offset(x - 5f, y + 5f), 2f)
                }
                fret == 0 -> {
                    val y     = topPad - fretSpacing * 0.6f
                    val note  = FRETBOARD.noteAt(s, 0)
                    val color = noteColor(note, noteToLabel, primaryColor, tertiaryColor, secondaryColor)
                    drawCircle(color, radius = 5f, center = Offset(x, y), style = Stroke(2f))
                }
                else -> {
                    val displayFret = fret - baseFret + 1
                    if (displayFret in 1..DISPLAY_FRETS) {
                        val y     = topPad + (displayFret - 0.5f) * fretSpacing
                        val note  = FRETBOARD.noteAt(s, fret)
                        val color = noteColor(note, noteToLabel, primaryColor, tertiaryColor, secondaryColor)
                        drawCircle(color, radius = minOf(stringSpacing, fretSpacing) * 0.38f, center = Offset(x, y))
                        noteToLabel[note]?.let { label ->
                            val style = TextStyle(fontSize = 6.sp, color = Color.White)
                            val m = textMeasurer.measure(label, style)
                            drawText(textMeasurer, label,
                                topLeft = Offset(x - m.size.width / 2f, y - m.size.height / 2f),
                                style = style)
                        }
                    }
                }
            }
        }

        // Play indicator border
        if (onPlay != null) {
            drawRect(
                color = onSurfaceColor.copy(alpha = 0.15f),
                topLeft = Offset(0f, 0f),
                size = size,
                style = Stroke(width = 1.5f)
            )
        }
    }
}

private fun noteColor(
    note: Note,
    noteToLabel: Map<Note, String>,
    primary: Color,
    tertiary: Color,
    secondary: Color
): Color = when (noteToLabel[note]) {
    "R"        -> tertiary
    "3", "b3"  -> primary
    else       -> secondary
}
