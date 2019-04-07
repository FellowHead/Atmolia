package me.fellowhead.atmolia;

import me.fellowhead.atmolia.instruments.oscillators.SimpleOscillator;
import me.fellowhead.atmolia.theory.Chord;
import me.fellowhead.atmolia.theory.Note;
import me.fellowhead.atmolia.theory.Tone;

import javax.sound.sampled.AudioFormat;
import java.util.*;

public class AudioBuilder {
    public static final float frequency = 44100;
    private static final float gain = 0.2f;
    private ArrayList<Track> tracks;
    private int pos = 0;

    private static final long streamEnd = 5000000;
    private static final int fadeOut = (int)frequency * 10;
    public static final double bpm = 80;

    private Queue<Double> input = new ArrayDeque<>();
    private Queue<Double> reverb = new ArrayDeque<>();

    public ArrayList<Chord> savedChords = new ArrayList<>();

    public static byte bytesPerSmp = 1;
    public static AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, frequency, bytesPerSmp * 8, 1, bytesPerSmp, frequency, false);
    private static int bitMult = (int)Math.pow(2, bytesPerSmp * 8 - 1) - 1;

    public static void setBytesPerSmp(byte v) {
        bytesPerSmp = v;
        format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, frequency, bytesPerSmp * 8, 1, bytesPerSmp, frequency, false);
        bitMult = (int)Math.pow(2, bytesPerSmp * 8 - 1) - 1;
    }

    public AudioBuilder() {
        tracks = new ArrayList<>();

        //Random r = new Random();

//        String root = System.getProperty("user.dir") + "/src/samples/fl electric/";
//        System.out.println(root);
//
//        ZoneSmp.Zone[] zones = new ZoneSmp.Zone[] {
//                new ZoneSmp.Zone(new File(root + "1.wav"),36,24,39),
//                new ZoneSmp.Zone(new File(root + "2.wav"),42,40,45),
//                new ZoneSmp.Zone(new File(root + "3.wav"),48,46,51),
//                new ZoneSmp.Zone(new File(root + "4.wav"),54,52,57),
//                new ZoneSmp.Zone(new File(root + "5.wav"),60,58,63),
//                new ZoneSmp.Zone(new File(root + "6.wav"),66,64,69),
//                new ZoneSmp.Zone(new File(root + "7.wav"),72,70,96),
//        };

        //Track lead = new Track(new ZoneSmp(zones), 1);
        Track lead = new Track(new SimpleOscillator(SimpleOscillator.OscType.SINE, new BeatTime(0.01), new BeatTime(1)), 1);
        tracks.add(lead);
        tracks.add(lead);
        tracks.add(lead);

//        tracks.add(new Track(new ZoneSmp(zones), 0.8f));
//        //tracks.add(new Track(new SimpleOscillator(SimpleOscillator.OscType.SINE, 10000, 50000), 0.8f));

        //tracks.add(new Track(new SimpleOscillator(SimpleOscillator.OscType.SAWTOOTH, 10000, 60000), 1));
        //Instrument chords = new SimpleOscillator(SimpleOscillator.OscType.TRIANGLE, new BeatTime(1), new BeatTime(4));
        //chords.setOffset(24);
        //tracks.add(new Track(chords, 1));
        //tracks.add(new Track(new SimpleOscillator(SimpleOscillator.OscType.SINE, 10000, 1000), 1));

//        for (int t = 1; t < 8; t++) {
//            double[] wave = new double[r.nextInt(10) + 2];
//            float h = 0;
//            for (int i = 0; i < wave.length; i++) {
//                h += (r.nextFloat() * 2 - 1) * 0.25f;
//                h = Math.min(Math.max(h, -1), 1);
//                wave[i] = h;
//            }
//            //Track track = new Track(new CustomOscillator(wave, 50, 50000), 1);
//            Track track = new Track(new ZoneSmp(zones), 0.8f);
//
//            tracks.add(track);
//        }

        createNotes();
    }

    private Chord getRandomChord(Random r) {
        return Chord.build(new Tone(r.nextInt(12)), r.nextBoolean(), false);
    }

    public void createNotes() {
        //System.out.println("Creating notes at " + pos);
        //Random r = new Random();

        Composition composition = new Composition();
        tracks = new ArrayList<>(Arrays.asList(composition.toTracks()));

        pos += 50;

        savedChords = new ArrayList<>(Arrays.asList(composition.getChords()));

//        Track chordT = tracks.get(1);
//        for (int i = 0; i < chords.length; i++) {
//            Chord c = chords[i];
//            for (int n = 0; n < c.getPlayed().length; n++) {
//                //System.out.println(c.getName() + ": " + n + " | " + Atmo.getToneName(c.getPlayed()[n]));
//                chordT.notes.add(new Note(Atmo.abs(c.getPlayed()[n], 5), (pos + i * 4) * m + n * 1000, (int)(m * 4f)));
//            }
//        }
//
//        Track bassT = tracks.get(2);
//        for (int i = 0; i < chords.length; i++) {
//            Chord c = chords[i];
//            bassT.notes.add(new Note(Atmo.abs(c.getBase(), 2), (pos + i * 4) * m, (int)(m * 4f)));
//            bassT.notes.add(new Note(Atmo.abs(c.getBase(), 3), (pos + i * 4) * m, (int)(m * 4f)));
//        }
//
//        Track leadT = tracks.get(0);
//        for (int i = 0; i < chords.length; i++) {
//            Chord c = chords[i];
//            TimePattern pattern = TimePattern.random(r);
//            for (double d : pattern.getStarts()) {
//                leadT.notes.add(new Note(Atmo.abs(c.getScale()[r.nextInt(c.getScale().length)], 5), (int)((pos + d * 4 + i * 4) * m), (int)(m * 2f)));
//            }
//        }
//
//        pos += chords.length * 4;
//        savedChords.addAll(Arrays.asList(chords));
    }

    public Track[] getTracks() {
        return tracks.toArray(new Track[0]);
    }

    public byte[] getBytes(long start, int len) {
        byte[] out = new byte[len];
        //Arrays.fill(out, (byte) 0);

//        while (new AdvancedTime((double)pos, bpm, sampleRate).absolute() < start + len) {
//            createNotes();
//        }

        for (int i = 0; i < len; i += bytesPerSmp) {
            AdvancedTime at = new AdvancedTime((start + i) / bytesPerSmp, bpm, frequency);
            double glued = 0;

            double m = gain;
            if (start + i >= streamEnd) {
                break;
            }
            if (start + i >= streamEnd - fadeOut) {
                m *= (streamEnd - start + i) / (double)fadeOut;
            }

            for (Track t : tracks) {
                if (t.volume > 0) {
                    //System.out.println(at.beats);
                    double ev = t.evaluate(at);
                    glued += ev * t.volume;

//                    ArrayList<Note> removable = new ArrayList<>();
//                    for (Note n : t.notes) {
//                        long noteStart = n.start.advanced(bpm, frequency).absolute();
//                        long noteLength = n.length.advanced(bpm, frequency).absolute();
//
//                        if (noteStart <= start + 50000 && (noteStart + noteLength) <= start + 200000) {
//                            if (noteStart > frequency * bytesPerSmp * 20 && (noteStart + noteLength) < start - 500000) {
//                                removable.add(n);
//                            }
//
//                        }
//                    }
//                    t.notes.removeAll(removable);
                }
            }

//            reverb.add(input.element());
//
//            if (reverb.size() > 100) {
//                glued += reverb.remove();
//            }

            //input.add(glued);

            int value = (int)(bitMult * m * glued);
            //System.out.println(glued + " | " + value);

            for (int b = 0; b < bytesPerSmp; b++) {
                out[i + b] += (byte)((value >> (b * 8)));
            }

//            String s = "";
//            int all = 0;
//            for (int b = 0; b < bytesPerSmp; b++) {
//                all += (int)out[i + b] << (b * 8);
//            }

            //System.out.println(Math.abs(all / (Math.pow(2, bytesPerSmp * 8 - 1))));
        }

        return out;
    }
}
