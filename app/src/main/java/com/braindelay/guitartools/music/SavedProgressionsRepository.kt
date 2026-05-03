package com.braindelay.guitartools.music

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.json.JSONArray
import org.json.JSONObject

private val Context.dataStore by preferencesDataStore(name = "saved_progressions")
private val PROGRESSIONS_KEY = stringPreferencesKey("progressions")

data class SavedProgression(val name: String, val chords: List<ProgressionChord>)

class SavedProgressionsRepository(context: Context) {
    private val dataStore = context.dataStore

    val savedProgressions: Flow<List<SavedProgression>> = dataStore.data
        .map { prefs -> prefs[PROGRESSIONS_KEY]?.let(::deserialize) ?: emptyList() }

    suspend fun save(name: String, chords: List<ProgressionChord>) {
        dataStore.edit { prefs ->
            val current = prefs[PROGRESSIONS_KEY]?.let(::deserialize) ?: emptyList()
            val updated = current.filter { it.name != name } + SavedProgression(name, chords)
            prefs[PROGRESSIONS_KEY] = serialize(updated)
        }
    }

    suspend fun delete(name: String) {
        dataStore.edit { prefs ->
            val current = prefs[PROGRESSIONS_KEY]?.let(::deserialize) ?: emptyList()
            prefs[PROGRESSIONS_KEY] = serialize(current.filter { it.name != name })
        }
    }

    suspend fun rename(oldName: String, newName: String) {
        dataStore.edit { prefs ->
            val current = prefs[PROGRESSIONS_KEY]?.let(::deserialize) ?: emptyList()
            prefs[PROGRESSIONS_KEY] = serialize(current.map {
                if (it.name == oldName) it.copy(name = newName) else it
            })
        }
    }

    private fun serialize(list: List<SavedProgression>): String {
        val arr = JSONArray()
        list.forEach { sp ->
            val obj = JSONObject()
            obj.put("name", sp.name)
            val chordsArr = JSONArray()
            sp.chords.forEach { chord ->
                val c = JSONObject()
                c.put("note", chord.note.name)
                c.put("beats", chord.beats)
                when (val ct = chord.chordType) {
                    is ChordType -> {
                        c.put("kind", "standard")
                        c.put("chordType", ct.name)
                    }

                    is CustomChordType -> {
                        c.put("kind", "custom")
                        c.put("chordLabel", ct.label)
                        c.put("toneOffsets", JSONArray(ct.toneOffsets))
                        c.put("noteLabels", JSONArray(ct.noteLabels))
                    }

                    else -> c.put("kind", "standard")
                }
                chordsArr.put(c)
            }
            obj.put("chords", chordsArr)
            arr.put(obj)
        }
        return arr.toString()
    }

    private fun deserialize(json: String): List<SavedProgression> = try {
        val arr = JSONArray(json)
        (0 until arr.length()).mapNotNull { i ->
            val obj = arr.getJSONObject(i)
            val name = obj.getString("name")
            val chordsArr = obj.getJSONArray("chords")
            val chords = (0 until chordsArr.length()).mapNotNull { j ->
                val c = chordsArr.getJSONObject(j)
                val note =
                    Note.entries.find { it.name == c.getString("note") } ?: return@mapNotNull null
                val kind = if (c.has("kind")) c.getString("kind") else "standard"
                val type: AnyChordType = if (kind == "custom") {
                    val label = c.getString("chordLabel")
                    val offsetsArr = c.getJSONArray("toneOffsets")
                    val offsets = (0 until offsetsArr.length()).map { offsetsArr.getInt(it) }
                    val labelsArr = c.getJSONArray("noteLabels")
                    val labels = (0 until labelsArr.length()).map { labelsArr.getString(it) }
                    CustomChordType(label, offsets, labels)
                } else {
                    ChordType.entries.find { it.name == c.getString("chordType") }
                        ?: return@mapNotNull null
                }
                val beats = if (c.has("beats")) c.getInt("beats").coerceIn(1, 8) else 4
                ProgressionChord(note, type, beats)
            }
            SavedProgression(name, chords)
        }
    } catch (e: Exception) {
        emptyList()
    }
}
