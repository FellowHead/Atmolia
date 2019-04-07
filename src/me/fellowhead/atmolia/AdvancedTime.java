package me.fellowhead.atmolia;

public class AdvancedTime extends BeatTime {
    public double bpm;
    public double sampleRate;
    private long bufferAbsolute = 0;

    public float seconds() {
        return ((float) (beats / bpm) * 60f);
    }

    public long absolute() {
        if (bufferAbsolute == 0) {
            bufferAbsolute = (long) ((beats / bpm) * 60.0 * sampleRate);
        }
        return bufferAbsolute;
    }

    public AdvancedTime(double beats, double bpm, double sampleRate) {
        super(beats);
        this.bpm = bpm;
        this.sampleRate = sampleRate;
    }

    public AdvancedTime(long absolute, double bpm, double sampleRate) {
        super((absolute / sampleRate) * (bpm / 60));
        this.bufferAbsolute = absolute;
        this.bpm = bpm;
        this.sampleRate = sampleRate;
    }

    @Override
    public String toString() {
        return "[" + beats + " beats | " + bpm + "bpm, " + sampleRate + "Hz]";
    }
}
