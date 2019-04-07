package me.fellowhead.atmolia.theory;

import me.fellowhead.atmolia.BeatTime;

public class Note {
    public static final double semitone = Math.pow(2, 1f / 12);
    private static final double base = 16.351598; //C0

    public double abs;
    public BeatTime start;
    public BeatTime length;

    public BeatTime getEnd() {
        return new BeatTime(start.beats + length.beats);
    }

    public static double getHertz(double abs) {
        return base * Math.pow(semitone, abs);
    }

    public double getHertz() {
        return getHertz(abs);
    }

    @Override
    public String toString() {
        return "(" + abs + " | " + start + ", " + length + ")";
    }

    public Note(double abs, BeatTime start, BeatTime length) {
        this.abs = abs;
        this.start = start;
        this.length = length;
    }
}
