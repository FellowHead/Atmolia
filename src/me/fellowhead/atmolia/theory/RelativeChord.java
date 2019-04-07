package me.fellowhead.atmolia.theory;

public class RelativeChord {
    private static final String[] roman = new String[] {
            "I", "II", "III", "IV", "V", "VI", "VII"
    };
    private int index;

    public boolean isMajor(Scale scale) {
        Integer[] arr = scale.getTonesRelative();
        int diff = arr[((index + 2) % arr.length)] - arr[index];
        if (diff < 0) {
            diff = 12 + diff;
        }
        return diff == 4;
    }

    public boolean isDiminished(Scale scale) {
        Integer[] arr = scale.getTonesRelative();
        int diff = arr[((index + 4) % arr.length)] - arr[index];
        if (diff < 0) {
            diff = 12 + diff;
        }
        return diff == 6;
    }

    private boolean isSeventhMaj(Scale scale) {
        Integer[] arr = scale.getTonesRelative();
        int diff = arr[((index + 6) % arr.length)] - arr[index];
        if (diff < 0) {
            diff = 12 + diff;
        }
        return diff == 11;
    }

    public Chord toAbsolute(Tone tonic, Scale scale) {
        Scale played = new Scale(false,false, !isMajor(scale), isMajor(scale),false, isDiminished(scale), !isDiminished(scale),false,false, !isSeventhMaj(scale), isSeventhMaj(scale));
        return new Chord(scale.getTones(tonic)[index], played);
    }

    public RelativeChord(int number) {
        this.index = number - 1;
    }

    public String getRelString() {
        return roman[index] + "?";
    }

    public String getRelString(Scale scale) {
        String s = roman[index];
        if (isMajor(scale)) {
            return s;
        } else if (isDiminished(scale)) {
            return s + "°";
        }
        return s + "m";
    }

    @Override
    public String toString() {
        return getRelString();
    }
}
