package com.braindelay.guitartools.music

/**
 * Classic open-position chord shapes.
 * baseFret=1 on every voicing so ChordDiagramView shows the nut.
 * String order: index 0 = low E … index 5 = high e. null = muted, 0 = open string.
 */
object OpenChordLibrary {

    data class OpenChord(val chordType: ChordType, val voicing: ChordVoicing)

    // Build a ChordVoicing from 6 nullable fret values, always at open position.
    private fun v(s0: Int?, s1: Int?, s2: Int?, s3: Int?, s4: Int?, s5: Int?) =
        ChordVoicing(listOf(s0, s1, s2, s3, s4, s5), baseFret = 1)

    private val CHORDS: Map<Note, List<OpenChord>> = mapOf(

        Note.E to listOf(
            OpenChord(ChordType.MAJOR, v(0, 2, 2, 1, 0, 0)),
            OpenChord(ChordType.MINOR, v(0, 2, 2, 0, 0, 0)),
            OpenChord(ChordType.DOM7, v(0, 2, 0, 1, 0, 0)),
            OpenChord(ChordType.MAJ7, v(0, 2, 1, 1, 0, 0)),
            OpenChord(ChordType.MIN7, v(0, 2, 0, 0, 0, 0)),
            OpenChord(ChordType.SUS2, v(0, 2, 2, 2, 0, 0)),
            OpenChord(ChordType.SUS4, v(0, 2, 2, 2, 0, 0)),
        ),

        Note.A to listOf(
            OpenChord(ChordType.MAJOR, v(null, 0, 2, 2, 2, 0)),
            OpenChord(ChordType.MINOR, v(null, 0, 2, 2, 1, 0)),
            OpenChord(ChordType.DOM7, v(null, 0, 2, 0, 2, 0)),
            OpenChord(ChordType.MAJ7, v(null, 0, 2, 1, 2, 0)),
            OpenChord(ChordType.MIN7, v(null, 0, 2, 0, 1, 0)),
            OpenChord(ChordType.SUS2, v(null, 0, 2, 2, 0, 0)),
            OpenChord(ChordType.SUS4, v(null, 0, 2, 2, 3, 0)),
        ),

        Note.D to listOf(
            OpenChord(ChordType.MAJOR, v(null, null, 0, 2, 3, 2)),
            OpenChord(ChordType.MINOR, v(null, null, 0, 2, 3, 1)),
            OpenChord(ChordType.DOM7, v(null, null, 0, 2, 1, 2)),
            OpenChord(ChordType.MAJ7, v(null, null, 0, 2, 2, 2)),
            OpenChord(ChordType.MIN7, v(null, null, 0, 2, 1, 1)),
            OpenChord(ChordType.SUS2, v(null, null, 0, 2, 3, 0)),
            OpenChord(ChordType.SUS4, v(null, null, 0, 2, 3, 3)),
        ),

        Note.G to listOf(
            OpenChord(ChordType.MAJOR, v(3, 2, 0, 0, 0, 3)),
            OpenChord(ChordType.DOM7, v(3, 2, 0, 0, 0, 1)),
            OpenChord(ChordType.MAJ7, v(3, 2, 0, 0, 0, 2)),
            OpenChord(ChordType.SUS2, v(3, 0, 0, 0, 3, 3)),
            OpenChord(ChordType.SUS4, v(3, 3, 0, 0, 1, 3)),
        ),

        Note.C to listOf(
            OpenChord(ChordType.MAJOR, v(null, 3, 2, 0, 1, 0)),
            OpenChord(ChordType.MAJ7, v(null, 3, 2, 0, 0, 0)),
            OpenChord(ChordType.DOM7, v(null, 3, 2, 3, 1, 0)),
            OpenChord(ChordType.SUS2, v(null, 3, 0, 0, 3, 3)),
            OpenChord(ChordType.SUS4, v(null, 3, 3, 0, 1, 1)),
            OpenChord(ChordType.NINE, v(null, 3, 2, 0, 3, 3)),
        ),

        Note.B to listOf(
            OpenChord(ChordType.DOM7, v(null, 2, 1, 2, 0, 2)),
            OpenChord(ChordType.MIN7, v(null, 2, 0, 2, 0, 2)),
        ),

        Note.F to listOf(
            OpenChord(ChordType.MAJ7, v(null, null, 3, 2, 1, 0)),
        ),
    )

    fun getChords(root: Note): List<OpenChord> = CHORDS[root] ?: emptyList()
}
