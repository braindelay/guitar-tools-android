package com.braindelay.guitartools.music

enum class TriadType(val label: String, val toneOffsets: List<Int>, val noteLabels: List<String>) {
    MAJOR_TRIAD ("Major Triad",   listOf(0, 4, 7),  listOf("R", "3", "5")),
    MINOR_TRIAD ("Minor Triad",   listOf(0, 3, 7),  listOf("R", "b3", "5")),
    DIM_TRIAD   ("Diminished",    listOf(0, 3, 6),  listOf("R", "b3", "b5")),
    AUG_TRIAD   ("Augmented",     listOf(0, 4, 8),  listOf("R", "3", "#5")),
    SUS_2       ("Suspended 2",   listOf(0, 2, 7),  listOf("R", "2", "5")),
    SUS_4       ("Suspended 4",   listOf(0, 5, 7),  listOf("R", "4", "5")),
    DOM_7_SHELL ("Dom 7 Shell",   listOf(0, 4, 10), listOf("R", "3", "b7")),
    MAJ_7_SHELL ("Maj 7 Shell",   listOf(0, 4, 11), listOf("R", "3", "7")),
    MIN_7_SHELL ("Min 7 Shell",   listOf(0, 3, 10), listOf("R", "b3", "b7")),
    MIN_7B5_SHELL("Min 7b5 Shell",listOf(0, 6, 10), listOf("R", "b5", "b7"));
}
