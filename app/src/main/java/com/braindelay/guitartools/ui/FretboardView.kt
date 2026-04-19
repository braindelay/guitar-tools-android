package com.braindelay.guitartools.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.braindelay.guitartools.music.FretPosition
import com.braindelay.guitartools.music.Note
import com.braindelay.guitartools.music.Scale
import kotlin.math.roundToInt

private const val NUM_FRETS = 19
private const val NUM_STRINGS = 6

private val INLAY_SINGLE = listOf(1, 3, 5, 7, 9, 15, 17, 19)
private val INLAY_DOUBLE = 12
private val INLAY_LABEL_FRETS = listOf(1, 3, 5, 7, 9, 12, 15, 17, 19)

@Composable
fun FretboardView(
    scale: Scale,
    positions: Map<FretPosition, Note>,
    selectedPosition: FretPosition?,
    triadNotes: Map<Note, String>?,
    onFretTapped: (FretPosition) -> Unit,
    onOtherTapped: () -> Unit,
    modifier: Modifier = Modifier,
    scaleFactor: Float = 1f,
    isLeftHanded: Boolean = false,
    showNoteNames: Boolean = false
) {
    val textMeasurer = rememberTextMeasurer()

    val woodColor    = Color(0xFF2D1B13)
    val fretColor    = Color(0xFFBDBDBD)
    val stringColor  = Color(0xFFE0E0E0)
    val nutColor     = Color(0xFFEFEBE9)
    val markerColor  = Color(0xFFF5F5F5).copy(alpha = 0.6f)

    val primaryColor     = MaterialTheme.colorScheme.primary
    val onPrimaryColor   = MaterialTheme.colorScheme.onPrimary
    val secondaryColor   = MaterialTheme.colorScheme.secondary
    val onSecondaryColor = MaterialTheme.colorScheme.onSecondary
    val tertiaryColor    = MaterialTheme.colorScheme.tertiary
    val onTertiaryColor  = MaterialTheme.colorScheme.onTertiary
    val dimNoteColor     = Color.White.copy(alpha = 0.2f)

    val cellWidthDp     = 64.dp * scaleFactor
    val stringSpacingDp = 32.dp * scaleFactor
    val noteRadiusDp    = 13.dp * scaleFactor
    val topPaddingDp    = 24.dp * scaleFactor
    val bottomPaddingDp = 32.dp * scaleFactor
    val openColWidthDp  = 48.dp * scaleFactor

    val totalWidth  = openColWidthDp + cellWidthDp * NUM_FRETS
    val totalHeight = topPaddingDp + stringSpacingDp * (NUM_STRINGS - 1) + bottomPaddingDp

    val density = LocalDensity.current
    val cw  = with(density) { cellWidthDp.toPx() }
    val ss  = with(density) { stringSpacingDp.toPx() }
    val nr  = with(density) { noteRadiusDp.toPx() }
    val tp  = with(density) { topPaddingDp.toPx() }
    val ocw = with(density) { openColWidthDp.toPx() }
    val tw  = ocw + NUM_FRETS * cw   // total width in px

    fun rhX(fret: Int) = if (fret == 0) ocw / 2f else ocw + (fret - 0.5f) * cw
    fun noteX(fret: Int) = if (isLeftHanded) tw - rhX(fret) else rhX(fret)
    fun stringY(s: Int)  = tp + (5 - s) * ss

    // Fret line x in left-handed mode is mirrored
    fun fretLineX(fret: Int) = if (isLeftHanded) tw - (ocw + fret * cw) else ocw + fret * cw

    Canvas(
        modifier = modifier
            .padding(vertical = 8.dp)
            .width(totalWidth)
            .height(totalHeight)
            .clip(RoundedCornerShape(8.dp))
            .background(woodColor)
            .pointerInput(positions, isLeftHanded) {
                detectTapGestures { tap ->
                    val tapX = if (isLeftHanded) tw - tap.x else tap.x
                    val fret = if (tapX < ocw) 0
                               else ((tapX - ocw) / cw).toInt().inc().coerceAtMost(NUM_FRETS)
                    val string = (5f - (tap.y - tp) / ss).roundToInt().coerceIn(0, 5)
                    val pos = FretPosition(string, fret)
                    if (positions.containsKey(pos)) onFretTapped(pos) else onOtherTapped()
                }
            }
    ) {
        // Fret lines
        for (fret in 1..NUM_FRETS) {
            val x = fretLineX(fret)
            drawLine(fretColor, Offset(x, tp - 8f), Offset(x, tp + 5 * ss + 8f), 3f)
        }

        // Nut
        val nutX = if (isLeftHanded) tw - ocw - 4f else ocw - 4f
        drawRect(nutColor, Offset(nutX, tp - 10f), Size(8f, 5 * ss + 20f))

        // Inlay rectangles
        val rw  = cw * 0.8f
        val rh  = (5 * ss) * 0.9f
        val dotY = tp + 2.5f * ss
        for (fret in INLAY_SINGLE) {
            val cx = noteX(fret)
            drawRect(markerColor, Offset(cx - rw / 2f, dotY - rh / 2f), Size(rw, rh))
        }
        val x12  = noteX(INLAY_DOUBLE)
        val rh12 = (5 * ss) * 0.4f
        drawRect(markerColor, Offset(x12 - rw / 2f, tp + 1.1f * ss - rh12 / 2f), Size(rw, rh12))
        drawRect(markerColor, Offset(x12 - rw / 2f, tp + 3.9f * ss - rh12 / 2f), Size(rw, rh12))

        // String lines
        val thicknesses = listOf(4f, 3.5f, 3f, 2.5f, 2f, 1.5f)
        for (s in 0 until NUM_STRINGS) {
            drawLine(stringColor, Offset(0f, stringY(s)), Offset(tw, stringY(s)), thicknesses[s])
        }

        // Fret number labels
        for (fret in INLAY_LABEL_FRETS) {
            val fontSize = 11.sp * scaleFactor
            val m = textMeasurer.measure(fret.toString(), TextStyle(fontSize = fontSize, color = Color.White.copy(alpha = 0.5f)))
            drawText(m, topLeft = Offset(noteX(fret) - m.size.width / 2f, tp + 5 * ss + nr + 6f))
        }

        // Notes
        for ((pos, note) in positions) {
            val x = noteX(pos.fret)
            val y = stringY(pos.string)

            val (fill, textColor, label) = when {
                triadNotes != null -> {
                    val tLabel = triadNotes[note]
                    if (tLabel != null) {
                        val (f, t) = when {
                            tLabel.contains("R")  -> tertiaryColor  to onTertiaryColor
                            tLabel.contains("3")  -> primaryColor   to onPrimaryColor
                            else                  -> secondaryColor to onSecondaryColor
                        }
                        val display = if (showNoteNames) note.displayName else tLabel
                        Triple(f, t, display)
                    } else {
                        Triple(dimNoteColor, Color.Transparent, null)
                    }
                }
                else -> {
                    val roman = scale.romanNumeral(note) ?: continue
                    val isRoot = roman == "I"
                    val display = if (showNoteNames) note.displayName else roman
                    Triple(
                        if (isRoot) tertiaryColor else primaryColor,
                        if (isRoot) onTertiaryColor else onPrimaryColor,
                        display
                    )
                }
            }

            drawCircle(fill, nr, Offset(x, y))

            if (label != null) {
                val style = TextStyle(fontSize = (10 * scaleFactor).sp, fontWeight = FontWeight.Bold, color = textColor)
                val m = textMeasurer.measure(label, style)
                drawText(m, topLeft = Offset(x - m.size.width / 2f, y - m.size.height / 2f))
            }

            if (pos == selectedPosition) {
                drawCircle(Color.White, nr + 4f, Offset(x, y), style = Stroke(width = 3f))
            }
        }
    }
}
