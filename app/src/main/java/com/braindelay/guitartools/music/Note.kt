package com.braindelay.guitartools.music

enum class Note(val semitone: Int, val displayName: String) {
    C(0, "C"),
    C_SHARP(1, "C#"),
    D(2, "D"),
    D_SHARP(3, "D#"),
    E(4, "E"),
    F(5, "F"),
    F_SHARP(6, "F#"),
    G(7, "G"),
    G_SHARP(8, "G#"),
    A(9, "A"),
    A_SHARP(10, "A#"),
    B(11, "B");

    fun transpose(semitones: Int): Note {
        val newSemitone = (semitone + semitones).mod(12)
        return entries.first { it.semitone == newSemitone }
    }
}
