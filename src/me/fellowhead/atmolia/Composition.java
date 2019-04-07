package me.fellowhead.atmolia;

import me.fellowhead.atmolia.instruments.Lead2;
import me.fellowhead.atmolia.instruments.Lead3;
import me.fellowhead.atmolia.instruments.LeadSynth;
import me.fellowhead.atmolia.instruments.ZoneSmp;
import me.fellowhead.atmolia.instruments.oscillators.SimpleOscillator;
import me.fellowhead.atmolia.theory.*;

import java.io.File;
import java.net.URISyntaxException;

public class Composition {
    //private double bpm = 120;
    private Tone tonic;
    private Scale scale = Scale.MAJOR;
    private Chord[] chords;

    private static ZoneSmp createExampleZoneSmp() {
        String root = "resources/fl-electric/";

        try {
            return new ZoneSmp(new ZoneSmp.Zone[]{
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "1.wav").toURI()),36,24,39),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "2.wav").toURI()),42,40,45),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "3.wav").toURI()),48,46,51),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "4.wav").toURI()),54,52,57),
                    new ZoneSmp.Zone(new File(Main.class.getResource(root + "5.wav").toURI()),60,58,63),
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
        Track[] tracks = new Track[] {
                new Track(new SimpleOscillator(SimpleOscillator.OscType.TRIANGLE, new BeatTime(0.005), new BeatTime(1)), 0.5f),
                //new Track(new SimpleOscillator(SimpleOscillator.OscType.TRIANGLE, new BeatTime(0.1), new BeatTime(1)), 0.4f)
                new Track(new Lead3(),0.4f),
                //new Track(new LeadSynth(),0.5f)
        };
        tracks[0].arpeggio = Arpeggio.ARP_UP_DOWN(new BeatTime(0.25));
        //tracks[1].arpeggio = Arpeggio.ARP_UP_DOWN(new BeatTime(1));
        //tracks[0] = new Track(createExampleZoneSmp(), 0.5f);

        for (int i = 0; i < chords.length; i++) {
            for (Tone tone : chords[i].getPlayed()) {
                Note note = new Note(tone.getAbs(4), new BeatTime(i * 4), new BeatTime(4));
                tracks[0].notes.add(note);
            }
            tracks[0].notes.add(new Note(chords[i].getBase().getAbs(5), new BeatTime(i * 4), new BeatTime(4)));
            //tracks[0].notes.add(new Note(chords[i].getBase().getAbs(3), new BeatTime(i * 4), new BeatTime(4)));
            tracks[1].notes.add(new Note(chords[i].getBase().getAbs(2), new BeatTime(i * 4), new BeatTime(4)));
            //tracks[1].notes.add(new Note(chords[i].getBase().getAbs(1), new BeatTime(i * 4), new BeatTime(4)));
        }

//        Tone base = new Tone(Tone.C);
//        Tone[] tones = Scale.MAJOR.getPlayed(base);
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
        tonic = new Tone(Tone.C);

        ChordProgression progression = ChordProgression.random(16);
        chords = progression.toChords(tonic, scale);
        System.out.println("" + progression.getRelString(scale));
    }
}
