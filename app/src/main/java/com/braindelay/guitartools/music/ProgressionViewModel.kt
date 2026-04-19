package com.braindelay.guitartools.music

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.braindelay.guitartools.audio.GuitarAudioEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

data class ProgressionChord(val note: Note, val chordType: ChordType)

class ProgressionViewModel : ViewModel() {

    var progression by mutableStateOf<List<ProgressionChord>>(emptyList())
        private set
    var playingIndex by mutableStateOf<Int?>(null)
        private set
    var nextChordIndex by mutableStateOf<Int?>(null)
        private set

    private var _chordBpm by mutableIntStateOf(80)
    val chordBpm: Int get() = _chordBpm

    private var playJob: Job? = null

    val isPlaying: Boolean get() = playingIndex != null

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

    fun setChordBpm(value: Int) {
        _chordBpm = value.coerceIn(20, 240)
    }

    fun playProgression() {
        if (progression.isEmpty()) return
        stopPlayback()
        playJob = viewModelScope.launch {
            while (isActive) {
                for (i in progression.indices) {
                    if (!isActive) break
                    playingIndex = i
                    nextChordIndex = null
                    val chord = progression[i]
                    val voicing = StandardChordLibrary.getVoicings(chord.note, chord.chordType).firstOrNull()
                    if (voicing != null) GuitarAudioEngine.playVoicing(voicing)
                    delay(210_000L / _chordBpm)  // 3.5 beats
                    if (!isActive) break
                    nextChordIndex = (i + 1) % progression.size
                    delay(30_000L / _chordBpm)   // remaining 0.5 beat
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
