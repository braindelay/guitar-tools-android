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

    var selectedTriadType by mutableStateOf<TriadType?>(null)
        private set

    var selectedFretPosition by mutableStateOf<FretPosition?>(null)
        private set

    var isFullscreen by mutableStateOf(false)
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

    fun isDiatonic(triad: TriadType): Boolean {
        val pos = selectedFretPosition ?: return false
        val s = scale
        val rootNote = fretboard.noteAt(pos.string, pos.fret)
        val degree = s.notes.indexOf(rootNote).takeIf { it >= 0 } ?: return false
        val type = s.getDiatonicTriadTypes()[degree]
        
        return when (type) {
            "Maj" -> triad == TriadType.MAJOR_TRIAD || triad == TriadType.MAJ_7_SHELL || triad == TriadType.DOM_7_SHELL
            "Min" -> triad == TriadType.MINOR_TRIAD || triad == TriadType.MIN_7_SHELL
            "Dim" -> triad == TriadType.DIM_TRIAD || triad == TriadType.MIN_7B5_SHELL
            else -> false
        }
    }

    val fretPositions: Map<FretPosition, Note>
        get() {
            val s = scale
            val triad = triadNotes
            val allNotes = if (triad != null) s.notes + triad.keys else s.notes
            return fretboard.positionsForNotes(allNotes)
        }

    // Note → chord-tone label ("R", "3", "5", "7") for the selected triad, or null
    val triadNotes: Map<Note, String>?
        get() {
            val pos = selectedFretPosition ?: return null
            val triad = selectedTriadType ?: return null
            val rootNote = fretboard.noteAt(pos.string, pos.fret)
            return triad.toneOffsets.zip(triad.noteLabels).associate { (offset, label) ->
                rootNote.transpose(offset) to label
            }
        }

    val triadSummary: String?
        get() {
            val pos = selectedFretPosition ?: return null
            val triad = selectedTriadType ?: return null
            val rootNote = fretboard.noteAt(pos.string, pos.fret)
            val notes = triad.toneOffsets.map { offset ->
                rootNote.transpose(offset).displayName
            }
            return "${rootNote.displayName} ${triad.label.split(" (").getOrNull(0) ?: ""}: ${notes.joinToString(", ")}"
        }

    fun selectNote(note: Note) {
        selectedNote = note
        selectedFretPosition = null
    }

    fun selectMode(mode: Mode) {
        selectedMode = mode
        selectedFretPosition = null
    }

    fun selectTriadType(type: TriadType) {
        selectedTriadType = type
    }

    fun selectFretPosition(pos: FretPosition) {
        if (selectedFretPosition == pos) {
            selectedFretPosition = null
            selectedTriadType = null
        } else {
            selectedFretPosition = pos
            selectedTriadType = null
        }
    }

    fun clearFretSelection() {
        selectedFretPosition = null
        selectedTriadType = null
    }

    fun enterFullscreen() {
        isFullscreen = true
    }

    fun exitFullscreen() {
        isFullscreen = false
    }
}
