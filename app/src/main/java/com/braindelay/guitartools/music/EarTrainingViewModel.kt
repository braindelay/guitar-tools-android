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
import kotlinx.coroutines.launch

enum class EarDrill(val displayName: String) {
    INTERVAL("Interval"),
    SCALE_DEGREE("Scale Degree"),
    CHORD_QUALITY("Chord Quality"),
}

enum class IntervalMode(val displayName: String) {
    ASCENDING("Ascending"),
    DESCENDING("Descending"),
    HARMONIC("Harmonic"),
}

enum class EarChordQuality(val label: String, val offsets: List<Int>) {
    MAJOR("Major", listOf(0, 4, 7)),
    MINOR("Minor", listOf(0, 3, 7)),
    DIM("Dim", listOf(0, 3, 6)),
    AUG("Aug", listOf(0, 4, 8)),
    MAJ7("Maj7", listOf(0, 4, 7, 11)),
    MIN7("Min7", listOf(0, 3, 7, 10)),
    DOM7("Dom7", listOf(0, 4, 7, 10)),
    HALF_DIM("HalfDim", listOf(0, 3, 6, 10)),
}

object EarTraining {
    // m2, M2, m3, M3, P4, TT, P5, m6, M6, m7, M7, P8
    val INTERVAL_SEMITONES = (1..12).toList()
    val INTERVAL_LABELS =
        listOf("m2", "M2", "m3", "M3", "P4", "TT", "P5", "m6", "M6", "m7", "M7", "P8")

    val DEGREE_LABELS = listOf("I", "II", "III", "IV", "V", "VI", "VII")
    val MAJOR_DEGREE_OFFSETS = listOf(0, 2, 4, 5, 7, 9, 11)
}

class EarTrainingViewModel : ViewModel() {

    var drill by mutableStateOf(EarDrill.INTERVAL); private set

    private var _score by mutableIntStateOf(0)
    val score: Int get() = _score
    private var _attempts by mutableIntStateOf(0)
    val attempts: Int get() = _attempts
    private var _streak by mutableIntStateOf(0)
    val streak: Int get() = _streak
    private var _bestStreak by mutableIntStateOf(0)
    val bestStreak: Int get() = _bestStreak

    var intervalPool by mutableStateOf(EarTraining.INTERVAL_SEMITONES.toSet()); private set
    var intervalModes by mutableStateOf(IntervalMode.entries.toSet()); private set

    var scaleDegreeRoot by mutableStateOf(Note.C); private set

    var chordQualityPool by mutableStateOf(EarChordQuality.entries.toSet()); private set

    // null = not yet answered; true = correct; false = wrong
    var lastResult by mutableStateOf<Boolean?>(null); private set

    // Index into the active drill's `choices` list for the correct answer.
    // Set when the question is generated so the UI can highlight the right answer after submission.
    var correctChoiceIndex by mutableStateOf<Int?>(null); private set
    var pickedChoiceIndex by mutableStateOf<Int?>(null); private set
    var hasQuestion by mutableStateOf(false); private set

    private var firstMidi: Int = 60
    private var secondMidi: Int = 60
    private var chordMidis: List<Int> = emptyList()
    private var lastIntervalMode: IntervalMode = IntervalMode.ASCENDING
    private var playJob: Job? = null

    fun selectDrill(d: EarDrill) {
        if (drill == d) return
        drill = d
        resetSession()
    }

    fun resetSession() {
        _score = 0
        _attempts = 0
        _streak = 0
        lastResult = null
        correctChoiceIndex = null
        pickedChoiceIndex = null
        hasQuestion = false
    }

    fun toggleIntervalMode(m: IntervalMode) {
        intervalModes = if (m in intervalModes) intervalModes - m else intervalModes + m
    }

    fun toggleInterval(semi: Int) {
        intervalPool = if (semi in intervalPool) intervalPool - semi else intervalPool + semi
    }

    fun selectScaleDegreeRoot(n: Note) {
        scaleDegreeRoot = n
    }

    fun toggleChordQuality(q: EarChordQuality) {
        chordQualityPool = if (q in chordQualityPool) chordQualityPool - q else chordQualityPool + q
    }

    fun clearIntervalSettings() {
        intervalPool = emptySet()
        intervalModes = emptySet()
    }

    fun clearChordQualityPool() {
        chordQualityPool = emptySet()
    }

    val choices: List<String>
        get() = when (drill) {
            EarDrill.INTERVAL -> intervalPool.sorted().map { semi ->
                EarTraining.INTERVAL_LABELS[EarTraining.INTERVAL_SEMITONES.indexOf(semi)]
            }
            EarDrill.SCALE_DEGREE -> EarTraining.DEGREE_LABELS
            EarDrill.CHORD_QUALITY -> chordQualityPool.sortedBy { it.ordinal }.map { it.label }
        }

    fun nextQuestion() {
        when (drill) {
            EarDrill.INTERVAL -> {
                val pool = intervalPool.sorted()
                if (pool.isEmpty() || intervalModes.isEmpty()) return
                val semi = pool.random()
                val rootMidi = (48..67).random()
                firstMidi = rootMidi
                secondMidi = rootMidi + semi
                lastIntervalMode = intervalModes.random()
                correctChoiceIndex = pool.indexOf(semi)
            }
            EarDrill.SCALE_DEGREE -> {
                // C4 = 60. Anchor each session in the user's chosen root, octave 4.
                val rootMidi = 60 + scaleDegreeRoot.semitone
                val degree = (0..6).random()
                firstMidi = rootMidi
                secondMidi = rootMidi + EarTraining.MAJOR_DEGREE_OFFSETS[degree]
                correctChoiceIndex = degree
            }
            EarDrill.CHORD_QUALITY -> {
                val pool = chordQualityPool.sortedBy { it.ordinal }
                if (pool.isEmpty()) return
                val q = pool.random()
                val rootMidi = (48..60).random()
                chordMidis = q.offsets.map { rootMidi + it }
                correctChoiceIndex = pool.indexOf(q)
            }
        }
        lastResult = null
        pickedChoiceIndex = null
        hasQuestion = true
        playCurrent()
    }

    fun playCurrent() {
        if (!hasQuestion) return
        playJob?.cancel()
        playJob = viewModelScope.launch {
            when (drill) {
                EarDrill.INTERVAL -> when (lastIntervalMode) {
                    IntervalMode.ASCENDING -> {
                        GuitarAudioEngine.playMidis(listOf(firstMidi))
                        delay(700)
                        GuitarAudioEngine.playMidis(listOf(secondMidi))
                    }
                    IntervalMode.DESCENDING -> {
                        GuitarAudioEngine.playMidis(listOf(secondMidi))
                        delay(700)
                        GuitarAudioEngine.playMidis(listOf(firstMidi))
                    }
                    IntervalMode.HARMONIC -> {
                        GuitarAudioEngine.playMidis(listOf(firstMidi, secondMidi))
                    }
                }
                EarDrill.SCALE_DEGREE -> {
                    GuitarAudioEngine.playMidis(listOf(firstMidi))
                    delay(700)
                    GuitarAudioEngine.playMidis(listOf(secondMidi))
                }
                EarDrill.CHORD_QUALITY -> {
                    GuitarAudioEngine.playMidis(chordMidis)
                }
            }
        }
    }

    fun submit(choiceIdx: Int) {
        if (!hasQuestion || lastResult != null) return
        val correct = correctChoiceIndex ?: return
        pickedChoiceIndex = choiceIdx
        _attempts++
        if (choiceIdx == correct) {
            lastResult = true
            _score++
            _streak++
            if (_streak > _bestStreak) _bestStreak = _streak
        } else {
            lastResult = false
            _streak = 0
        }
    }

    override fun onCleared() {
        super.onCleared()
        playJob?.cancel()
    }
}
