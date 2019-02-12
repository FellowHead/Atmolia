package me.fellowhead.atmolia;

import java.util.ArrayList;

public class Chord {
    private Tone main;
    public boolean dur;

    public Tone getMain() {
        return main;
    }

    public String getName() {
        return main.getName() + "" + (dur ? "" : "m");
    }

    public Tone[] getTones() {
        if (dur) {
            return new Tone[] {
                    new Tone(main), new Tone(main.getAbs() + 4), new Tone(main.getAbs() + 7)
            };
        } else {
            return new Tone[] {
                    new Tone(main), new Tone(main.getAbs() + 3), new Tone(main.getAbs() + 7)
            };
        }
    }

    public Chord[] getBrothers() {
        if (dur) {
            return new Chord[] {
                    new Chord(new Tone(main.getAbs() + 4), false),
                    new Chord(new Tone(main.getAbs() + 9), false)
            };
        }
        return new Chord[] {
                new Chord(new Tone(main.getAbs() + 3), true),
                new Chord(new Tone(main.getAbs() + 8), true)
        };
    }

    public Chord[] getCousins() {
        if (dur) {
            return new Chord[] {
                    new Chord(new Tone(main.getAbs() + 5), true),
                    new Chord(new Tone(main.getAbs() + 5), false),
                    new Chord(new Tone(main.getAbs() + 8), true),

                    new Chord(new Tone(main.getAbs() + 1), false),

                    new Chord(new Tone(main.getAbs() + 7), true),
                    new Chord(new Tone(main.getAbs() + 7), false),
                    new Chord(new Tone(main.getAbs() + 3), true),
            };
        }
        return new Chord[] {
                new Chord(new Tone(main.getAbs() + 5), true),
                new Chord(new Tone(main.getAbs() + 5), false),
                new Chord(new Tone(main.getAbs() + 8), true),

                new Chord(new Tone(main.getAbs() + 3), false),

                new Chord(new Tone(main.getAbs() + 7), true),
                new Chord(new Tone(main.getAbs() + 7), false),
                new Chord(new Tone(main.getAbs() + 3), true),
        };
    }

    public Chord(Tone main, boolean dur) {
        this.main = main;
        this.dur = dur;
    }
}
