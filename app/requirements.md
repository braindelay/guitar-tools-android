# Guitar Tools

A guitar training tool for learning scales and diatonic chord voicings.

A bottom navigation bar switches between two modes: **Scales** and **Chords**.

---

## Scales

### Scale selection

- Select any tone from the western 12-tone chromatic scale.
- Select a mode from:
  - Major
  - Minor
  - Harmonic Minor
  - Melodic Minor
  - Dorian, Phrygian, Lydian, Mixolydian, Locrian (church modes)

The selection panel can be expanded or collapsed. The toggle button in the top bar shows:
- "Choose scale" (with a down-arrow icon) when collapsed
- "Close" (with an up-arrow icon) when expanded

### Fretboard

A 19-fret visual fretboard shows every note in the selected scale, labelled with Roman numeral degrees (I–VII). Root notes are drawn in the tertiary colour; all other scale notes in the primary colour. Inlay dots appear at frets 1, 3, 5, 7, 9, 12, 15, 17, 19.

Tapping anywhere outside a highlighted note enters **fullscreen mode**: the fretboard fills the screen with a "Go Back" button. Below the normal-view fretboard the label "Click fretboard to zoom" is shown.

### Diatonic chords

Below the scale selector, a card lists the diatonic chords for the current scale (e.g. "I: C Maj", "II: D Min").

### Chord voicings

To the left of the fretboard a scrollable column (120 dp wide, matching the fretboard height) lists all 10 voicing types:

- Major Triad, Minor Triad, Diminished, Augmented
- Suspended 2, Suspended 4
- Dom 7 Shell, Maj 7 Shell, Min 7 Shell, Min 7b5 Shell

Selecting a voicing type from this column persists across fret taps. When a fret position is also selected, the fretboard overlays the chord tones in colour with degree labels (R, 3, 5, b7 …) and a summary card shows the chord name and its constituent notes. Tapping the active voicing chip deselects it. Tapping the same fret again clears both the fret selection and the voicing. The **Clear** button (visible whenever a fret or voicing is selected) resets both.

Diatonic voicings for the currently selected fret are highlighted with an info icon in the left column.

---

## Chords

The chords screen is displayed in **landscape orientation**.

### Layout

- **Left panel (38%)**: Circle of Fifths. Tap any note to select the root.
- **Right panel (62%)**: All 16 chord types are shown at once in a scrollable list. Each section shows the chord type name, a divider, and a horizontally scrollable row of chord diagrams for that type.

### Chord diagrams

Each diagram renders a 4-fret window of the guitar neck showing fingering positions for one voicing. Notes are colour-coded by chord degree (root, third, other). Muted strings are shown with a red X. Up to 3 octaves of voicings are generated per chord type, sorted by fret position from low to high.

---

## Future Enhancements & Ideas
- **Audio Playback**: Allow the user to tap on a chord voicing or a scale to hear it played back via synthesized audio or samples, aiding in ear training.
- **Left-Handed Mode**: Provide a toggle to flip the fretboard layout horizontally for left-handed players.
- **Interval/Degree Labels**: Give the option to toggle the labels on the fretboard markers between note names (C, D, E) and intervals relative to the root (R, b3, 5, b7).
- **Arpeggio Overlays**: In the scale view, add the ability to highlight the arpeggio shapes (1, 3, 5, 7) of the underlying diatonic chords directly over the scale patterns.
- **Metronome & Practice Mode**: Build a simple metronome directly into the tool with a feature that guides the user through scale runs at configurable tempos.
- **Chord Progression Builder**: Allow users to drag and drop chords to build custom progressions (e.g., ii-V-I) and visualize them sequentially on the fretboard.
