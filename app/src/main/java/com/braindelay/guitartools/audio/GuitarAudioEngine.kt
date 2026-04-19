package com.braindelay.guitartools.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.braindelay.guitartools.music.ChordVoicing
import com.braindelay.guitartools.music.Note
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow
import kotlin.math.sin

object GuitarAudioEngine {

    private const val SAMPLE_RATE = 44100

    // MIDI note numbers for open strings: low E2, A2, D3, G3, B3, high E4
    private val OPEN_MIDI = intArrayOf(40, 45, 50, 55, 59, 64)

    fun midiFromPosition(string: Int, fret: Int): Int = OPEN_MIDI[string] + fret

    fun freqFromMidi(midi: Int): Float = (440.0 * 2.0.pow((midi - 69).toDouble() / 12.0)).toFloat()

    private fun karplusStrong(freq: Float, durationMs: Int = 1500): ShortArray {
        val bufSize = (SAMPLE_RATE / freq).toInt().coerceAtLeast(2)
        val totalSamples = SAMPLE_RATE * durationMs / 1000
        val ring = FloatArray(bufSize) { Math.random().toFloat() * 2f - 1f }
        var pos = 0
        return ShortArray(totalSamples) {
            val s = ring[pos]
            val next = (pos + 1) % bufSize
            ring[pos] = (s + ring[next]) * 0.498f
            pos = next
            (s * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
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

    private fun playPcm(samples: ShortArray) {
        if (samples.isEmpty()) return
        val bufBytes = samples.size * 2
        try {
            val at = AudioTrack.Builder()
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
            at.write(samples, 0, samples.size)
            at.play()
            Thread {
                Thread.sleep(samples.size * 1000L / SAMPLE_RATE + 100)
                at.stop()
                at.release()
            }.start()
        } catch (_: Exception) {}
    }

    fun playNote(note: Note, octave: Int = 3) {
        Thread {
            val midi = (octave + 1) * 12 + note.semitone
            playPcm(karplusStrong(freqFromMidi(midi)))
        }.start()
    }

    fun playVoicing(voicing: ChordVoicing) {
        Thread {
            val tracks = (0..5).mapNotNull { s ->
                val fret = voicing.stringFrets[s] ?: return@mapNotNull null
                karplusStrong(freqFromMidi(midiFromPosition(s, fret)))
            }
            playPcm(mix(tracks))
        }.start()
    }

    fun playMidis(midis: List<Int>) {
        Thread {
            val tracks = midis.map { karplusStrong(freqFromMidi(it)) }
            playPcm(mix(tracks))
        }.start()
    }

    fun playClick(accentuated: Boolean = false) {
        Thread { playPcm(generateClick(accentuated)) }.start()
    }
}
