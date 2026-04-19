# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run unit tests
./gradlew test

# Run a single unit test class
./gradlew :app:testDebugUnitTest --tests "com.braindelay.guitartools.ExampleUnitTest"

# Run instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest

# Lint
./gradlew lint

# Clean build
./gradlew clean
```

## App Purpose

A guitar training tool for learning scales and diatonic chord voicings. The user can:

1. Select a root note from the 12-tone chromatic scale
2. Select a scale: Major, Minor, Harmonic Minor, Melodic Minor, or church modes (Dorian, Phrygian, Lydian, Mixolydian, Locrian)
3. View a 19-fret canvas fretboard with:
   - All scale notes highlighted and labelled with Roman numeral degrees (I–VII)
   - Root notes distinguished by tertiary color; other notes in primary color
4. Tap any highlighted note to open a bottom sheet of 10 triad/shell voicings
5. Select a voicing to overlay it on the fretboard with color-coded chord tones (R, 3, 5, b7…)
6. Toggle fullscreen mode for a magnified fretboard view

## Architecture

Single-module Android app (`app/`) using **MVVM + Jetpack Compose + Material3**.

- **Package**: `com.braindelay.guitartools`
- **App name**: Guitar Tools
- **Project name**: Guitar Tools
- **Min SDK**: 24, **Target SDK**: 36
- **Language**: Kotlin 2.2.10, Java 11 compatibility
- **UI**: Jetpack Compose with Material3; dynamic color enabled (Android 12+)
- **Theme**: `ui/theme/` — `Theme.kt` (`GuitarToolsTheme`), `Color.kt` (purple/pink palette), `Type.kt`
- **Dependency versions**: `gradle/libs.versions.toml`

### Entry Point

`MainActivity` — 2-second splash (`LoadingScreen`: full-screen guitar image with dark gradient overlay and "braindelay guitar tools" text at bottom) then transitions via 500ms Crossfade to `ScaleScreen`.

### State Management

`ScaleViewModel` holds all mutable state via `mutableStateOf`:
- `selectedNote`, `selectedMode` → derive `scale: Scale`
- `selectedFretPosition` → triggers voicing bottom sheet
- `selectedTriadType` → overlays chord tones on fretboard
- `isFullscreen` → controls layout mode
- Computed: `fretPositions`, `triadNotes`, `diatonicChords`, `triadSummary`

### Music Domain (`music/`)

| File | Role |
|---|---|
| `Note.kt` | Enum of 12 chromatic notes with `transpose(semitones)` |
| `Mode.kt` | Enum of 9 scales/modes, each with its interval pattern |
| `Scale.kt` | Combines root + mode; computes scale notes, Roman numerals, diatonic triad qualities |
| `Fretboard.kt` | 6-string model (19 frets); `noteAt(string, fret)`, `positionsForScale(scale)` |
| `FretPosition.kt` | Data class `(string, fret)` |
| `TriadType.kt` | Enum of 10 voicings (triads + 7-shell shapes) with semitone offsets and tone labels |

#### Modes (`Mode.kt`)

| Enum | Display Name |
|---|---|
| `MAJOR` | Major |
| `MINOR` | Minor |
| `DORIAN` | Dorian |
| `PHRYGIAN` | Phrygian |
| `LYDIAN` | Lydian |
| `MIXOLYDIAN` | Mixolydian |
| `LOCRIAN` | Locrian |
| `HARMONIC_MINOR` | Harmonic Minor |
| `MELODIC_MINOR` | Melodic Minor |

#### Voicings (`TriadType.kt`)

Major Triad, Minor Triad, Diminished, Augmented, Suspended 2, Suspended 4, Dom 7 Shell, Maj 7 Shell, Min 7 Shell, Min 7b5 Shell.

### UI (`ui/`)

| File | Role |
|---|---|
| `ScaleScreen.kt` | Main screen: top bar ("braindelay guitar tools"), hero banner, expandable root/scale chips (FlowRow for scales), diatonic chord chips, scrollable fretboard, bottom sheet voicing picker (3-column grid), fullscreen toggle |
| `FretboardView.kt` | Canvas composable drawing strings, frets, nut, inlay dots, note circles; handles tap gestures → `ScaleViewModel` |

### Data Flow

```
User tap (note/scale chip)  → ScaleViewModel.selectNote/Mode
                            → scale recomputed → fretPositions updated → FretboardView redraws

User tap (fret)             → ScaleViewModel.selectFretPosition
                            → ModalBottomSheet opens with diatonic voicings highlighted

User selects voicing        → ScaleViewModel.selectTriadType
                            → triadNotes computed → fretboard redraws with chord tone colors

Tap empty fret area         → ScaleViewModel.enterFullscreen / exitFullscreen
```
