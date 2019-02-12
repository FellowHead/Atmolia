package me.fellowhead.atmolia.instruments;

import me.fellowhead.atmolia.AdvancedTime;
import me.fellowhead.atmolia.Note;

public class Lead2 extends Instrument {
    private double process(double time) {
        return 0.5 * (time + Math.round(time));
    }

    @Override
    public double evaluate(Note n, AdvancedTime time) {
        if (time.beats >= n.start.beats && time.beats < n.getEnd().beats) {
            double[] chorus = new double[] { -0.1, 0.1 };

            double value = process(getValue(n, time));

            for (double d : chorus) {
                value += 0.5 * process(getValue(new Note(n.abs + d, n.start, n.length), time));
            }

            return value * (1 - (time.beats - n.start.beats) / n.length.beats);
        }
        return 0;
    }
}
