package me.fellowhead.atmolia.instruments.oscillators;

import me.fellowhead.atmolia.BeatTime;

public class SimpleOscillator extends Oscillator {
    public enum OscType {
        SINE,
        SQUARE,
        TRIANGLE,
        SAWTOOTH
    }

    private OscType type;

    public SimpleOscillator(OscType type, BeatTime attack, BeatTime release) {
        super(attack, release);
        this.type = type;
    }

    public static double waveEvaluate(OscType type, double time) {
        switch (type) {
            case SINE:
                return Math.sin(2*Math.PI * time);
            case SQUARE:
                return Math.round(time) * 2 - 1;
            case TRIANGLE:
                return (time < 0.5) ? time * 4 - 1 : 3 - time * 4;
            case SAWTOOTH:
                return time * 2 - 1;
        }
        return 0;
    }

    @Override
    public double evalWave(double time) {
        return waveEvaluate(type, time);
    }
}
