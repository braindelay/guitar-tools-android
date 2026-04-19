package com.braindelay.guitartools.music

data class Scale(val root: Note, val mode: Mode) {

    val notes: List<Note> by lazy {
        val result = mutableListOf(root)
        var current = root
        for (interval in mode.intervals.dropLast(1)) {
            current = current.transpose(interval)
            result.add(current)
        }
        result
    }

    fun romanNumeral(note: Note): String? {
        val degree = notes.indexOf(note).takeIf { it >= 0 } ?: return null
        val numeral = ROMAN_NUMERALS[degree]
        return if (getDiatonicTriadTypes()[degree] == "Maj") numeral else numeral.lowercase()
    }

    fun getDiatonicTriadTypes(): List<String> {
        val majorPattern = listOf("Maj", "Min", "Min", "Maj", "Maj", "Min", "Dim")
        val offset = when (mode) {
            Mode.MAJOR -> 0
            Mode.DORIAN -> 1
            Mode.PHRYGIAN -> 2
            Mode.LYDIAN -> 3
            Mode.MIXOLYDIAN -> 4
            Mode.MINOR -> 5
            Mode.LOCRIAN -> 6
            Mode.HARMONIC_MINOR -> return listOf("Min", "Dim", "Aug", "Min", "Maj", "Maj", "Dim")
            Mode.MELODIC_MINOR  -> return listOf("Min", "Min", "Aug", "Maj", "Maj", "Dim", "Dim")
        }
        return List(7) { i -> majorPattern[(i + offset) % 7] }
    }

    companion object {
        val ROMAN_NUMERALS = listOf("I", "II", "III", "IV", "V", "VI", "VII")
    }
}
