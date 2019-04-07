package me.fellowhead.atmolia.theory;

public class Tone {
    public static final byte C = 0;
    public static final byte Cis = 1;
    public static final byte D = 2;
    public static final byte Dis = 3;
    public static final byte E = 4;
    public static final byte F = 5;
    public static final byte Fis = 6;
    public static final byte G = 7;
    public static final byte Gis = 8;
    public static final byte A = 9;
    public static final byte Ais = 10;
    public static final byte H = 11;

    public static final String[] toneNames = new String[] {
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "H"
    };

    private int tone;
    private double pitch;

    public Tone(double abs) {
        setAbs(abs);
    }

    public Tone(Tone tone) {
        setAbs(tone.getAbs());
    }

    public double getAbs() {
        return tone + pitch;
    }

    public double getAbs(int octave) {
        return getAbs() + octave * 12;
    }

    public void setAbs(double abs) {
        tone = (int)Math.floor(abs) % 12;
        pitch = abs % 1.0;
    }

    public String getName() {
        return toneNames[tone % 12];
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Tone && ((Tone) o).tone == tone;
    }

    //Dur
    public Tone getDominant() {
        return new Tone(tone + pitch + 7);
    }

    public Tone getSubDominant() {
        return new Tone(tone + pitch + 6);
    }
}
