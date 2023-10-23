package gay.nyako.nyakomod;

import I;
import Z;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class InstrumentRegister {
    Identifier[] ids = new Identifier[6];
    SoundEvent[] soundEvents = new SoundEvent[6];
    SoundEvent neutralSound;

    public InstrumentRegister(String name, SoundEvent neutralSound) {
        this.neutralSound = neutralSound;
        ids[0] = new Identifier("nyakomod:" + name + "_2");
        ids[1] = new Identifier("nyakomod:" + name + "_3");
        ids[2] = new Identifier("nyakomod:" + name + "_4");
        ids[3] = new Identifier("nyakomod:" + name + "_6");
        ids[4] = new Identifier("nyakomod:" + name + "_7");
        ids[5] = new Identifier("nyakomod:" + name + "_t8");

        for (var i = 0; i < ids.length; i++) {
            soundEvents[i] = new SoundEvent(ids[i]);
        }
    }

    public void register() {
        for (var i = 0; i < ids.length; i++) {
            Registry.register(Registry.SOUND_EVENT, ids[i], soundEvents[i]);
        }
    }

    public SoundEvent getFromIndex(int i) {
        var negative = i < 0;

        var index = Math.abs(i) / 12;

        if (index == 0) {
            return neutralSound;
        }

        if (negative) {
            index *= -1;

            return this.get(index + 3);
        } else {
            return this.get(index + 2);
        }
    }

    public SoundEvent get(int i) {
        if (i < 0 || i > soundEvents.length) {
            System.out.println("Invalid Sound Index: " + i);
            return null;
        }

        return soundEvents[i];
    }
}
