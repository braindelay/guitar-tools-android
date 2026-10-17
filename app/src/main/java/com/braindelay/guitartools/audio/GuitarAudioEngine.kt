package com.braindelay.guitartools.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.braindelay.guitartools.music.ChordVoicing
import com.braindelay.guitartools.music.Note
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

object GuitarAudioEngine {

    private const val SAMPLE_RATE = 44100

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val clickSamples by lazy { generateClick(accentuated = false) }
    private val accentClickSamples by lazy { generateClick(accentuated = true) }

    // MIDI note numbers for open strings: low E2, A2, D3, G3, B3, high E4
    private val OPEN_MIDI = intArrayOf(40, 45, 50, 55, 59, 64)

    fun midiFromPosition(string: Int, fret: Int): Int = OPEN_MIDI[string] + fret

    fun freqFromMidi(midi: Int): Float = (440.0 * 2.0.pow((midi - 69).toDouble() / 12.0)).toFloat()

    private fun karplusStrong(freq: Float, durationMs: Int = 1500): ShortArray {
        val bufSize = (SAMPLE_RATE / freq).toInt().coerceAtLeast(2)
        val totalSamples = SAMPLE_RATE * durationMs / 1000
        val ring = FloatArray(bufSize) { Random.nextFloat() * 2f - 1f }
        var pos = 0
        return ShortArray(totalSamples) {
            val s = ring[pos]
            val next = (pos + 1) % bufSize
            ring[pos] = (s + ring[next]) * 0.498f
            pos = next
            (s * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                .toShort()
        }
    }

    fun generateClick(accentuated: Boolean = false): ShortArray {
        val freq = if (accentuated) 1200.0 else 880.0
        val durationMs = 60
        val totalSamples = SAMPLE_RATE * durationMs / 1000
        return ShortArray(totalSamples) { i ->
            val envelope = exp(-i.toFloat() / (totalSamples * 0.25f))
            val wave = sin(2.0 * PI * freq * i / SAMPLE_RATE).toFloat()
            (wave * envelope * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
    }

    private fun mix(tracks: List<ShortArray>): ShortArray {
        if (tracks.isEmpty()) return ShortArray(0)
        val len = tracks.maxOf { it.size }
        return ShortArray(len) { i ->
            var sum = 0f
            for (t in tracks) if (i < t.size) sum += t[i] / Short.MAX_VALUE.toFloat()
            (sum / tracks.size * Short.MAX_VALUE).toInt()
                .coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
    }

    private suspend fun playPcm(samples: ShortArray) {
        if (samples.isEmpty()) return
        val bufBytes = samples.size * 2
        val at = try {
            AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(bufBytes)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
        } catch (_: Exception) {
            return
        }
        try {
            at.write(samples, 0, samples.size)
            at.play()
            delay(samples.size * 1000L / SAMPLE_RATE + 100)
            at.stop()
        } catch (_: Exception) {
        } finally {
            at.release()
        }
    }

    fun playNote(note: Note, octave: Int = 3) {
        scope.launch {
            val midi = (octave + 1) * 12 + note.semitone
            playPcm(karplusStrong(freqFromMidi(midi)))
        }
    }

    fun playVoicing(voicing: ChordVoicing) {
        scope.launch {
            val tracks = (0..5).mapNotNull { s ->
                val fret = voicing.stringFrets[s] ?: return@mapNotNull null
                karplusStrong(freqFromMidi(midiFromPosition(s, fret)))
            }
            playPcm(mix(tracks))
        }
    }

    fun playMidis(midis: List<Int>) {
        scope.launch {
            val tracks = midis.map { karplusStrong(freqFromMidi(it)) }
            playPcm(mix(tracks))
        }
    }

    fun playClick(accentuated: Boolean = false) {
        scope.launch { playPcm(if (accentuated) accentClickSamples else clickSamples) }
    }
}
