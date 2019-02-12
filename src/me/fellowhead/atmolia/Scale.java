package me.fellowhead.atmolia;

import java.util.ArrayList;

public class Scale {
    public static final Scale DUR = new Scale(false,true,false,true,true,false,true,false,true,false,true);

    private boolean[] playable;

    public Scale(boolean b2, boolean b3, boolean b4, boolean b5, boolean b6, boolean b7, boolean b8, boolean b9, boolean b10, boolean b11, boolean b12) {
        playable = new boolean[] {
                true, b2,b3,b4,b5,b6,b7,b8,b9,b10,b11,b12
        };
    }

    public Tone[] getTones(Tone base) {
        ArrayList<Tone> res = new ArrayList<>();
        for (int i = 0; i < playable.length; i++) {
            if (playable[i]) {
                res.add(new Tone(base.getAbs() + i));
            }
        }
        return res.toArray(new Tone[0]);
    }
}
