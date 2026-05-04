package com.braindelay.guitartools.music

data class TemplateEntry(val degreeIndex: Int, val chordType: ChordType)

data class ProgressionTemplate(val name: String, val entries: List<TemplateEntry>) {
    fun resolve(scale: Scale): List<ProgressionChord> {
        // Templates are written against 7 diatonic degrees. If the active scale is
        // pentatonic or blues, fall back to the parallel major (or minor for the
        // minor-flavoured pentatonic/blues) so degree indices resolve correctly.
        val resolved = if (scale.mode.isDiatonic) scale else when (scale.mode) {
            Mode.MAJOR_PENTATONIC -> Scale(scale.root, Mode.MAJOR)
            Mode.MINOR_PENTATONIC, Mode.BLUES -> Scale(scale.root, Mode.MINOR)
            else -> Scale(scale.root, Mode.MAJOR)
        }
        return entries.map { ProgressionChord(resolved.notes[it.degreeIndex], it.chordType) }
    }
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
        ProgressionTemplate(
            name = "Heart-and-Soul I–vi–ii–V",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MINOR),
                TemplateEntry(1, ChordType.MINOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Pachelbel I–V–vi–iii–IV–I–IV–V",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MINOR),
                TemplateEntry(2, ChordType.MINOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(3, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Mixolydian Vamp I–bVII–IV",
            entries = listOf(
                TemplateEntry(0, ChordType.MAJOR),
                TemplateEntry(6, ChordType.MAJOR),
                TemplateEntry(3, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Andalusian Cadence i–VII–VI–V",
            entries = listOf(
                TemplateEntry(0, ChordType.MINOR),
                TemplateEntry(6, ChordType.MAJOR),
                TemplateEntry(5, ChordType.MAJOR),
                TemplateEntry(4, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Dorian Vamp i–IV",
            entries = listOf(
                TemplateEntry(0, ChordType.MINOR),
                TemplateEntry(3, ChordType.MAJOR),
            )
        ),
        ProgressionTemplate(
            name = "Jazz Turnaround ii–V–I–vi",
            entries = listOf(
                TemplateEntry(1, ChordType.MIN7),
                TemplateEntry(4, ChordType.DOM7),
                TemplateEntry(0, ChordType.MAJ7),
                TemplateEntry(5, ChordType.MIN7),
            )
        ),
        ProgressionTemplate(
            name = "Minor ii–V–i",
            entries = listOf(
                TemplateEntry(1, ChordType.MIN7B5),
                TemplateEntry(4, ChordType.DOM7),
                TemplateEntry(0, ChordType.MIN7),
            )
        ),
        ProgressionTemplate(
            name = "Jazz Blues 12-bar",
            entries = listOf(
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(3, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(5, ChordType.DOM7),
                TemplateEntry(1, ChordType.MIN7),
                TemplateEntry(4, ChordType.DOM7),
                TemplateEntry(0, ChordType.DOM7),
                TemplateEntry(4, ChordType.DOM7),
            )
        ),
    )
}
