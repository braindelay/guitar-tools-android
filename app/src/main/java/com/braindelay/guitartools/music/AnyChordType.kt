package com.braindelay.guitartools.music

interface AnyChordType {
    val label: String
    val toneOffsets: List<Int>
    val noteLabels: List<String>
}
