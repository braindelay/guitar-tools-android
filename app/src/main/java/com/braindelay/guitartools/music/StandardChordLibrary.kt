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

        // Power chords: E5 = 022xxx, A5 = x022xx, D5 = xx023x
        ChordType.POWER to listOf(
            Shape(0, listOf(0, 2, 2, null, null, null)),
            Shape(1, listOf(null, 0, 2, 2, null, null)),
            Shape(2, listOf(null, null, 0, 2, 3, null))
        ),

        ChordType.MAJOR to listOf(
            Shape(0, listOf(0, 2, 2, 1, 0, 0)),           // E shape
            Shape(1, listOf(null, 0, 2, 2, 2, 0)),         // A shape
            Shape(2, listOf(null, null, 0, 2, 3, 2))        // D shape
        ),

        // Eadd9 = 022102, Cadd9 = x32033
        ChordType.ADD9 to listOf(
            Shape(0, listOf(0, 2, 2, 1, 0, 2)),
            Shape(1, listOf(null, 0, -1, -3, 0, 0))
        ),

        ChordType.MINOR to listOf(
            Shape(0, listOf(0, 2, 2, 0, 0, 0)),            // Em shape
            Shape(1, listOf(null, 0, 2, 2, 1, 0)),          // Am shape
            Shape(2, listOf(null, null, 0, 2, 3, 1))        // Dm shape
        ),

        // Em(add9) = 022002, Cm(add9) = x31033
        ChordType.M_ADD9 to listOf(
            Shape(0, listOf(0, 2, 2, 0, 0, 2)),
            Shape(1, listOf(null, 0, -2, -3, 0, 0))
        ),

        // Em6 = 022020, Am6 = x02212, Dm6 = xx0201
        ChordType.MIN6 to listOf(
            Shape(0, listOf(0, 2, 2, 0, 2, 0)),
            Shape(1, listOf(null, 0, 2, 2, 1, 2)),
            Shape(2, listOf(null, null, 0, 2, 0, 1))
        ),

        // E7 = 020100, A7 = x02020, D7 = xx0212
        ChordType.DOM7 to listOf(
            Shape(0, listOf(0, 2, 0, 1, 0, 0)),            // E7 shape
            Shape(1, listOf(null, 0, 2, 0, 2, 0)),          // A7 shape
            Shape(2, listOf(null, null, 0, 2, 1, 2))        // D7 shape
        ),

        // E7sus4 = 020230, A7sus4 = x02030, D7sus4 = xx0213
        ChordType.SEVEN_SUS4 to listOf(
            Shape(0, listOf(0, 2, 0, 2, 3, 0)),
            Shape(1, listOf(null, 0, 2, 0, 3, 0)),
            Shape(2, listOf(null, null, 0, 2, 1, 3))
        ),

        // E7b5 = 010130, A7b5 = x01020 (no high e)
        ChordType.SEVEN_B5 to listOf(
            Shape(0, listOf(0, 1, 0, 1, 3, null)),
            Shape(1, listOf(null, 0, 1, 0, 2, null))
        ),

        // E7b9 = 020101, A7b9 = x02323
        ChordType.SEVEN_B9 to listOf(
            Shape(0, listOf(0, 2, 0, 1, 0, 1)),
            Shape(1, listOf(null, 0, 2, 3, 2, 3))
        ),

        // Hendrix shape — A-shape from A string root: x-R-(R-1)-R-(R+1)-x
        ChordType.SEVEN_SHARP9 to listOf(
            Shape(1, listOf(null, 0, -1, 0, 1, null))
        ),

        // C7#11 = x3x354 (root on A string)
        ChordType.SEVEN_SHARP11 to listOf(
            Shape(1, listOf(null, 0, null, 0, 2, 1))
        ),

        // Emaj7 = 021100, Amaj7 = x02120
        ChordType.MAJ7 to listOf(
            Shape(0, listOf(0, 2, 1, 1, 0, 0)),            // Emaj7 shape
            Shape(1, listOf(null, 0, 2, 1, 2, 0))           // Amaj7 shape
        ),

        // Cmaj9 = x32430 (root on A string)
        ChordType.MAJ9 to listOf(
            Shape(1, listOf(null, 0, -1, 1, 0, -3))
        ),

        // Em7 = 022030, Am7 = x02010, Dm7 = xx0211
        ChordType.MIN7 to listOf(
            Shape(0, listOf(0, 2, 2, 0, 3, 0)),            // Em7 shape
            Shape(1, listOf(null, 0, 2, 0, 1, 0)),          // Am7 shape
            Shape(2, listOf(null, null, 0, 2, 1, 1))        // Dm7 shape
        ),

        // Em9 = 022032, Cm9 = x31333
        ChordType.MIN9 to listOf(
            Shape(0, listOf(0, 2, 2, 0, 3, 2)),
            Shape(1, listOf(null, 0, -2, 0, 0, 0))
        ),

        // Cm11 = x31331 (Cm9 with 11 on top); also full barre Em11 = 000000
        ChordType.MIN11 to listOf(
            Shape(0, listOf(0, 0, 0, 0, 0, 0)),
            Shape(1, listOf(null, 0, -2, 0, 0, -2))
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

        // Cmaj13 = x32455 (R, 3, 7, 9, 13)
        ChordType.MAJ13 to listOf(
            Shape(1, listOf(null, 0, -1, 1, 2, 2))
        ),
    )

    fun getVoicingsForCustomType(root: Note, toneOffsets: List<Int>): List<ChordVoicing> {
        if (toneOffsets.size < 2) return emptyList()
        val requiredSemitones = toneOffsets.map { (root.semitone + it).mod(12) }.toSet()
        val rootIncluded = 0 in toneOffsets
        val voicings = mutableListOf<ChordVoicing>()

        for (rootString in 0..2) {
            val baseRootFret = (root.semitone - OPEN[rootString] + 12).mod(12)
            for (octave in 0..2) {
                val rootFret = baseRootFret + octave * 12
                if (rootFret > 15) continue

                val frets = MutableList<Int?>(6) { null }
                val startFret: Int = if (rootIncluded) {
                    rootFret
                } else {
                    val openSemi = OPEN[rootString]
                    var best: Int? = null
                    var bestDist = Int.MAX_VALUE
                    for (semi in requiredSemitones) {
                        for (oct in 0..1) {
                            val fret = (semi - openSemi + 12).mod(12) + oct * 12
                            if (fret < 0 || fret > 19) continue
                            val dist = kotlin.math.abs(fret - rootFret)
                            if (dist < bestDist) {
                                bestDist = dist
                                best = fret
                            }
                        }
                    }
                    best ?: continue
                }
                frets[rootString] = startFret
                var currentMin = startFret
                var currentMax = startFret

                for (s in (rootString + 1)..5) {
                    val openSemi = OPEN[s]
                    var bestFret: Int? = null
                    var bestDist = Int.MAX_VALUE
                    for (semi in requiredSemitones) {
                        for (oct in 0..1) {
                            val fret = (semi - openSemi + 12).mod(12) + oct * 12
                            if (fret < 0 || fret > 19) continue
                            val newMin = minOf(currentMin, fret)
                            val newMax = maxOf(currentMax, fret)
                            if (newMax - newMin > 3) continue
                            val dist = kotlin.math.abs(fret - rootFret)
                            if (dist < bestDist) {
                                bestDist = dist
                                bestFret = fret
                            }
                        }
                    }
                    if (bestFret != null) {
                        frets[s] = bestFret
                        currentMin = minOf(currentMin, bestFret)
                        currentMax = maxOf(currentMax, bestFret)
                    }
                }

                val nonNull = frets.filterNotNull()
                if (nonNull.size >= 3) {
                    val hasOpen = nonNull.any { it == 0 }
                    val baseFret = if (hasOpen) 1 else nonNull.filter { it > 0 }.minOrNull() ?: 0
                    voicings.add(ChordVoicing(frets, baseFret))
                    break
                }
            }
        }
        return voicings.distinct()
    }

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
