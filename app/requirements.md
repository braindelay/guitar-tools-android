# Guitar tools

Using the underlying android example project

Build a guitar training tool to teach me how to play scales, and diatonic chords


Allow me to select the following:


- Any tone in the western 12 tone scale
- A mode from the list:
  - Major
  - Minor
  - Harmonic and melodic minor
  - Other church modes
  

Give me the option to then see, on a visual representation of a guitar fretboard

- Every note in that scale
- Indicating the tone scale in roman numerals
- Add fretboard markings at the following frets: 1, 3, 5, 7, 9, 12, 15, 17, 19

Allow me to expand / collapse the selection options, so I can just see the fretboard.

In small text to the left of the icon that controls the selection options:
- When closed: "Choose scale"
- When open: "Close"

When I press a note on the fretboard
- Open a menu to offer a list of reasonable shell voicings for triads
- When one is selected, then colour the notes on the fretboard for the notes in that triad
- Include the name of the tone in the triad in the summary 
- Layout the triads in the menu as a grid, so they all fit on the screen 

Use guitar_lessons.jpeg as the app icon

If I click anywhere else on the fretboard, then show only the fretboard, and an option to go back to thge normal view. Below the fretboard, when it's not zoomed, say "Click fretboard to zoom"



Add a second section to render chords charts


- add a bottom navigation bar to switch between scales and chords
- if I pick scales then show the current features
- if I pick chords, then show me the new feature I will describe below

Show this entirely in landscape

- On the left show a dropdown of chord types (e.g. major / minor / dom7, maj7, b5 etc) and below it show the circle of fifths
- When I select a tone on the circle of fifths, and a chord type, then show reasonable samples of that chord, up to 3 octaves worth, in different positions, on the right. Sort these by fret position, from low to high.

## Future Enhancements & Ideas
- **Audio Playback**: Allow the user to tap on a chord voicing or a scale to hear it played back via synthesized audio or samples, aiding in ear training.
- **Left-Handed Mode**: Provide a toggle to flip the fretboard layout horizontally for left-handed players.
- **Interval/Degree Labels**: Give the option to toggle the labels on the fretboard markers between note names (C, D, E) and intervals relative to the root (R, b3, 5, b7).
- **Arpeggio Overlays**: In the scale view, add the ability to highlight the arpeggio shapes (1, 3, 5, 7) of the underlying diatonic chords directly over the scale patterns.
- **Metronome & Practice Mode**: Build a simple metronome directly into the tool with a feature that guides the user through scale runs at configurable tempos.
- **Chord Progression Builder**: Allow users to drag and drop chords to build custom progressions (e.g., ii-V-I) and visualize them sequentially on the fretboard.