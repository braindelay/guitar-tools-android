package com.braindelay.guitartools.music

import org.junit.Assert.assertEquals
import org.junit.Test

class MusicLogicTest {

    @Test
    fun testNoteTranspose() {
        assertEquals(Note.C_SHARP, Note.C.transpose(1))
        assertEquals(Note.C, Note.B.transpose(1))
        assertEquals(Note.B, Note.C.transpose(-1))
        assertEquals(Note.G, Note.E.transpose(3))
    }

    @Test
    fun testScaleNotes() {
        val cMajor = Scale(Note.C, Mode.MAJOR)
        assertEquals(listOf(Note.C, Note.D, Note.E, Note.F, Note.G, Note.A, Note.B), cMajor.notes)

        val aMinor = Scale(Note.A, Mode.MINOR)
        assertEquals(listOf(Note.A, Note.B, Note.C, Note.D, Note.E, Note.F, Note.G), aMinor.notes)
    }

    @Test
    fun testFretboardNoteAt() {
        val fretboard = Fretboard()
        assertEquals(Note.E, fretboard.noteAt(0, 0)) // Low E open
        assertEquals(Note.F, fretboard.noteAt(0, 1)) // Low E 1st fret
        assertEquals(Note.A, fretboard.noteAt(1, 0)) // A open
        assertEquals(Note.E, fretboard.noteAt(5, 0)) // High E open
    }

    @Test
    fun testScaleRomanNumeral() {
        val cMajor = Scale(Note.C, Mode.MAJOR)
        assertEquals("I", cMajor.romanNumeral(Note.C))
        assertEquals("V", cMajor.romanNumeral(Note.G))
        assertEquals(null, cMajor.romanNumeral(Note.C_SHARP))
    }

    @Test
    fun testNonDiatonicChordTones() {
        // Dom7 on C: C, E, G, Bb — Bb is not in C Major
        val dom7 = ChordType.DOM7
        val root = Note.C
        val toneNotes = dom7.toneOffsets.zip(dom7.noteLabels).associate { (offset, label) ->
            root.transpose(offset) to label
        }

        // Bb (A#) should be present even though it's outside C Major
        assertEquals(true, toneNotes.containsKey(Note.A_SHARP))
        assertEquals("b7", toneNotes[Note.A_SHARP])
    }
}
