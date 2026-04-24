# Guitar Tools

A guitar training tool for learning scales, diatonic chord voicings, and chord progressions.

A bottom navigation bar switches between four modes: **Scales**, **Chords**, **Progression**, and **Help**.

---

## Scales

### Top bar

Two icon toggle buttons appear in the top bar:
- **SwapHoriz** — toggles left-handed mode (mirrors the fretboard horizontally)
- **TextFields** — toggles label mode between Roman numerals / interval names and actual note names (C, C#, G…)

An expand/collapse toggle button also appears in the top bar showing:
- "Choose scale" (with a down-arrow icon) when collapsed
- "Close" (with an up-arrow icon) when expanded

### Expandable panel

When expanded, the panel shows two cards (both collapse and expand together):

1. **Hero banner** — a 180 dp tall image card with the text "Master Your Fretboard" and "Visualise scales and diatonic chords" overlaid at the bottom.

2. **Root & Scale card** — a side-by-side layout:
   - **Left**: A 160 dp Circle of Fifths wheel (same component as the Chords screen). Tap any segment to select that note as the scale root.
   - **Right**: Mode filter chips in a wrapping flow row. Available modes: Major, Minor, Harmonic Minor, Melodic Minor, Dorian, Phrygian, Lydian, Mixolydian, Locrian.

### Fretboard

Below the expandable panel, a "Fretboard" heading shows the currently selected note and mode (e.g. "Highlighting: C Major").

A 19-fret visual fretboard shows every note in the selected scale, labelled with Roman numeral degrees (I–VII) by default, or note names when the label toggle is active. Each scale degree is drawn in a distinct theme colour: I → tertiary, II → primary, III → secondary, IV → tertiaryContainer, V → primaryContainer, VI → secondaryContainer, VII → error. Inlay dots appear at frets 1, 3, 5, 7, 9, 12, 15, 17, 19.

In left-handed mode the entire fretboard is mirrored: the nut moves to the right and fret numbering increases from right to left. Tap detection mirrors accordingly.

**Pinching in** on the fretboard enters **fullscreen mode**: the fretboard fills the screen scaled to fit the screen height. Pinch out to return to normal view. Below the normal-view fretboard the hint "Tap a note to pick chord type · pinch to zoom" is shown.

When the Scales screen is in fullscreen, the bottom navigation bar is hidden. Swipe up to reveal it; swipe down to hide it again.

In fullscreen, swiping right from the left edge of the screen slides in a **Diatonic Chords** drawer (160 dp wide). Tapping a chord chip overlays the arpeggio and closes the drawer. Swipe the drawer left or tap outside it to dismiss without selecting.

### Diatonic chord sidebar

To the left of the fretboard a scrollable column (120 dp wide, 216 dp tall) lists the 7 diatonic chords for the current scale as filter chips (e.g. "I: C Maj", "II: D Min"). Tapping a chip highlights the full 1-3-5-7 arpeggio of that diatonic chord across the entire fretboard; each arpeggio note is coloured by its scale degree using the same colour scheme as the plain scale view, and labelled with its interval (R, 3, 5, 7…). Tapping the active chip again clears the overlay.

### Chord voicing bottom sheet

Tapping any highlighted scale note opens a **bottom sheet** titled "[Note] — choose chord type" containing all 10 voicing types as filter chips:

- Major Triad, Minor Triad, Diminished, Augmented
- Suspended 2, Suspended 4
- Dom 7 Shell, Maj 7 Shell, Min 7 Shell, Min 7b5 Shell

Voicings that are diatonic to the current scale are marked with an info icon. Selecting a voicing closes the sheet and overlays the chord tones on the fretboard in colour with degree labels (R, 3, 5, b7…). Tapping the active voicing chip deselects it.

### Fretboard title row controls

When a chord voicing is active, a **Play ▶** icon button appears in the fretboard title row and plays the chord tones using Karplus-Strong synthesis. A **Clear** button is shown whenever a fret selection, voicing, or arpeggio overlay is active; pressing it resets all active selections.

Selecting a diatonic chord chip (arpeggio mode) and selecting a voicing type (chord overlay mode) are mutually exclusive; activating one clears the other.

---

## Chords

### Layout

In landscape:
- **Left panel (38%)**: Circle of Fifths. Tap any note to select the root.
- **Right panel (62%)**: All 16 chord types are shown at once in a scrollable list.

In portrait, the Circle of Fifths is on top (45% height) and the chord list is below (55% height), stacked vertically.

### Chord diagrams

Each diagram renders a 4-fret window of the guitar neck showing fingering positions for one voicing. Notes are colour-coded by chord degree (root, third, other). Muted strings are shown with a red X. Up to 3 octaves of voicings are generated per chord type, sorted by fret position from low to high. Tapping a diagram plays the voicing using Karplus-Strong synthesis.

---

## Progression

### Layout

In landscape:
- **Left panel (62%)**: Chord picker — Circle of Fifths, chord type filter chips (scrollable column), and a voicings preview for the current selection.
- **Right panel (38%)**: Progression list with playback controls.

In portrait, the chord picker is on top (50%) and the progression list is below (50%), stacked vertically.

### Building a progression

Select a chord type from the scrollable chip list and tap a note on the Circle of Fifths, then press **Add** to append the chord to the progression. Each chord in the list shows its name with left/right reorder buttons and a delete button.

### Playback

A BPM slider (20–240) controls chord duration (4 beats per chord at the set tempo). A **play/pause** toggle button starts and stops looping through the progression; the active chord is highlighted and its first voicing is played via Karplus-Strong synthesis. A **mute** toggle silences audio playback without stopping the progression. Adjust the BPM slider at any time — the new speed takes effect on the next chord.

When a progression is playing, the Scales screen fretboard shows the active chord's arpeggio as a colour overlay; each note is coloured by its scale degree using the same colour scheme as the plain scale view. On beat 4, the next chord's arpeggio is previewed in a semi-transparent overlay so the player can anticipate the upcoming change.

---

## Help

A scrollable help screen with a search bar at the top. Typing filters sections by title and keywords; an empty-state message is shown when no sections match.

Content mirrors the README Usage Guide (excluding the build instructions), organised into sections: an intro paragraph, Scales, Fretboard Options, Chord Voicings, Arpeggio Overlays, Fullscreen Mode, Chords, and Progression. Screenshots from the `drawable-nodpi` resource folder are embedded inline. Key terms are rendered in bold.

The Scales section includes a degree colour legend — a row of seven labelled chips (I–VII) each filled with its corresponding scale degree colour — so users can cross-reference the fretboard colours without leaving the help screen.

---

## Audio

All audio uses Karplus-Strong plucked-string synthesis at 44 100 Hz, mixed in mono. Multiple strings are summed and normalised. A metronome click (accented on beat 1, unaccented on beats 2–4) plays during progression playback. Audio runs on background threads; no Android permissions are required.
