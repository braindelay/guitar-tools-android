package com.braindelay.guitartools.music

enum class Mode(
    val displayName: String,
    val intervals: List<Int>,
    // For each scale-degree position, which entry in FretboardView's degreeColors to use.
    // Defaults to natural 0..6 for 7-note modes; pentatonic/blues remap so equivalent
    // intervals share colours with their parent diatonic mode.
    val degreeColorIndex: List<Int> = listOf(0, 1, 2, 3, 4, 5, 6)
) {
    MAJOR("Major", listOf(2, 2, 1, 2, 2, 2, 1)),
    MINOR("Minor", listOf(2, 1, 2, 2, 1, 2, 2)),
    DORIAN("Dorian", listOf(2, 1, 2, 2, 2, 1, 2)),
    PHRYGIAN("Phrygian", listOf(1, 2, 2, 2, 1, 2, 2)),
    LYDIAN("Lydian", listOf(2, 2, 2, 1, 2, 2, 1)),
    MIXOLYDIAN("Mixolydian", listOf(2, 2, 1, 2, 2, 1, 2)),
    LOCRIAN("Locrian", listOf(1, 2, 2, 1, 2, 2, 2)),
    HARMONIC_MINOR("Harmonic Minor", listOf(2, 1, 2, 2, 1, 3, 1)),
    MELODIC_MINOR("Melodic Minor", listOf(2, 1, 2, 2, 2, 2, 1)),
    MAJOR_PENTATONIC("Major Pentatonic", listOf(2, 2, 3, 2, 3), listOf(0, 1, 2, 4, 5)),
    MINOR_PENTATONIC("Minor Pentatonic", listOf(3, 2, 2, 3, 2), listOf(0, 2, 3, 4, 6)),
    BLUES("Blues", listOf(3, 2, 1, 1, 3, 2), listOf(0, 2, 3, 6, 4, 6));

    val isDiatonic: Boolean get() = intervals.size == 7
}
