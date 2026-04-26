package com.braindelay.guitartools.music

data class CustomChordType(
    override val label: String,
    override val toneOffsets: List<Int>,
    override val noteLabels: List<String>
) : AnyChordType
