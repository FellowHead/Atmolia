package me.fellowhead.atmolia.theory;

public class Chord {
    private Tone base;
    private Scale played;

    public Tone getBase() {
        return base;
    }

    public String getName() {
        String s = base.getName();
        if (played.getPlayable()[3]) {
            if (played.getPlayable()[6]) {
                return s + "°";
            }
            return s + "m";
        } else if (played.getPlayable()[4]) {
            if (played.getPlayable()[8]) {
                return s + "+";
            }
            return s + "";
        }
        return base.getName() + "?";
    }

    public Tone[] getPlayed() {
        return played.getTones(base);
    }

//    public Chord[] getBrothers() {
//        if (dur) {
//            return new Chord[] {
//                    new Chord(new Tone(base.getAbs() + 4), false),
//                    new Chord(new Tone(base.getAbs() + 9), false)
//            };
//        }
//        return new Chord[] {
//                new Chord(new Tone(base.getAbs() + 3), true),
//                new Chord(new Tone(base.getAbs() + 8), true)
//        };
//    }
//
//    public Chord[] getCousins() {
//        if (dur) {
//            return new Chord[] {
//                    new Chord(new Tone(base.getAbs() + 5), true),
//                    new Chord(new Tone(base.getAbs() + 5), false),
//                    new Chord(new Tone(base.getAbs() + 8), true),
//
//                    new Chord(new Tone(base.getAbs() + 1), false),
//
//                    new Chord(new Tone(base.getAbs() + 7), true),
//                    new Chord(new Tone(base.getAbs() + 7), false),
//                    new Chord(new Tone(base.getAbs() + 3), true),
//            };
//        }
//        return new Chord[] {
//                new Chord(new Tone(base.getAbs() + 5), true),
//                new Chord(new Tone(base.getAbs() + 5), false),
//                new Chord(new Tone(base.getAbs() + 8), true),
//
//                new Chord(new Tone(base.getAbs() + 3), false),
//
//                new Chord(new Tone(base.getAbs() + 7), true),
//                new Chord(new Tone(base.getAbs() + 7), false),
//                new Chord(new Tone(base.getAbs() + 3), true),
//        };
//    }

    public static Chord build(Tone base, boolean major, boolean adjFifth) {
        if (!major) {
            return new Chord(base, new Scale(false,false,true,false,false,adjFifth,!adjFifth,false,false,false,false));
        }
        return new Chord(base, new Scale(false,false,false,true,false,false,!adjFifth,adjFifth,false,false,false));
    }

    public Chord(Tone base, Scale played) {
        this.base = base;
        this.played = played;
    }
}
