package com.braindelay.guitartools.music

data class ChordVoicing(
    val stringFrets: List<Int?>, // size 6, index 0 = low E; null = muted
    val baseFret: Int            // minimum non-open fret, 0 if open position
)
