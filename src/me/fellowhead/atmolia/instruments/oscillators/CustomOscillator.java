package me.fellowhead.atmolia.instruments.oscillators;

import me.fellowhead.atmolia.BeatTime;

public class CustomOscillator extends Oscillator {
    private double[] values;

    public CustomOscillator(double[] values, BeatTime attack, BeatTime release) {
        super(attack, release);
        this.values = values;
    }

    @Override
    public double evalWave(double time) {
        double[] places = values.clone();
        for (int i = 0; i < places.length; i++) {
            places[i] = i / (places.length - 1f);
        }
        int i1 = (int) Math.floor(time * (values.length - 1));
        double a = values[i1];
        double b = values[i1 + 1];
        double posA = places[i1];
        double posB = places[i1 + 1];
        double tScaled = (time - posA) / (posB - posA);
        return a + (b - a) * tScaled;
    }
}
