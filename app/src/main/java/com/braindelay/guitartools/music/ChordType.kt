package com.braindelay.guitartools.music

enum class ChordType(val label: String, val toneOffsets: List<Int>, val noteLabels: List<String>) {
    MAJOR    ("Major",       listOf(0, 4, 7),      listOf("R", "3",  "5")),
    MINOR    ("Minor",       listOf(0, 3, 7),      listOf("R", "b3", "5")),
    DOM7     ("Dom 7",       listOf(0, 4, 7, 10),  listOf("R", "3",  "5", "b7")),
    MAJ7     ("Maj7",       listOf(0, 4, 7, 11),  listOf("R", "3",  "5", "7")),
    MIN7     ("Min7",       listOf(0, 3, 7, 10),  listOf("R", "b3", "5", "b7")),
    DIM      ("Dim",  listOf(0, 3, 6),      listOf("R", "b3", "b5")),
    DIM7     ("Dim7",       listOf(0, 3, 6, 9),   listOf("R", "b3", "b5", "bb7")),
    AUG      ("Aug",   listOf(0, 4, 8),      listOf("R", "3",  "#5")),
    SUS2     ("Sus2",       listOf(0, 2, 7),      listOf("R", "2",  "5")),
    SUS4     ("Sus4",       listOf(0, 5, 7),      listOf("R", "4",  "5")),
    MIN7B5   ("HalfDim",     listOf(0, 3, 6, 10),  listOf("R", "b3", "b5", "b7")),
    MIN_MAJ7 ("Min/Maj7",   listOf(0, 3, 7, 11),  listOf("R", "b3", "5", "7")),
    SIX      ("6",           listOf(0, 4, 7, 9),      listOf("R", "3",  "5", "6")),
    NINE     ("9",           listOf(0, 4, 7, 10, 14), listOf("R", "3",  "5", "b7", "9")),
    SIX_NINE ("6/9",         listOf(0, 4, 7, 9, 14),  listOf("R", "3",  "5", "6", "9")),
    THIRTEEN ("13",          listOf(0, 4, 7, 10, 14, 21), listOf("R", "3",  "5", "b7", "9", "13"))
}
