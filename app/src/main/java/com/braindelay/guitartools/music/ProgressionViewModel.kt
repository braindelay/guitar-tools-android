package com.braindelay.guitartools.music

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.braindelay.guitartools.audio.GuitarAudioEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ProgressionChord(val note: Note, val chordType: ChordType, val beats: Int = 4)

class ProgressionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = SavedProgressionsRepository(application)

    var progression by mutableStateOf<List<ProgressionChord>>(emptyList())
        private set
    var playingIndex by mutableStateOf<Int?>(null)
        private set
    var nextChordIndex by mutableStateOf<Int?>(null)
        private set
    var isMuted by mutableStateOf(false)
        private set
    var savedProgressions by mutableStateOf<List<SavedProgression>>(emptyList())
        private set

    private var playJob: Job? = null

    val isPlaying: Boolean get() = playingIndex != null

    init {
        viewModelScope.launch {
            repo.savedProgressions.collect { savedProgressions = it }
        }
    }

    fun addChord(note: Note, chordType: ChordType) {
        progression = progression + ProgressionChord(note, chordType)
    }

    fun removeChord(index: Int) {
        if (index in progression.indices) {
            progression = progression.toMutableList().also { it.removeAt(index) }
        }
    }

    fun moveChordLeft(index: Int) {
        if (index > 0) {
            val m = progression.toMutableList()
            val tmp = m[index]; m[index] = m[index - 1]; m[index - 1] = tmp
            progression = m
        }
    }

    fun moveChordRight(index: Int) {
        if (index < progression.size - 1) {
            val m = progression.toMutableList()
            val tmp = m[index]; m[index] = m[index + 1]; m[index + 1] = tmp
            progression = m
        }
    }

    fun setChordBeats(index: Int, beats: Int) {
        if (index in progression.indices) {
            val m = progression.toMutableList()
            m[index] = m[index].copy(beats = beats.coerceIn(1, 8))
            progression = m
        }
    }

    fun loadTemplate(chords: List<ProgressionChord>) {
        stopPlayback()
        progression = chords
    }

    fun appendTemplate(chords: List<ProgressionChord>) {
        progression = progression + chords
    }

    fun saveProgression(name: String) {
        viewModelScope.launch { repo.save(name, progression) }
    }

    fun deleteSaved(name: String) {
        viewModelScope.launch { repo.delete(name) }
    }

    fun renameSaved(oldName: String, newName: String) {
        viewModelScope.launch { repo.rename(oldName, newName) }
    }

    fun toggleMute() { isMuted = !isMuted }

    fun playProgression(getBpm: () -> Int) {
        if (progression.isEmpty()) return
        stopPlayback()
        playJob = viewModelScope.launch {
            while (isActive) {
                val snapshot = progression
                if (snapshot.isEmpty()) break
                for (i in snapshot.indices) {
                    if (!isActive) break
                    if (i >= progression.size) break
                    playingIndex = i
                    nextChordIndex = null
                    val chord = progression[i]
                    val voicing = StandardChordLibrary.getVoicings(chord.note, chord.chordType).firstOrNull()
                    val beatMs = 60_000L / getBpm()
                    val beats = chord.beats

                    // Beat 1: chord strum
                    if (voicing != null && !isMuted) GuitarAudioEngine.playVoicing(voicing)
                    delay(beatMs)

                    if (beats > 1) {
                        // Middle beats 2..(beats-1)
                        for (b in 2 until beats) {
                            if (!isActive) break
                            delay(beatMs)
                        }

                        // Final beat: preview next chord
                        if (!isActive) break
                        val currentSize = progression.size
                        nextChordIndex = if (currentSize > 0) (i + 1) % currentSize else null
                        delay(beatMs)
                    }
                }
            }
        }
    }

    fun stopPlayback() {
        playJob?.cancel()
        playJob = null
        playingIndex = null
        nextChordIndex = null
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}
