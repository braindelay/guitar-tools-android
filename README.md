# Braindelay Guitar Tools

A tool to help guitarists to learn scales, chord voicings, and progressions. You can use it to visualise any scale on a 19-fret fretboard, explore diatonic chord shapes, and build chord progressions with audio playback.

## Usage Guide

### Scales

<img src="./screenshots/01_scale_selector.png" width="480"/>

1. **Choose a root note** — tap any of the 12 chromatic notes in the Root & Scale card.
2. **Choose a mode** — tap a mode chip (Major, Minor, Dorian, Harmonic Minor, etc.).
3. The fretboard updates immediately, showing every scale note labelled with Roman numeral degrees (I–VII). Root notes appear in a distinct colour.
4. **Collapse the selector** — tap the arrow in the top bar to hide the Root & Scale panel and focus on the fretboard. Tap again to restore it.

#### Fretboard options (top bar icons)
| Icon | Action |
|------|--------|
| ⇄ (SwapHoriz) | Toggle **left-handed mode** — mirrors the fretboard so the nut is on the right |
| T (TextFields) | Toggle **label mode** — switches note labels between Roman numerals (I, b3…) and note names (C, C#…) |

#### Chord voicings

- **Tap any highlighted note** on the fretboard to open a chord type picker at the bottom of the screen.

<img src="./screenshots/04_voicing_picker.png" width="480"/>

- Select a chord type to overlay its tones on the fretboard (colour-coded R / 3 / 5 / 7).

<img src="./screenshots/03_chord_overlay.png" width="480"/>

- Tap **▶** (Play) to hear the chord via synthesised audio.
- Diatonic chord types for the tapped note are marked with a dot (●).
- Press **Clear**, or tap outside the picker, to reset the overlay.

#### Arpeggio overlays
- In the **Diatonic Chords** card, tap any chord chip (e.g. "II: D Min") to highlight the full 1-3-5-7 arpeggio of that chord across the entire fretboard.
- A summary card shows the chord name, quality, and constituent notes.
- Tap the chip again or press **Clear** to remove the overlay.

#### Fullscreen mode
- Tap anywhere on the fretboard that is **not** a highlighted note to enter fullscreen zoom.
- Tap **Go Back** to return to the normal view.

---

### Chords

<img src="./screenshots/05_chords.png" width="480"/>

The Chords screen is landscape-only.

1. **Tap a note** on the circle (left panel) to set the root.
2. The right panel shows all 16 chord types at once — scroll vertically to browse them.
3. Each chord type section contains a horizontally scrollable row of voicing diagrams covering up to 3 octave positions.
4. **Tap any diagram** to hear it played back.

---

### Progression

<img src="./screenshots/06_progression.png" width="480"/>

The chord selector always shows the circle of fifths on the left, chord type chips in the middle, and example voicings on the right. In portrait the selector and progression list are stacked; in landscape they sit side by side.

#### Building a progression
1. **Tap a note** on the circle of fifths to set the root.
2. **Select a chord type** from the chip list.
3. Press **Add** to append the chord to the progression list.
4. Repeat to build up a sequence. Use the **← →** arrows to reorder chords and **✕** to delete one.

#### Playback
1. Set the **BPM** with the slider (20–240). Each chord plays for 4 beats at the chosen tempo.
2. Press **▶** to start looping through the progression. The active chord is highlighted.
3. Adjust the BPM slider at any time — the new speed takes effect on the next chord.
4. Press **⏸** to stop.
5. While playing, switch to the Scales tab to see the chord arpeggios highlighted on the fretboard in real time.
