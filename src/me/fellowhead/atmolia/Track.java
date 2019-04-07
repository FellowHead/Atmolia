package me.fellowhead.atmolia;

import me.fellowhead.atmolia.instruments.Instrument;
import me.fellowhead.atmolia.theory.Note;

import java.util.ArrayList;

public class Track {
    public Instrument instrument;
    public ArrayList<Note> notes;
    public float volume;

    public Arpeggio arpeggio = Arpeggio.NONE;

    private Note[] arpNotes = null;

    public Note[] getFinalNotes() {
        if (arpNotes == null) {
            arpNotes = arpeggio.arpeggiate(notes.toArray(new Note[0]));
        }
        return arpNotes;
    }

    public Track(Instrument instrument) {
        this.instrument = instrument;
        this.notes = new ArrayList<>();
        this.volume = 1;
    }

    public Track(Instrument instrument, float volume) {
        this.instrument = instrument;
        this.notes = new ArrayList<>();
        this.volume = volume;
    }

    public double evaluate(AdvancedTime time) {
        if (arpNotes == null) {
            arpNotes = arpeggio.arpeggiate(notes.toArray(new Note[0]));
        }
        double v = 0;
        for (Note n : arpNotes) {
            v += instrument.evaluate(n, time);
        }
        v = instrument.procForward(v, time);
        return v;
    }
}
