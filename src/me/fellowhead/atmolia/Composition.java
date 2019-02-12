package me.fellowhead.atmolia;

import me.fellowhead.atmolia.instruments.ZoneSmp;
import me.fellowhead.atmolia.instruments.oscillators.SimpleOscillator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Random;

public class Composition {
    //private double bpm = 120;
    private Tone tonika;
    private Chord[] chords;

    private static ZoneSmp createExampleZoneSmp() {
        String root = "resources/fl-electric/";

        try {
            return new ZoneSmp(new ZoneSmp.Zone[]{
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "1.wav").toURI()),36,24,39),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "2.wav").toURI()),42,40,45),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "3.wav").toURI()),48,46,51),
                    //new ZoneSmp.Zone(new File(Main.class.getResource(root + "4.wav").toURI()),54,52,57),
                    //new ZoneSmp.Zone(new File(Main.class.getResource(root + "5.wav").toURI()),60,58,63),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "6.wav").toURI()),66,64,69),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "7.wav").toURI()),72,70,96)
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

//        try {
//            File dir = new File(Main.class.getResource("resources/fl-grand").toURI());
//            return new ZoneSmp(dir);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public Track[] toTracks() {
        Track[] tracks = new Track[1];
        //tracks[0] = new Track(new SimpleOscillator(SimpleOscillator.OscType.SAWTOOTH, new BeatTime(0.005), new BeatTime(1.5)));
        tracks[0] = new Track(createExampleZoneSmp());

        for (int i = 0; i < chords.length; i++) {
            for (Tone tone : chords[i].getTones()) {
                Note note = new Note(tone.getAbs(5), new BeatTime(i * 4), new BeatTime(1));
                tracks[0].notes.add(note);
            }
        }

//        Tone base = new Tone(Tone.C);
//        Tone[] tones = Scale.DUR.getTones(base);
//        for (int i = 0; i < tones.length; i++) {
//            tracks[0].notes.add(new Note(tones[i].getAbs(4), new BeatTime(i * 0.5), new BeatTime(0.5)));
//        }
        //tracks[0].notes.add(new Note(12 * 3 + 7, new BeatTime(4), new BeatTime(8)));
        //tracks[0].notes.add(new Note(12 * 3 + 12, new BeatTime(8), new BeatTime(4)));
        return tracks;
    }

    public Chord[] getChords() {
        return chords;
    }

    public Composition() {
        tonika = new Tone(Tone.C);

        Random random = new Random();

        chords = new Chord[17];
        chords[0] = new Chord(tonika,true);
        for (int i = 1; i < 17; i++) {
            if (chords[i-1].getMain().equals(tonika.getDominant())) {
                chords[i] = new Chord(tonika,true);
            } else {
                //Chord control = random.nextBoolean() ? chords[0] : chords[i-1];
                Chord control = chords[0];
                if (random.nextBoolean()) {
                    chords[i] = control.getBrothers()[random.nextInt(2)];
                } else {
                    chords[i] = control.getCousins()[random.nextInt(7)];
                }
            }
        }

        for (int i = 0; i < chords.length; i++) {
            System.out.println(i + ": " + chords[i].getName());
        }
    }
}
