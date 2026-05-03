package com.braindelay.guitartools.music

enum class ChordType(
    override val label: String,
    override val toneOffsets: List<Int>,
    override val noteLabels: List<String>
) : AnyChordType {
    POWER("5", listOf(0, 7), listOf("R", "5")),
    MAJOR("Major", listOf(0, 4, 7), listOf("R", "3", "5")),
    ADD9("add9", listOf(0, 4, 7, 14), listOf("R", "3", "5", "9")),
    MINOR("Minor", listOf(0, 3, 7), listOf("R", "b3", "5")),
    M_ADD9("m(add9)", listOf(0, 3, 7, 14), listOf("R", "b3", "5", "9")),
    MIN6("m6", listOf(0, 3, 7, 9), listOf("R", "b3", "5", "6")),
    DOM7("Dom 7", listOf(0, 4, 7, 10), listOf("R", "3", "5", "b7")),
    SEVEN_SUS4("7sus4", listOf(0, 5, 7, 10), listOf("R", "4", "5", "b7")),
    SEVEN_B5("7b5", listOf(0, 4, 6, 10), listOf("R", "3", "b5", "b7")),
    SEVEN_B9("7b9", listOf(0, 4, 7, 10, 13), listOf("R", "3", "5", "b7", "b9")),
    SEVEN_SHARP9("7#9", listOf(0, 4, 7, 10, 15), listOf("R", "3", "5", "b7", "#9")),
    SEVEN_SHARP11("7#11", listOf(0, 4, 7, 10, 18), listOf("R", "3", "5", "b7", "#11")),
    MAJ7("Maj7", listOf(0, 4, 7, 11), listOf("R", "3", "5", "7")),
    MAJ9("Maj9", listOf(0, 4, 7, 11, 14), listOf("R", "3", "5", "7", "9")),
    MIN7("Min7", listOf(0, 3, 7, 10), listOf("R", "b3", "5", "b7")),
    MIN9("m9", listOf(0, 3, 7, 10, 14), listOf("R", "b3", "5", "b7", "9")),
    MIN11("m11", listOf(0, 3, 7, 10, 14, 17), listOf("R", "b3", "5", "b7", "9", "11")),
    DIM("Dim", listOf(0, 3, 6), listOf("R", "b3", "b5")),
    DIM7("Dim7", listOf(0, 3, 6, 9), listOf("R", "b3", "b5", "bb7")),
    AUG("Aug", listOf(0, 4, 8), listOf("R", "3", "#5")),
    SUS2("Sus2", listOf(0, 2, 7), listOf("R", "2", "5")),
    SUS4("Sus4", listOf(0, 5, 7), listOf("R", "4", "5")),
    MIN7B5("HalfDim", listOf(0, 3, 6, 10), listOf("R", "b3", "b5", "b7")),
    MIN_MAJ7("Min/Maj7", listOf(0, 3, 7, 11), listOf("R", "b3", "5", "7")),
    SIX("6", listOf(0, 4, 7, 9), listOf("R", "3", "5", "6")),
    NINE("9", listOf(0, 4, 7, 10, 14), listOf("R", "3", "5", "b7", "9")),
    SIX_NINE("6/9", listOf(0, 4, 7, 9, 14), listOf("R", "3", "5", "6", "9")),
    THIRTEEN("13", listOf(0, 4, 7, 10, 14, 21), listOf("R", "3", "5", "b7", "9", "13")),
    MAJ13("Maj13", listOf(0, 4, 7, 11, 14, 21), listOf("R", "3", "5", "7", "9", "13"))
}
