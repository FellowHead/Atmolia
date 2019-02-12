package me.fellowhead.atmolia;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class Atmo {
    public static int abs(int tone, int oct) {
        return tone + oct * 12;
    }

    public static void saveToFile(byte[] bytes, AudioFormat format, File file) {
        InputStream b_in = new ByteArrayInputStream(bytes);
        try {
            AudioInputStream stream = new AudioInputStream(b_in, format,
                    bytes.length);
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE, file);
            System.out.println("Saved to file " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static Chord[] createRandomProgression() {
//        ArrayList<Chord> chords = new ArrayList<>();
//        Random r = new Random();
//        Chord.ChordType[] validTypes = new Chord.ChordType[] {
//                //Chord.ChordType.Maj,
//                Chord.ChordType.Min,
//                //Chord.ChordType.Maj7,
//                //Chord.ChordType.Min7
//        };
//        //validTypes = Chord.ChordType.values();
//        Chord current = new Chord(r.nextInt(12), validTypes[r.nextInt(validTypes.length)]);
//
//        for (int i = 0; i < 100; i++) {
//            chords.add(current);
//            int[] tones = new int[1];
//            for (int t = 0; i < tones.length; i++) {
//                tones[t] = current.getTones()[r.nextInt(current.getTones().length)];
//            }
//            Chord[] matches = Chord.findMatches(tones);
//            ArrayList<Chord> choices = new ArrayList<>();
//            for (Chord match : matches) {
//                for (Chord.ChordType valid : validTypes) {
//                    if (match.type == valid) {
//                        choices.add(match);
//                        break;
//                    }
//                }
//            }
//            current = choices.get(r.nextInt(choices.size()));
//        }
//
//        return chords.toArray(new Chord[0]);
//    }

    public static double frac(double v) {
        return v % 1.0;
    }
}
