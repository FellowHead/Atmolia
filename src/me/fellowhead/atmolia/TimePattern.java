package me.fellowhead.atmolia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TimePattern {
    private ArrayList<Double> noteStarts;

    public Double[] getStarts() {
        return noteStarts.toArray(new Double[0]);
    }

    public TimePattern(Double[] starts) {
        this.noteStarts = new ArrayList<>(Arrays.asList(starts));
    }

    public static TimePattern random(Random r) {
        double step = (r.nextFloat() <= 0.5f) ? (1.0 / 32) : (1.0 / 48);

        ArrayList<Double> starts = new ArrayList<>();
        for (double t = 0; t < 1; t += (r.nextInt(5) + 5) * step) {
            starts.add(t);
        }
        return new TimePattern(starts.toArray(new Double[0]));
    }
}
