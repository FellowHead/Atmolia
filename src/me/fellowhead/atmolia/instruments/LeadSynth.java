package me.fellowhead.atmolia.instruments;

import me.fellowhead.atmolia.AdvancedTime;
import me.fellowhead.atmolia.theory.Note;

public class LeadSynth extends Instrument {
    @Override
    public double evaluate(Note n, AdvancedTime time) {
        if (time.beats >= n.start.beats && time.beats < n.getEnd().beats) {
            double[] chorus = new double[] { -0.1, 0.1 };

            double value = getValue(n, time);

            for (double d : chorus) {
                value += 0.5 * getValue(new Note(n.abs + d, n.start, n.length), time);
            }

            return value * (1 - 0.2 * (time.beats - n.start.beats) / n.length.beats);
        }
        return 0;
    }
}
