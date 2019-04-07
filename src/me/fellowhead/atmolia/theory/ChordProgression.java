package me.fellowhead.atmolia.theory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ChordProgression {
    private ArrayList<RelativeChord> prog;

    public ChordProgression(RelativeChord... prog) {
        this.prog = new ArrayList<>(Arrays.asList(prog));
    }

    public static ChordProgression simple() {
        return new ChordProgression(
                new RelativeChord(1),
                new RelativeChord(6),
                new RelativeChord(2),
                new RelativeChord(5),
                new RelativeChord(1)
        );
    }

    public static ChordProgression random(int length) {
        Random r = new Random();
        RelativeChord[] prog = new RelativeChord[length];
        for (int i = 0; i < prog.length - 2; i++) {
            prog[i] = new RelativeChord(1 + r.nextInt(6));
        }
        prog[length-2] = new RelativeChord(5);
        prog[length-1] = new RelativeChord(1);
        return new ChordProgression(prog);
    }

    public Chord[] toChords(Tone tonic, Scale scale) {
        Chord[] out = new Chord[prog.size()];
        for (int i = 0; i < prog.size(); i++) {
            out[i] = prog.get(i).toAbsolute(tonic, scale);
        }
        return out;
    }

    public String getRelString(Scale scale) {
        StringBuilder s = new StringBuilder(prog.get(0).getRelString(scale));
        for (int i = 1; i < prog.size(); i++) {
            RelativeChord chord = prog.get(i);
            s.append(" ").append(chord.getRelString(scale));
        }
        return s.toString();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder(prog.get(0).getRelString());
        for (int i = 1; i < prog.size(); i++) {
            RelativeChord chord = prog.get(i);
            s.append(" ").append(chord.getRelString());
        }
        return s.toString();
    }
}
