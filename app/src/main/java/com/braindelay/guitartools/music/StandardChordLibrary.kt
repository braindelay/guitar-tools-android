package com.braindelay.guitartools.music

/**
 * CAGED-style standard chord shapes.
 * rootString: which string the root sits on (0=low E, 1=A, 2=D).
 * offsets: per-string fret offset from the root's fret (null = muted).
 * The open strings for each string: E(4) A(9) D(2) G(7) B(11) e(4).
 */
object StandardChordLibrary {

    private data class Shape(val rootString: Int, val offsets: List<Int?>)

    // Open-string semitones for strings 0-5
    private val OPEN = intArrayOf(4, 9, 2, 7, 11, 4)

    private val shapes: Map<ChordType, List<Shape>> = mapOf(

        ChordType.MAJOR to listOf(
            Shape(0, listOf(0, 2, 2, 1, 0, 0)),           // E shape
            Shape(1, listOf(null, 0, 2, 2, 2, 0)),         // A shape
            Shape(2, listOf(null, null, 0, 2, 3, 2))        // D shape
        ),

        ChordType.MINOR to listOf(
            Shape(0, listOf(0, 2, 2, 0, 0, 0)),            // Em shape
            Shape(1, listOf(null, 0, 2, 2, 1, 0)),          // Am shape
            Shape(2, listOf(null, null, 0, 2, 3, 1))        // Dm shape
        ),

        // E7 = 020100, A7 = x02020, D7 = xx0212
        ChordType.DOM7 to listOf(
            Shape(0, listOf(0, 2, 0, 1, 0, 0)),            // E7 shape
            Shape(1, listOf(null, 0, 2, 0, 2, 0)),          // A7 shape
            Shape(2, listOf(null, null, 0, 2, 1, 2))        // D7 shape
        ),

        // Emaj7 = 021100, Amaj7 = x02120
        ChordType.MAJ7 to listOf(
            Shape(0, listOf(0, 2, 1, 1, 0, 0)),            // Emaj7 shape
            Shape(1, listOf(null, 0, 2, 1, 2, 0))           // Amaj7 shape
        ),

        // Em7 = 022030, Am7 = x02010, Dm7 = xx0211
        ChordType.MIN7 to listOf(
            Shape(0, listOf(0, 2, 2, 0, 3, 0)),            // Em7 shape
            Shape(1, listOf(null, 0, 2, 0, 1, 0)),          // Am7 shape
            Shape(2, listOf(null, null, 0, 2, 1, 1))        // Dm7 shape
        ),

        // Adim = x01210 (mute high e), Ddim = xx0131
        ChordType.DIM to listOf(
            Shape(1, listOf(null, 0, 1, 2, 1, null)),       // from A string
            Shape(2, listOf(null, null, 0, 1, 3, 1))        // from D string
        ),

        // Eaug = 032110, Aaug = x03221
        ChordType.AUG to listOf(
            Shape(0, listOf(0, 3, 2, 1, 1, 0)),            // from E string
            Shape(1, listOf(null, 0, 3, 2, 2, 1))           // from A string
        ),

        // Asus2 = x02200, Dsus2 = xx0230
        ChordType.SUS2 to listOf(
            Shape(1, listOf(null, 0, 2, 2, 0, 0)),          // A-shape sus2
            Shape(2, listOf(null, null, 0, 2, 3, 0))        // D-shape sus2
        ),

        // Esus4 = 022200, Asus4 = x02230, Dsus4 = xx0233
        ChordType.SUS4 to listOf(
            Shape(0, listOf(0, 2, 2, 2, 0, 0)),            // E-shape sus4
            Shape(1, listOf(null, 0, 2, 2, 3, 0)),          // A-shape sus4
            Shape(2, listOf(null, null, 0, 2, 3, 3))        // D-shape sus4
        ),

        // Adim7 = x01212, Ddim7 = xx0101
        ChordType.DIM7 to listOf(
            Shape(1, listOf(null, 0, 1, 2, 1, 2)),          // from A string
            Shape(2, listOf(null, null, 0, 1, 0, 1))         // from D string
        ),

        // Am7b5 = x01213, Dm7b5 = xx0111
        ChordType.MIN7B5 to listOf(
            Shape(1, listOf(null, 0, 1, 2, 1, 3)),          // from A string
            Shape(2, listOf(null, null, 0, 1, 1, 1))        // from D string
        ),
    )

    fun getVoicings(root: Note, chordType: ChordType): List<ChordVoicing> =
        (shapes[chordType] ?: emptyList()).map { toVoicing(root, it) }

    private fun toVoicing(root: Note, shape: Shape): ChordVoicing {
        val rootFret = (root.semitone - OPEN[shape.rootString] + 12) % 12
        val frets = shape.offsets.map { offset -> offset?.let { rootFret + it } }
        val baseFret = frets.filterNotNull().filter { it > 0 }.minOrNull() ?: 0
        return ChordVoicing(frets, baseFret)
    }
}
