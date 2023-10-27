package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.object.builder.v1.block.type.WoodTypeBuilder;
import net.minecraft.block.BlockSetType;
import net.minecraft.block.WoodType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class NyakoWoodTypes {
    public static final WoodType ECHO = register("echo", WoodTypeBuilder.copyOf(WoodType.WARPED), NyakoBlockSetTypes.ECHO);

    public static WoodType register(String id, WoodTypeBuilder woodTypeBuilder, BlockSetType blockSetType)
    {
        return woodTypeBuilder.register(new Identifier("nyakomod", id), blockSetType);
    }
}
