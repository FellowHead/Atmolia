package me.fellowhead.atmolia;

import me.fellowhead.atmolia.theory.Note;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Arpeggio {
    protected interface NoteChooser {
        Note choose(Note[] sorted, int count);
    }

    public static final Arpeggio NONE = new Arpeggio(new BeatTime(0)) {
        @Override
        public Note[] arp(Note[] notes) {
            return notes;
        }
    };
    protected static Note[] defaultArp(Note[] notes, BeatTime noteLength, NoteChooser user) {
        double lastEnd = 0;
        for (Note n : notes) {
            if (n.getEnd().beats > lastEnd) {
                lastEnd = n.getEnd().beats;
            }
        }

        ArrayList<Double> noteStarts = new ArrayList<>();
        for (Note n : notes) {
            double d = n.start.beats;
            if (!noteStarts.contains(d)) {
                noteStarts.add(d);
            }
        }

        noteStarts.sort((o1, o2) -> (int) (o1 - o2));

        noteStarts.add(lastEnd);

        ArrayList<Note> o = new ArrayList<>();

        for (int s = 0; s < noteStarts.size() - 1; s++) {
            double start = noteStarts.get(s);
            double end = noteStarts.get(s + 1);

            Note[] out = new Note[(int) ((end - start) / noteLength.beats)];
            for (int i = 0; i < out.length; i++) {
                BeatTime time = new BeatTime(start + i * noteLength.beats);
                ArrayList<Note> valid = new ArrayList<>();
                for (Note n : notes) {
                    if (n.getEnd().beats > time.beats && n.start.beats <= time.beats) {
                        valid.add(n);
                    }
                }
                valid.sort((o1, o2) -> (int) (o1.abs - o2.abs));

                out[i] = new Note(user.choose(valid.toArray(new Note[0]), i).abs, new BeatTime(start + i * noteLength.beats), new BeatTime(noteLength.beats));
            }
            o.addAll(Arrays.asList(out));
        }
        return o.toArray(new Note[0]);
    }

    public static Arpeggio ARP_UP_DOWN(BeatTime noteLength) {
        return new Arpeggio(noteLength) {
            @Override
            public Note[] arp(Note[] notes) {
                return defaultArp(notes, noteLength, (sorted, count) -> {
                    int c = count % (sorted.length * 2 - 2);
                    if (c >= sorted.length) {
                        int i = sorted.length - (c % sorted.length) - 2;
                        return sorted[i];
                    }
                    return sorted[c];
                });
            }
        };
    }
    public static Arpeggio ARP_UP(BeatTime noteLength) {
        return new Arpeggio(noteLength) {
            @Override
            public Note[] arp(Note[] notes) {
                return defaultArp(notes, noteLength, (sorted, count) -> sorted[count % sorted.length]);
            }
        };
    }

    protected BeatTime noteLength;

    public Arpeggio(BeatTime noteLength) {
        this.noteLength = noteLength;
    }

    public final Note[] arpeggiate(Note[] notes) {
        return arp(notes);
    }

    protected abstract Note[] arp(Note[] notes);
}
