package com.braindelay.guitartools.music

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ScaleViewModel : ViewModel() {

    var selectedNote by mutableStateOf(Note.C)
        private set
    var selectedMode by mutableStateOf(Mode.MAJOR)
        private set
    var selectedTriadType by mutableStateOf<ChordType?>(null)
        private set
    var selectedFretPosition by mutableStateOf<FretPosition?>(null)
        private set
    var isFullscreen by mutableStateOf(false)
        private set
    var isLeftHanded by mutableStateOf(false)
        private set
    var showNoteNames by mutableStateOf(false)
        private set
    var arpeggioChordIndex by mutableStateOf<Int?>(null)
        private set
    var progressionChord by mutableStateOf<Pair<Note, AnyChordType>?>(null)
        private set
    var nextProgressionChord by mutableStateOf<Pair<Note, AnyChordType>?>(null)
        private set

    private val fretboard = Fretboard(fretCount = 19)

    val scale: Scale get() = Scale(selectedNote, selectedMode)

    val diatonicChords: List<String>
        get() {
            val s = scale
            val types = s.getDiatonicTriadTypes()
            return s.notes.mapIndexed { i, note ->
                "${Scale.ROMAN_NUMERALS[i]}: ${note.displayName} ${types[i]}"
            }
        }

    fun isDiatonic(triad: ChordType): Boolean {
        val pos = selectedFretPosition ?: return false
        val s = scale
        val rootNote = fretboard.noteAt(pos.string, pos.fret)
        val degree = s.notes.indexOf(rootNote).takeIf { it >= 0 } ?: return false
        val type = s.getDiatonicTriadTypes()[degree]
        return when (type) {
            "Maj" -> triad == ChordType.MAJOR || triad == ChordType.MAJ7 || triad == ChordType.DOM7
            "Min" -> triad == ChordType.MINOR || triad == ChordType.MIN7 || triad == ChordType.MIN_MAJ7
            "Dim" -> triad == ChordType.DIM || triad == ChordType.MIN7B5 || triad == ChordType.DIM7
            else -> false
        }
    }

    val progressionArpeggioNotes: Map<Note, String>?
        get() {
            val (note, type) = progressionChord ?: return null
            return type.toneOffsets.zip(type.noteLabels).associate { (offset, label) ->
                note.transpose(offset) to label
            }
        }

    // Notes → interval label for active chord/arpeggio overlay
    val activeOverlay: Map<Note, String>?
        get() = triadNotes ?: progressionArpeggioNotes ?: arpeggioNotes

    val nextProgressionArpeggioNotes: Map<Note, String>?
        get() {
            val (note, type) = nextProgressionChord ?: return null
            return type.toneOffsets.zip(type.noteLabels).associate { (offset, label) ->
                note.transpose(offset) to label
            }
        }

    val fretPositions: Map<FretPosition, Note>
        get() {
            val s = scale
            val overlay = activeOverlay
            val nextOverlay = nextProgressionArpeggioNotes
            val allNotes = buildSet {
                addAll(s.notes)
                overlay?.keys?.let { addAll(it) }
                nextOverlay?.keys?.let { addAll(it) }
            }
            return fretboard.positionsForNotes(allNotes)
        }

    val triadNotes: Map<Note, String>?
        get() {
            val pos = selectedFretPosition ?: return null
            val triad = selectedTriadType ?: return null
            val rootNote = fretboard.noteAt(pos.string, pos.fret)
            return triad.toneOffsets.zip(triad.noteLabels).associate { (offset, label) ->
                rootNote.transpose(offset) to label
            }
        }

    // Arpeggio tones for the selected diatonic chord degree, derived from scale positions
    val arpeggioNotes: Map<Note, String>?
        get() {
            val idx = arpeggioChordIndex ?: return null
            val s = scale
            val n = s.notes
            val quality = s.getDiatonicTriadTypes()[idx]
            val root = n[idx]
            val third = n[(idx + 2) % 7]
            val fifth = n[(idx + 4) % 7]
            val seventh = n[(idx + 6) % 7]
            val thirdLabel = if (quality == "Maj") "3" else "b3"
            val fifthLabel = when (quality) {
                "Dim" -> "b5"; "Aug" -> "#5"; else -> "5"
            }
            val seventhLabel = when (quality) {
                "Maj" -> "7"; "Min" -> "b7"; "Dim" -> "bb7"; else -> "7"
            }
            return mapOf(
                root to "R",
                third to thirdLabel,
                fifth to fifthLabel,
                seventh to seventhLabel
            )
        }

    val triadSummary: String?
        get() {
            val arpIdx = arpeggioChordIndex
            if (arpIdx != null) {
                val s = scale
                val rootNote = s.notes[arpIdx]
                val quality = s.getDiatonicTriadTypes()[arpIdx]
                val notes = arpeggioNotes?.keys?.map { it.displayName }?.joinToString(", ") ?: ""
                return "${Scale.ROMAN_NUMERALS[arpIdx]}: ${rootNote.displayName} $quality arpeggio — $notes"
            }
            val pos = selectedFretPosition ?: return null
            val triad = selectedTriadType ?: return null
            val rootNote = fretboard.noteAt(pos.string, pos.fret)
            val notes = triad.toneOffsets.map { offset -> rootNote.transpose(offset).displayName }
            return "${rootNote.displayName} ${
                triad.label.split(" (").first()
            }: ${notes.joinToString(", ")}"
        }

    fun selectNote(note: Note) {
        selectedNote = note
        selectedFretPosition = null
    }

    fun selectMode(mode: Mode) {
        selectedMode = mode
        selectedFretPosition = null
    }

    fun selectTriadType(type: ChordType) {
        selectedTriadType = type
        arpeggioChordIndex = null
    }

    fun selectFretPosition(pos: FretPosition) {
        if (selectedFretPosition == pos) {
            selectedFretPosition = null
            selectedTriadType = null
        } else {
            selectedFretPosition = pos
        }
    }

    fun clearTriadType() {
        selectedTriadType = null
    }

    fun clearFretSelection() {
        selectedFretPosition = null
        selectedTriadType = null
    }

    fun selectArpeggioChord(index: Int) {
        arpeggioChordIndex = if (arpeggioChordIndex == index) null else index
        selectedFretPosition = null
        selectedTriadType = null
    }

    fun setProgressionChord(note: Note, chordType: AnyChordType) {
        progressionChord = Pair(note, chordType)
    }

    fun clearProgressionChord() {
        progressionChord = null
    }

    fun setNextProgressionChord(note: Note, chordType: AnyChordType) {
        nextProgressionChord = Pair(note, chordType)
    }

    fun clearNextProgressionChord() {
        nextProgressionChord = null
    }

    fun noteAt(pos: FretPosition) = fretboard.noteAt(pos.string, pos.fret)

    fun toggleLeftHanded() {
        isLeftHanded = !isLeftHanded
    }

    fun toggleLabelMode() {
        showNoteNames = !showNoteNames
    }

    fun enterFullscreen() {
        isFullscreen = true
    }

    fun exitFullscreen() {
        isFullscreen = false
    }
}
