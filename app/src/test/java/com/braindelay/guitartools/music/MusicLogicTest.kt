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
    fun testNonDiatonicTriadInFretPositions() {
        val vm = ScaleViewModel()
        vm.selectNote(Note.C)
        vm.selectMode(Mode.MAJOR)
        
        // Find a C note on the fretboard (e.g., A string 3rd fret)
        val cPosition = FretPosition(1, 3) 
        vm.selectFretPosition(cPosition)
        
        // Select Dom 7 Shell (R, 3, b7) -> C, E, Bb. Bb is not in C Major.
        val dom7 = TriadType.DOM_7_SHELL
        vm.selectTriadType(dom7)
        
        val positions = vm.fretPositions
        // Bb should be in the positions even though it's not in C Major scale
        val hasBb = positions.values.contains(Note.A_SHARP) // Bb is A# in our enum
        assertEquals(true, hasBb)
        
        val triadNotes = vm.triadNotes!!
        assertEquals("b7", triadNotes[Note.A_SHARP])
    }
}
