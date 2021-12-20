package gay.nyako.nyakomod;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class CustomDiscItem extends MusicDiscItem {
    public CustomDiscItem(int comparatorOutput, SoundEvent sound, Settings settings) {
        super(comparatorOutput, sound, settings);
    }
}