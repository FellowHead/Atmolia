package me.fellowhead.atmolia.instruments;

import me.fellowhead.atmolia.*;

public abstract class Instrument {
    public abstract double evaluate(Note n, AdvancedTime time);

//    private double offset;
//
//    public void setOffset(double off) {
//        offset = off;
//    }

    protected double getValue(Note n, AdvancedTime time) {
        return Atmo.frac((time.absolute() - n.start.advanced(time.bpm, time.sampleRate).absolute()) / (time.sampleRate / Note.getHertz(n.abs)));
    }
}
