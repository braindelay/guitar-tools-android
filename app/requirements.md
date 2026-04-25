# Guitar Tools

A guitar training tool for learning scales, diatonic chord voicings, chord progressions, and improvisation techniques.

A bottom navigation bar (icon-only, no text labels) switches between six modes: **Scales**, **Chords**, **Progression**, **Metronome**, **Exercises**, and **Help**.

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

Fullscreen mode is also entered automatically when a progression starts playing, and exited automatically when the progression stops. The user can return to normal view manually by pinching out at any time.

When the Scales screen is in fullscreen, the bottom navigation bar is hidden. Swipe up to reveal it; swipe down to hide it again.

In fullscreen, swiping right from the left edge of the screen slides in a **Diatonic Chords** drawer (160 dp wide in landscape; 48 dp wide in portrait with rotated chips). Tapping a chord chip overlays the arpeggio and closes the drawer. Swipe the drawer left or tap outside it to dismiss without selecting.

When a progression is playing, the current chord name is shown as an overlay on the fullscreen fretboard (top-centre in landscape; right-edge rotated in portrait).

### Diatonic chord sidebar

To the left of the fretboard a scrollable column (120 dp wide, 216 dp tall) lists the 7 diatonic chords for the current scale as filter chips (e.g. "I: C Maj", "II: D Min"). Tapping a chip highlights the full 1-3-5-7 arpeggio of that diatonic chord across the entire fretboard; each arpeggio note is coloured by its scale degree using the same colour scheme as the plain scale view, and labelled with its interval (R, 3, 5, 7…). Tapping the active chip again clears the overlay.

### Chord voicing bottom sheet

Tapping any highlighted scale note opens a **bottom sheet** titled "[Note] — choose chord type" containing all 16 chord types as filter chips:

- Major, Minor, Dom 7, Maj7, Min7
- Dim, Dim7, Aug, Sus2, Sus4
- HalfDim, Min/Maj7, 6, 9, 6/9, 13

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

Each diagram renders a 4-fret window of the guitar neck showing fingering positions for one voicing. Notes are colour-coded by chord degree (root, third, other). Muted strings are shown with a red X. Voicings are generated from CAGED shapes (E, A, and D positions where applicable) plus an open-position voicing from the open chord library where available; duplicates are removed and results are sorted by fret position from low to high. Tapping a diagram plays the voicing using Karplus-Strong synthesis.

---

## Progression

### Layout

In landscape:
- **Left panel (62%)**: Chord picker — Circle of Fifths, chord type filter chips (scrollable column), and a voicings preview for the current selection.
- **Right panel (38%)**: Progression list with playback controls.

In portrait, the chord picker is on top (50%) and the progression list is below (50%), stacked vertically.

### Building a progression

Select a chord type from the scrollable chip list and tap a note on the Circle of Fifths, then press **Add** to append the chord to the progression. Each chord in the list shows its name, per-chord beat count controls, left/right reorder buttons, and a delete button.

### Per-chord beat count

Each chord entry displays its beat count (default 4). **−** and **+** buttons decrease or increase the count; the range is 1–8. The playback engine reads each chord's individual beat count and waits accordingly before advancing to the next chord.

### Playback

The current BPM is sourced from the Metronome screen and shown as a label above the progression list. A **play/pause** toggle button starts and stops looping through the progression; pressing Play also starts the metronome at the same BPM, and pressing Pause stops both simultaneously. The active chord is highlighted and its first voicing is played via Karplus-Strong synthesis. A **mute** toggle silences chord audio playback without stopping the progression or metronome.

On the final beat of each chord, the next chord's arpeggio is previewed in a semi-transparent overlay so the player can anticipate the upcoming change.

When a progression is playing, switching to the Scales tab shows the active chord's arpeggio as a colour overlay on the fretboard; each note is coloured by its scale degree using the same colour scheme as the plain scale view.

### Templates

A **Templates** button in the Progression header opens a bottom sheet of named chord sequence templates. Tapping a template name shows a preview of the chord names it would generate in the current key (derived from the Scales screen's selected note and mode). **Load** replaces the current progression; **Append** adds the template chords to the end.

Built-in templates:

| Name | Sequence |
|---|---|
| Pop I–V–vi–IV | I Maj — V Maj — vi Min — IV Maj |
| Blues I–IV–V | I Dom7 — IV Dom7 — I Dom7 — IV Dom7 — I Dom7 — V Dom7 — IV Dom7 — I Dom7 |
| Jazz ii–V–I | ii Min7 — V Dom7 — I Maj7 |
| Minor i–VII–VI–VII | i Min — VII Maj — VI Maj — VII Maj |
| Approach Notes (I–IV–V) | I Maj — IV Maj — V Maj |
| Chord Tone Landing (I–IV–V) | I Maj — IV Maj — V Maj |
| Voice Leading (I–vi–IV–V) | I Maj — vi Min — IV Maj — V Maj |
| CAGED Position (I–IV–V) | I Maj — IV Maj — V Maj |

### Saved progressions

A **Save** button (enabled when the progression is non-empty) opens a dialog where the user enters a name; confirming writes it to local storage (DataStore).

A **Saved** expandable section appears when saved progressions exist. Tapping a saved entry loads it (with a confirmation dialog if the current list is non-empty). Long-pressing a saved entry opens a rename dialog. Each entry has a delete (✕) button. Saved progressions survive app restarts.

---

## Metronome

A standalone click track that also drives the tempo for Progression playback. BPM is shared: any change on this screen takes effect immediately when the Progression loops.

### Setting the tempo

- A large BPM number display at the top shows the current tempo. Below it, an Italian tempo name updates automatically (Largo, Larghetto, Adagio, Andante, Moderato, Allegro, Vivace, Presto, Prestissimo).
- A **slider** below sets BPM in the range 20–300.
- **−5 / −1 / +1 / +5** outlined buttons allow precise nudges.
- A **Tap Tempo** button derives the BPM from tap timing. Three or more taps in rhythm average out to a stable tempo; a gap of 2.5 seconds or more resets the tap history.

### Beat display

Animated dots show each beat of the bar. The **downbeat (beat 1)** pulses in the tertiary colour; other active beats pulse in the primary colour; inactive beats are shown in the outline-variant colour. Dot size springs up on the active beat and returns to rest size.

Choose 2–8 **beats per bar** using filter chips below the dots.

### Controls

A **Muted / Sound on** tonal button toggles audio on and off — the visual beat display continues whether muted or not.

A **Start / Stop** primary button starts and stops the click. When started by Progression playback, this button reflects the same running state.

---

## Exercises

Seven structured exercises for developing chord-tone soloing and improvisation. Each card shows a difficulty badge and a one-line summary. Tapping a card expands its step-by-step instructions and (where present) a tip.

### Difficulty levels

| Level | Exercises |
|---|---|
| Beginner | Triad Arpeggios Across the Neck; Three-String Triad Shapes |
| Intermediate | Approach Notes to Chord Tones; Scale Runs Landing on Chord Tones; Triad Pairs |
| Advanced | Voice Leading Through Chord Changes; CAGED Position Soloing |

### Difficulty filter

Filter chips at the top of the list — **All**, **Beginner**, **Intermediate**, **Advanced** — show only cards matching the selected difficulty. Tapping the active filter deselects it and returns to All.

### Exercise cards

Each card shows:
- Title and difficulty badge (colour-coded: primary = Beginner, secondary = Intermediate, tertiary = Advanced)
- One-line summary
- Expand/collapse chevron

When expanded, numbered steps and an optional italic tip are shown below a divider.

---

## Help

A scrollable help screen with a search bar at the top. Typing filters sections by title and keywords; an empty-state message is shown when no sections match.

Content mirrors the README Usage Guide (excluding the build instructions), organised into sections: an intro paragraph, Scales, Fretboard Options, Chord Voicings, Arpeggio Overlays, Fullscreen Mode, Chords, Progression (including Templates, Saved Progressions, and Playback subsections), Metronome, and Exercises. Screenshots from the `drawable-nodpi` resource folder are embedded inline. Key terms are rendered in bold.

The Scales section includes a degree colour legend — a row of seven labelled chips (I–VII) each filled with its corresponding scale degree colour — so users can cross-reference the fretboard colours without leaving the help screen.

---

## Audio

All audio uses Karplus-Strong plucked-string synthesis at 44 100 Hz, mixed in mono. Multiple strings are summed and normalised. A metronome click (accented on beat 1, unaccented on remaining beats) plays when the metronome is running — either started directly from the Metronome screen or automatically when Progression playback begins. Audio runs on background threads; no Android permissions are required.

---

## Proposed Requirements

---

### Capo Support (Scales screen)

A capo position selector appears in the Scales top bar or Root & Scale card. The user picks a fret (0–7, default 0; 0 = no capo). When set to fret N:

- The fretboard is rendered starting at fret N. Fret N is shown as a thick coloured bar representing the capo; frets below it are greyed out and non-interactive.
- All note and scale calculations are unchanged — the capo simply shifts which physical positions are visible and tappable. A fret number in the diagram is rendered relative to the capo position (so the first open-position fret after the capo is labelled as fret 1 in the capo view, or the absolute fret number is shown with the capo label alongside — either convention is acceptable).
- The capo value persists while the user changes root and mode, and resets when the user sets it back to 0.
- Chord voicing overlays and arpeggio overlays continue to work; positions below the capo are not shown.
