package com.braindelay.guitartools.music

enum class Mode(val displayName: String, val intervals: List<Int>) {
    MAJOR("Major",       listOf(2, 2, 1, 2, 2, 2, 1)),
    MINOR("Minor",       listOf(2, 1, 2, 2, 1, 2, 2)),
    DORIAN("Dorian",     listOf(2, 1, 2, 2, 2, 1, 2)),
    PHRYGIAN("Phrygian", listOf(1, 2, 2, 2, 1, 2, 2)),
    LYDIAN("Lydian",     listOf(2, 2, 2, 1, 2, 2, 1)),
    MIXOLYDIAN("Mixolydian", listOf(2, 2, 1, 2, 2, 1, 2)),
    LOCRIAN("Locrian",           listOf(1, 2, 2, 1, 2, 2, 2)),
    HARMONIC_MINOR("Harmonic Minor", listOf(2, 1, 2, 2, 1, 3, 1)),
    MELODIC_MINOR("Melodic Minor",   listOf(2, 1, 2, 2, 2, 2, 1));
}
