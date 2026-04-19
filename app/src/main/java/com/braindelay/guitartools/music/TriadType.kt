package com.braindelay.guitartools.music

enum class TriadType(val label: String, val toneOffsets: List<Int>, val noteLabels: List<String>) {
    MAJOR_TRIAD ("Maj",   listOf(0, 4, 7),  listOf("R", "3", "5")),
    MINOR_TRIAD ("Min",   listOf(0, 3, 7),  listOf("R", "b3", "5")),
    DIM_TRIAD   ("Dim",    listOf(0, 3, 6),  listOf("R", "b3", "b5")),
    AUG_TRIAD   ("Aug",     listOf(0, 4, 8),  listOf("R", "3", "#5")),
    SUS_2       ("Sus2",   listOf(0, 2, 7),  listOf("R", "2", "5")),
    SUS_4       ("Sus4",   listOf(0, 5, 7),  listOf("R", "4", "5")),
    DOM_7_SHELL ("Dom7",   listOf(0, 4, 10), listOf("R", "3", "b7")),
    MAJ_7_SHELL ("Maj7",   listOf(0, 4, 11), listOf("R", "3", "7")),
    MIN_7_SHELL ("Min7",   listOf(0, 3, 10), listOf("R", "b3", "b7")),
    MIN_7B5_SHELL("HalfDim",listOf(0, 6, 10), listOf("R", "b3", "b5", "b7"));
}
