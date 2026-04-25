package com.braindelay.guitartools.music

data class TemplateEntry(val degreeIndex: Int, val chordType: ChordType)

data class ProgressionTemplate(val name: String, val entries: List<TemplateEntry>) {
    fun resolve(scale: Scale): List<ProgressionChord> =
        entries.map { ProgressionChord(scale.notes[it.degreeIndex], it.chordType) }
}

object ProgressionTemplates {
    val all = listOf(
        ProgressionTemplate(
            name = "Pop I–V–vi–IV",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MINOR),
                TemplateEntry(3, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Blues I–IV–V",
            entries = listOf(
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(4, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
            )
        ),
        ProgressionTemplate(
            name = "Jazz ii–V–I",
            entries = listOf(
                TemplateEntry(1, ChordType.MIN7),
                TemplateEntry(4, ChordType.DOM7),
                TemplateEntry(0, ChordType.MAJ7),
            )
        ),
        ProgressionTemplate(
            name = "Minor i–VII–VI–VII",
            entries = listOf(
                TemplateEntry(0, ChordType.MINOR),
                TemplateEntry(6, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MAJOR),
                TemplateEntry(6, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Approach Notes (I–IV–V)",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Chord Tone Landing (I–IV–V)",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Voice Leading (I–vi–IV–V)",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MINOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "CAGED Position (I–IV–V)",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
    )
}
