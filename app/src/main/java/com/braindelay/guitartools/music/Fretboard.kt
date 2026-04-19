package com.braindelay.guitartools.music

class Fretboard(val fretCount: Int = 12) {

    // Standard tuning, string 0 = low E
    val openStrings = listOf(Note.E, Note.A, Note.D, Note.G, Note.B, Note.E)

    fun noteAt(string: Int, fret: Int): Note = openStrings[string].transpose(fret)

    fun positionsForNotes(notes: Collection<Note>): Map<FretPosition, Note> =
        buildMap {
            for (string in openStrings.indices) {
                for (fret in 0..fretCount) {
                    val note = noteAt(string, fret)
                    if (notes.contains(note)) put(FretPosition(string, fret), note)
                }
            }
        }

    fun positionsForScale(scale: Scale): Map<FretPosition, Note> =
        positionsForNotes(scale.notes)
}
