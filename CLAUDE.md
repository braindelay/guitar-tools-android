# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Build signed release AAB (needs keystore.properties)
./gradlew bundleSignedRelease

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew :app:testDebugUnitTest --tests "com.braindelay.guitartools.music.MusicLogicTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Clean build
./gradlew clean
```

## App Purpose

A guitar training tool for learning scales, chords, progressions and ear training. A bottom navigation bar switches between seven modes (`AppMode` in `MainActivity.kt`):

1. **Scales**: pick a root and scale, see it on a 19-fret canvas fretboard with degree colours. Tap a note to open a voicing picker and overlay chord tones. Supports fullscreen, left-handed layout and note-name labels.
2. **Chords**: circle of fifths plus open and standard chord voicings, with chord diagrams and audio playback.
3. **Progression**: build, play, save and load chord progressions (including templates and custom chord types). The playing chord is mirrored on the Scales fretboard.
4. **Metronome**: BPM slider, tap tempo, 1–12 beats per bar, mute.
5. **Exercises**: static list of practice exercises (triad arpeggios, CAGED soloing, etc.).
6. **Ear Training**: interval, scale-degree and chord-quality drills.
7. **Help**: in-app documentation with screenshots.

## Architecture

Single-module Android app (`app/`) using **MVVM + Jetpack Compose + Material3**.

- **Package**: `com.braindelay.guitartools`
- **App name**: Guitar Tools
- **Min SDK**: 24, **Compile/Target SDK**: 37
- **Toolchain**: AGP 9 (built-in Kotlin, no `kotlin-android` plugin), Kotlin 2.4, Java 17 compatibility
- **UI**: Jetpack Compose with Material3; dynamic color enabled (Android 12+); edge-to-edge
- **Persistence**: DataStore Preferences (saved progressions, stored as JSON)
- **Theme**: `ui/theme/`: `Theme.kt` (`GuitarToolsTheme`), `Color.kt` (fallback palette), `Type.kt`
- **Dependency versions**: `gradle/libs.versions.toml`

### Entry Point

`MainActivity` shows a 2-second splash (`LoadingScreen`), then crossfades to `MainContent`. `MainContent` owns the shared `ScaleViewModel`, `ProgressionViewModel` and `MetronomeViewModel`, wires progression playback into the Scales fretboard, and hides the nav bar in Scales fullscreen mode (swipe up to reveal it).

### State Management

ViewModels hold Compose state via `mutableStateOf`/`mutableIntStateOf`:

| ViewModel | Role |
|---|---|
| `ScaleViewModel` | Selected note/mode → `scale`, fret selection, selected chord overlay, fullscreen, left-handed, label mode, current/next progression chord |
| `ProgressionViewModel` | `AndroidViewModel`; progression list, playback, saved progressions via `SavedProgressionsRepository` |
| `MetronomeViewModel` | BPM, beats per bar, tap tempo, playback loop scheduled from absolute beat times |
| `EarTrainingViewModel` | Drill type, question generation, scoring/streaks |

### Music Domain (`music/`)

| File | Role |
|---|---|
| `Note.kt` | Enum of 12 chromatic notes with `transpose(semitones)` |
| `Mode.kt` | Enum of 12 scales: 7 church modes, Harmonic/Melodic Minor, Major/Minor Pentatonic, Blues |
| `Scale.kt` | Root + mode; scale notes, Roman numerals, diatonic triad qualities |
| `Fretboard.kt` | 6-string standard-tuning model; `noteAt`, `positionsForScale` |
| `FretPosition.kt` | Data class `(string, fret)` |
| `AnyChordType.kt` | Interface for chord types (`label`, `toneOffsets`, `noteLabels`) |
| `ChordType.kt` | Enum of built-in chord types (triads, 7ths, extensions, sus, power) |
| `CustomChordType.kt` | User-defined chord type |
| `ChordVoicing.kt` | Per-string frets (null = muted) + base fret |
| `OpenChordLibrary.kt` / `StandardChordLibrary.kt` | Voicing lookups |
| `ProgressionTemplate.kt` | Degree-based progression templates |
| `SavedProgressionsRepository.kt` | DataStore-backed save/rename/delete |

### Audio (`audio/`)

`GuitarAudioEngine`: Karplus-Strong plucked-string synthesis and sine metronome clicks, played via `AudioTrack` (static mode) on a background coroutine scope.

### UI (`ui/`)

| File | Role |
|---|---|
| `ScaleScreen.kt` | Scale selector, diatonic chords, fretboard, voicing bottom sheet, fullscreen |
| `FretboardView.kt` | Canvas fretboard; tap gestures → `ScaleViewModel` |
| `ChordScreen.kt` / `ChordDiagramView.kt` / `CircleOfFifthsView.kt` | Chord browser |
| `ProgressionScreen.kt` | Progression builder and player |
| `MetronomeScreen.kt` | Metronome (portrait and landscape layouts) |
| `ExercisesScreen.kt` | Exercise list |
| `EarTrainingScreen.kt` | Ear-training drills |
| `HelpScreen.kt` | Help content |
