package gay.nyako.nyakomod.mixin;

import com.google.common.collect.ImmutableMap;
import gay.nyako.nyakomod.NyakoBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin {

    @Shadow
    @Final
    @Mutable
    protected static Map<Block, Block> STRIPPED_BLOCKS;

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onClinitAddLog(CallbackInfo cbi) {
        Map<Block, Block> strippedBlocks = new HashMap<>(STRIPPED_BLOCKS);
        strippedBlocks.put(NyakoBlocks.ECHO_SPINE, NyakoBlocks.STRIPPED_ECHO_SPINE);
        strippedBlocks.put(NyakoBlocks.ECHO_SPUR, NyakoBlocks.STRIPPED_ECHO_SPUR);

        STRIPPED_BLOCKS = ImmutableMap.copyOf(strippedBlocks);
    }
}