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

    private var _bpm by mutableIntStateOf(120)
    val bpm: Int get() = _bpm

    private var _beatsPerBar by mutableIntStateOf(4)
    val beatsPerBar: Int get() = _beatsPerBar

    var isPlaying by mutableStateOf(false)
        private set
    var currentBeat by mutableIntStateOf(0)
        private set

    private var job: Job? = null
    private var lastTapMs = 0L
    private val tapIntervals = mutableListOf<Long>()

    fun setBpm(value: Int) {
        _bpm = value.coerceIn(40, 240)
    }

    fun setBeatsPerBar(value: Int) {
        _beatsPerBar = value
        currentBeat = 0
    }

    fun start() {
        if (isPlaying) return
        isPlaying = true
        currentBeat = 0
        job = viewModelScope.launch {
            var nextBeatNs = System.nanoTime()
            while (isActive) {
                val waitMs = (nextBeatNs - System.nanoTime()) / 1_000_000
                if (waitMs > 1) delay(waitMs)
                val beat = currentBeat
                GuitarAudioEngine.playClick(beat == 0)
                currentBeat = (beat + 1) % _beatsPerBar
                nextBeatNs += 60_000_000_000L / _bpm
            }
        }
    }

    fun stop() {
        isPlaying = false
        job?.cancel()
        job = null
        currentBeat = 0
    }

    fun tapTempo() {
        val now = System.currentTimeMillis()
        if (lastTapMs > 0) {
            val interval = now - lastTapMs
            if (interval in 250..3000) {
                tapIntervals.add(interval)
                if (tapIntervals.size > 4) tapIntervals.removeAt(0)
                setBpm((60000.0 / tapIntervals.average()).toInt())
            } else {
                tapIntervals.clear()
            }
        }
        lastTapMs = now
    }

    override fun onCleared() {
        super.onCleared()
        stop()
    }
}
