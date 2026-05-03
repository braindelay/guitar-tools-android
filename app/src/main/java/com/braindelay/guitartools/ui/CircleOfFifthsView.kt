package com.braindelay.guitartools.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.braindelay.guitartools.music.Note
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val CIRCLE_OF_FIFTHS = listOf(
    Note.C, Note.G, Note.D, Note.A, Note.E, Note.B,
    Note.F_SHARP, Note.C_SHARP, Note.G_SHARP, Note.D_SHARP, Note.A_SHARP, Note.F
)

@Composable
fun CircleOfFifthsView(
    selectedNote: Note?,
    onNoteSelected: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val surfaceColor = MaterialTheme.colorScheme.surface
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    Canvas(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { tapOffset ->
                    val cx = size.width / 2f
                    val cy = size.height / 2f
                    val dx = tapOffset.x - cx
                    val dy = tapOffset.y - cy
                    val dist = sqrt(dx * dx + dy * dy)
                    val outerR = minOf(size.width, size.height) / 2f * 0.92f
                    val innerR = outerR * 0.32f
                    if (dist in innerR..outerR) {
                        var angle = Math.toDegrees(atan2(dy.toDouble(), dx.toDouble())).toFloat()
                        // Convert to 0=top, clockwise
                        angle = (angle + 90f + 360f) % 360f
                        // Add 15° so sector boundaries fall between notes
                        val index = ((angle + 15f) / 30f).toInt() % 12
                        onNoteSelected(CIRCLE_OF_FIFTHS[index])
                    }
                }
            }
    ) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val outerRadius = minOf(size.width, size.height) / 2f * 0.92f
        val innerRadius = outerRadius * 0.32f
        val labelRadius = (innerRadius + outerRadius) / 2f

        CIRCLE_OF_FIFTHS.forEachIndexed { index, note ->
            val isSelected = note == selectedNote
            // startAngle: -90 puts C at top; each note is 30° clockwise
            val startAngle = -105f + index * 30f
            val sweepAngle = 28f

            drawArc(
                color = if (isSelected) primaryColor else surfaceVariant,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                size = Size(outerRadius * 2, outerRadius * 2)
            )

            if (isSelected) {
                drawArc(
                    color = primaryColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - outerRadius, center.y - outerRadius),
                    size = Size(outerRadius * 2, outerRadius * 2),
                    style = Stroke(width = 3f)
                )
            }

            val centerAngleDeg = -90.0 + index * 30.0
            val centerAngleRad = Math.toRadians(centerAngleDeg)
            val labelX = center.x + labelRadius * cos(centerAngleRad).toFloat()
            val labelY = center.y + labelRadius * sin(centerAngleRad).toFloat()

            val textColor = if (isSelected) onPrimaryColor else onSurfaceColor
            val fontSize = if (note.displayName.length > 1) 11.sp else 13.sp
            val style =
                TextStyle(fontSize = fontSize, color = textColor, textAlign = TextAlign.Center)
            val measured = textMeasurer.measure(note.displayName, style)
            drawText(
                textMeasurer = textMeasurer,
                text = note.displayName,
                style = style,
                topLeft = Offset(
                    labelX - measured.size.width / 2f,
                    labelY - measured.size.height / 2f
                )
            )
        }

        // Draw inner circle to create donut shape
        drawCircle(surfaceColor, radius = innerRadius, center = center)

        // Inner ring border
        drawCircle(
            color = onSurfaceColor.copy(alpha = 0.2f),
            radius = innerRadius,
            center = center,
            style = Stroke(width = 1f)
        )

        // Center label
        val centerStyle = TextStyle(fontSize = 10.sp, color = onSurfaceColor.copy(alpha = 0.6f))
        val centerText = "5ths"
        val centerMeasured = textMeasurer.measure(centerText, centerStyle)
        drawText(
            textMeasurer = textMeasurer,
            text = centerText,
            style = centerStyle,
            topLeft = Offset(
                center.x - centerMeasured.size.width / 2f,
                center.y - centerMeasured.size.height / 2f
            )
        )
    }
}
