package me.fellowhead.atmolia.instruments;

import me.fellowhead.atmolia.AdvancedTime;
import me.fellowhead.atmolia.theory.Note;

public class Lead3 extends Instrument {
    private double oldValue;

    private double process(double time) {
        return 0.5 * (time + Math.round(time));
    }

    @Override
    public double procForward(double value, AdvancedTime time) {
        return applyLowPass(value, 10 - ((-Math.cos(0.25*Math.PI*time.beats) + 1) / 2.0) * 9);
    }

    @Override
    public double evaluate(Note n, AdvancedTime time) {
        if (time.beats >= n.start.beats && time.beats < n.getEnd().beats) {
            double[] chorus = new double[] { -0.1, 0.1 };

            double value = process(getValue(n, time));

            for (double d : chorus) {
                value += 0.5 * process(getValue(new Note(n.abs + d, n.start, n.length), time));
            }

            return value;
        }
        return 0;
    }

    private double applyLowPass(double newValue, double smoothing) {
        double filtered = oldValue + (newValue - oldValue) / smoothing;
        oldValue = filtered;
        return filtered;
    }
}
