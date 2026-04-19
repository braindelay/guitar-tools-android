# Guitar Tools

A guitar training tool for learning scales, diatonic chord voicings, and chord progressions.

A bottom navigation bar switches between three modes: **Scales**, **Chords**, and **Progression**.

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

Two icon toggle buttons also appear in the top bar:
- **SwapHoriz** — toggles left-handed mode (mirrors the fretboard horizontally)
- **TextFields** — toggles label mode between Roman numerals / interval names and actual note names (C, C#, G…)

### Fretboard

A 19-fret visual fretboard shows every note in the selected scale, labelled with Roman numeral degrees (I–VII) by default, or note names when the label toggle is active. Root notes are drawn in the tertiary colour; all other scale notes in the primary colour. Inlay dots appear at frets 1, 3, 5, 7, 9, 12, 15, 17, 19.

In left-handed mode the entire fretboard is mirrored: the nut moves to the right and fret numbering increases from right to left. Tap detection mirrors accordingly.

Tapping anywhere outside a highlighted note enters **fullscreen mode**: the fretboard fills the screen with a "Go Back" button. Below the normal-view fretboard the label "Click fretboard to zoom" is shown.

### Diatonic chords and arpeggio overlays

Below the scale selector, a card lists the diatonic chords for the current scale (e.g. "I: C Maj", "II: D Min") as selectable filter chips. Tapping a chip highlights the full 1-3-5-7 arpeggio of that diatonic chord across the entire fretboard, with colour-coded degree labels (R, 3, 5, 7…). A summary card shows the chord name, quality, and constituent notes. Tapping the same chip again clears the overlay.

### Chord voicings

To the left of the fretboard a scrollable column (120 dp wide, matching the fretboard height) lists all 10 voicing types:

- Major Triad, Minor Triad, Diminished, Augmented
- Suspended 2, Suspended 4
- Dom 7 Shell, Maj 7 Shell, Min 7 Shell, Min 7b5 Shell

Selecting a voicing type persists across fret taps. When a fret position is also selected, the fretboard overlays the chord tones in colour with degree labels (R, 3, 5, b7…) and a summary card shows the chord name and its constituent notes. A **Play ▶** icon button appears next to the Clear button and plays the chord tones using Karplus-Strong synthesis. Tapping the active voicing chip deselects it. Tapping the same fret again clears both the fret selection and the voicing. The **Clear** button resets all active selections.

Diatonic voicings for the currently selected fret are highlighted with an info icon in the left column.

Selecting a diatonic chord chip (arpeggio mode) and selecting a voicing type (chord overlay mode) are mutually exclusive; activating one clears the other.

---

## Chords

The chords screen is displayed in **landscape orientation**.

### Layout

- **Left panel (38%)**: Circle of Fifths. Tap any note to select the root.
- **Right panel (62%)**: All 16 chord types are shown at once in a scrollable list. Each section shows the chord type name, a divider, and a horizontally scrollable row of chord diagrams for that type.

### Chord diagrams

Each diagram renders a 4-fret window of the guitar neck showing fingering positions for one voicing. Notes are colour-coded by chord degree (root, third, other). Muted strings are shown with a red X. Up to 3 octaves of voicings are generated per chord type, sorted by fret position from low to high. Tapping a diagram plays the voicing using Karplus-Strong synthesis.

---

## Progression

The progression screen is displayed in **landscape orientation**.

### Layout

- **Left panel (38%)**: Progression list with playback controls.
- **Right panel (62%)**: Chord picker — chord type filter chips (scrollable column), Circle of Fifths, and a voicings preview for the current selection.

### Building a progression

Select a chord type from the scrollable chip list and tap a note on the Circle of Fifths, then press **Add** to append the chord to the progression. Each chord in the list shows its name with left/right reorder buttons and a delete button.

### Playback

A BPM slider (20–240) controls chord duration (4 beats per chord at the set tempo). The play/pause button loops through the progression continuously, highlighting the active chord and playing its first voicing via Karplus-Strong synthesis. Stop ends playback.

---

## Audio

All audio uses Karplus-Strong plucked-string synthesis at 44 100 Hz, mixed in mono. Multiple strings are summed and normalised. Audio runs on background threads; no Android permissions are required.
