package gay.nyako.nyakomod;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class InstrumentRegister {
    Identifier[] ids = new Identifier[4];
    SoundEvent[] soundEvents = new SoundEvent[4];

    public InstrumentRegister(String name) {
        ids[0] = new Identifier("nyakomod:" + name + "_3");
        ids[1] = new Identifier("nyakomod:" + name + "_4");
        ids[2] = new Identifier("nyakomod:" + name + "_6");
        ids[3] = new Identifier("nyakomod:" + name + "_7");

        for (var i = 0; i < ids.length; i++) {
            soundEvents[i] = new SoundEvent(ids[i]);
        }
    }

    public void register() {
        for (var i = 0; i < ids.length; i++) {
            Registry.register(Registry.SOUND_EVENT, ids[i], soundEvents[i]);
        }
    }

    public SoundEvent get(int i) {
        return soundEvents[i];
    }
}
