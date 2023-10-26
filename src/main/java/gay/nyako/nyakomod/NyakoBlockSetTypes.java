package gay.nyako.nyakomod;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.fabricmc.fabric.api.object.builder.v1.block.type.BlockSetTypeBuilder;
import net.minecraft.block.BlockSetType;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import java.util.Set;
import java.util.stream.Stream;

public class NyakoBlockSetTypes {
    public static final BlockSetType ECHO = register("echo", BlockSetTypeBuilder.copyOf(BlockSetType.WARPED));

    public static BlockSetType register(String id, BlockSetTypeBuilder blockSetTypeBuilder)
    {
        return blockSetTypeBuilder.register(new Identifier("nyakomod", id));
    }
}

