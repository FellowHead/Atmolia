package me.fellowhead.atmolia.instruments;

import me.fellowhead.atmolia.AdvancedTime;
import me.fellowhead.atmolia.Note;
import me.fellowhead.atmolia.Tone;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ZoneSmp extends Instrument {
    public static class Zone {
        public interface ZoneListener {
            void ready(Zone zone);
        }

        private float[] cleans;
        private int root;
        private int toneL;
        private int toneH;
        private int loopStart;
        private int loopLength;
        private float sustain = 3; //in seconds
        private float release = 0.5f; //in seconds
        private float sampleRate;

        public int getSamplePos(int x) {
            if (loopLength <= 0) {
                return Math.min(x, cleans.length - 1);
            }
            //int x = (int)(seconds * sampleRate);
            final int loopEnd = loopStart + loopLength;

            if (x < loopEnd) {
                return x;
            }

            int direction = ((x - loopStart) / loopLength) % 2;
            int out = (x - loopStart) % loopLength;

            return (direction < 1) ? loopStart + out : loopEnd - out;
        }

        public float[] getCleans() {
            return cleans;
        }

        public int getRoot() {
            return root;
        }

        public static void loadFromNamedFile(File wavFile, int rangeDown, int rangeUp, ZoneListener zoneListener) {
            int root;
            String s = wavFile.getName().replace('_', ' ').replace('-', ' ');
            s = s.substring(s.indexOf(" ") + 1, s.length() - 4);
            String[] toneNames = Tone.toneNames;

            for (int i = 0; i < toneNames.length; i++) {
                String toneName = toneNames[i];
                if (s.substring(0, s.length() - 1).equalsIgnoreCase(toneName)) {
                    System.out.println("Creating zone with root " + s);
                    s = s.replace(toneName, "");
                    root = i + Integer.parseInt(s) * 12;
                    Zone res = new Zone(wavFile, root, root - rangeDown, root + rangeUp);
                    res.loopLength = 0;
                    zoneListener.ready(res);
                    return;
                }
            }
        }

        public Zone(File wavFile, int root, int low, int high) {
            this.root = root;
            this.toneL = low;
            this.toneH = high;
            this.loopStart = 1000;
            this.loopLength = 5000;
            float best = 0;
            try {
                AudioInputStream stream = AudioSystem.getAudioInputStream(wavFile);
                stream.getFormat();

                sampleRate = stream.getFormat().getSampleRate();

                cleans = new float[(int)stream.getFrameLength()];

                final double streamSize = Math.pow(2, stream.getFormat().getSampleSizeInBits());

                for (int n = 0; n < cleans.length; n++) {
                    byte[] data = new byte[stream.getFormat().getFrameSize()];
                    int count = stream.read(data);
                    long frameValue = 0;
                    for (int i = 0; i < count; i++) {
                        frameValue += data[i] * Math.pow(2, 8 *
                                (stream.getFormat().isBigEndian() ? (count - i - 1) : (i)));
                    }
                    cleans[n] = (float) (frameValue / streamSize);
                    if (Math.abs(cleans[n]) > best) {
                        best = Math.abs(cleans[n]);
                    }
                }

                for (int i = 0; i < cleans.length; i++) {
                    cleans[i] = (cleans[i] / best);
                }
                loopLength = cleans.length - loopStart - 1;

            } catch (UnsupportedAudioFileException | IOException e) {
                e.printStackTrace();
                System.out.println("File: " + wavFile.getPath() + " | " + wavFile.exists());
            }

            /*for (int i = 0; i < 25000; i++) {
                System.out.println(i + " | " + getSamplePos(i));
            }*/
        }
    }

    private ArrayList<Zone> zones;
    private float globalRelease;

    public void setZones(Zone[] zones) {
        this.zones = new ArrayList<>();
        globalRelease = 0;
        for (Zone zone : zones) {
            this.zones.add(zone);
            if (zone.release > globalRelease) {
                globalRelease = zone.release;
            }
        }
    }

    public void fillArrangement() {
        for (Zone zone : zones) {
            Zone below = null;
            for (Zone check : zones) {
                if (check.root < zone.root) {
                    if (below == null || below.root < check.root) {
                        below = check;
                    }
                }
            }
            if (below == null) {
                zone.toneL = 0;
            } else {
                zone.toneL = below.root + 1;
            }
        }
        System.out.println("arranged");
    }

    public void createZonesFromDirectory(File dir) {
        zones = new ArrayList<>();
        final int[] inProgress = {0};
        for (File file : dir.listFiles((f, name) -> name.endsWith(".wav"))) {
            inProgress[0]++;
            new Thread(() -> Zone.loadFromNamedFile(file, 0, 0, zone -> {
                System.out.println("Adding zone " + zone.root);
                zones.add(zone);
                inProgress[0]--;
                if (inProgress[0] == 0) {
                    System.out.println("so guys we did it");
                    fillArrangement();
                }
            })).start();
        }
    }

    public ZoneSmp(File directory) {
        createZonesFromDirectory(directory);
    }

    public ZoneSmp(Zone[] zones) {
        setZones(zones);
    }

    @Override
    public double evaluate(Note n, AdvancedTime time) {
        float noteEndSec = n.getEnd().advanced(time).seconds();
        if (time.beats >= n.start.beats && time.seconds() < noteEndSec + globalRelease) {
            for (Zone f : zones) {
                if (f.toneL <= n.abs && f.toneH >= n.abs && time.seconds() < noteEndSec + f.release) {
                    int c = (int)Math.floor(((time.beats - n.start.beats) * f.sampleRate) * Math.pow(Note.semitone, n.abs - f.root));

                    double res = (1 - (time.seconds() - n.start.advanced(time).seconds()) / f.sustain) * f.cleans[f.getSamplePos(c)];
                    if (time.seconds() > noteEndSec) {
                        res *= (1 - (time.seconds() - noteEndSec) / f.release);
                    }
                    return res;
                }
            }
        }
        return 0;
    }
}
