package me.fellowhead.atmolia;

public class BeatTime {
    public double beats;

    public AdvancedTime advanced(double bpm, double frequency) {
        return new AdvancedTime(beats, bpm, frequency);
    }

    public AdvancedTime advanced(AdvancedTime template) {
        return new AdvancedTime(beats, template.bpm, template.sampleRate);
    }

    public BeatTime(double beats) {
        this.beats = beats;
    }

    @Override
    public String toString() {
        return "[" + beats + " beats]";
    }
}
