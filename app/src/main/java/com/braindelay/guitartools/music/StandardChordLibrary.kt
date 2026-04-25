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

        // Em(maj7) = 021000, Am(maj7) = x02110, Dm(maj7) = xx0221
        ChordType.MIN_MAJ7 to listOf(
            Shape(0, listOf(0, 2, 1, 0, 0, 0)),             // E-shape m(maj7)
            Shape(1, listOf(null, 0, 2, 1, 1, 0)),          // A-shape m(maj7)
            Shape(2, listOf(null, null, 0, 2, 2, 1))        // D-shape m(maj7)
        ),

        // 6 chords (e.g. G6 = 355453, C6 = x35555, D6 = xx0202)
        ChordType.SIX to listOf(
            Shape(0, listOf(0, 2, 2, 1, 2, 0)),             // E-shape 6
            Shape(1, listOf(null, 0, 2, 2, 2, 2)),          // A-shape 6
            Shape(2, listOf(null, null, 0, 2, 0, 2))        // D-shape 6
        ),

        // 9 chords (e.g. G9 = 353435, C9 = x32333)
        ChordType.NINE to listOf(
            Shape(0, listOf(0, 2, 0, 1, 0, 2)),            // E-shape 9
            Shape(1, listOf(null, 0, -1, 0, 0, 0))         // A-shape 9
        ),

        // 6/9 chords (e.g. G6/9 = 3x2233, C6/9 = x32233)
        ChordType.SIX_NINE to listOf(
            Shape(0, listOf(0, null, -1, -1, 0, 0)),       // E-shape 6/9
            Shape(1, listOf(null, 0, -1, -1, 0, 0))        // A-shape 6/9
        ),

        // 13 chords (e.g. G13 = 3x345x, C13 = x32335)
        ChordType.THIRTEEN to listOf(
            Shape(0, listOf(0, null, 0, 1, 2, null)),      // E-shape 13
            Shape(1, listOf(null, 0, -1, 0, 0, 2))         // A-shape 13
        ),
    )

    fun getVoicings(root: Note, chordType: ChordType): List<ChordVoicing> {
        val voicings = mutableListOf<ChordVoicing>()
        for (shape in shapes[chordType] ?: emptyList()) {
            val baseRootFret = (root.semitone - OPEN[shape.rootString] + 12) % 12
            for (octave in 0..2) {
                val rootFret = baseRootFret + octave * 12
                val frets = shape.offsets.map { offset -> offset?.let { rootFret + it } }
                
                val maxFret = frets.filterNotNull().maxOrNull() ?: -1
                val minFret = frets.filterNotNull().minOrNull() ?: 0
                
                if (minFret >= 0 && maxFret <= 19) {
                    val hasOpenStrings = frets.filterNotNull().any { it == 0 }
                    val baseFret = if (hasOpenStrings) 1
                                   else frets.filterNotNull().filter { it > 0 }.minOrNull() ?: 0
                    voicings.add(ChordVoicing(frets, baseFret))
                    break
                }
            }
        }
        return voicings.distinct().sortedBy { it.baseFret }
    }
}
