package me.fellowhead.atmolia;

import me.fellowhead.atmolia.instruments.Instrument;

import java.util.ArrayList;

public class Track {
    public Instrument instrument;
    public ArrayList<Note> notes;
    public float volume = 1;

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
}
