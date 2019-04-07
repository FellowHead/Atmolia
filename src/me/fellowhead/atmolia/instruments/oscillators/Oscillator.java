package me.fellowhead.atmolia.instruments.oscillators;

import me.fellowhead.atmolia.AdvancedTime;
import me.fellowhead.atmolia.BeatTime;
import me.fellowhead.atmolia.instruments.Instrument;
import me.fellowhead.atmolia.theory.Note;

public abstract class Oscillator extends Instrument {
    public BeatTime attack;
    public BeatTime release;

    public Oscillator(BeatTime attack, BeatTime release) {
        this.attack = attack;
        this.release = release;
    }

    public abstract double evalWave(double time);

    @Override
    public double evaluate(Note n, AdvancedTime time) {
        if (time.beats >= n.start.beats && time.beats < n.getEnd().beats + release.beats) {
            double value = evalWave(getValue(n, time));
            double x = time.beats - n.start.beats;
            if (x <= attack.beats) {
                return value * x / attack.beats;
            }
            if (time.beats <= n.getEnd().beats) {
                return value;
            }
            return value * (1 - (time.beats - n.getEnd().beats) / release.beats);
        }
        return 0;
    }
}
