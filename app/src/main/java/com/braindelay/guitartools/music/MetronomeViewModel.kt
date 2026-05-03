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

class MetronomeViewModel : ViewModel() {

    private var _bpm by mutableIntStateOf(80)
    val bpm: Int get() = _bpm

    private var _beatsPerBar by mutableIntStateOf(4)
    val beatsPerBar: Int get() = _beatsPerBar

    private var _currentBeat by mutableIntStateOf(0)
    val currentBeat: Int get() = _currentBeat

    private var _isPlaying by mutableStateOf(false)
    val isPlaying: Boolean get() = _isPlaying

    private var _isMuted by mutableStateOf(false)
    val isMuted: Boolean get() = _isMuted

    private var playJob: Job? = null
    private val tapTimes = mutableListOf<Long>()

    fun setBpm(value: Int) {
        _bpm = value.coerceIn(20, 300)
    }

    fun setBeatsPerBar(value: Int) {
        _beatsPerBar = value
    }

    fun toggleMute() {
        _isMuted = !_isMuted
    }

    fun tapTempo() {
        val now = System.currentTimeMillis()
        if (tapTimes.isNotEmpty() && now - tapTimes.last() > 2500L) tapTimes.clear()
        tapTimes.add(now)
        if (tapTimes.size > 8) tapTimes.removeAt(0)
        if (tapTimes.size >= 2) {
            val avgInterval = (tapTimes.last() - tapTimes.first()).toFloat() / (tapTimes.size - 1)
            setBpm((60_000f / avgInterval).toInt())
        }
    }

    fun start() {
        if (_isPlaying) return
        _isPlaying = true
        _currentBeat = 0
        playJob = viewModelScope.launch {
            var beatIndex = 0
            while (isActive) {
                _currentBeat = beatIndex % _beatsPerBar
                if (!_isMuted) GuitarAudioEngine.playClick(accentuated = _currentBeat == 0)
                delay(60_000L / _bpm)
                beatIndex++
            }
        }
    }

    fun stop() {
        playJob?.cancel()
        playJob = null
        _isPlaying = false
        _currentBeat = 0
    }

    fun togglePlay() {
        if (_isPlaying) stop() else start()
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
