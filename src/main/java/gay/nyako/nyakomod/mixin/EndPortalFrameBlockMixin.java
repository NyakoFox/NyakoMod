package gay.nyako.nyakomod.mixin;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.EndPortalFrameBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(EndPortalFrameBlock.class)
public class EndPortalFrameBlockMixin {
    @ModifyArg(method = "<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;<init>(Lnet/minecraft/block/AbstractBlock$Settings;)V"), index = 0)
    private static AbstractBlock.Settings injected(AbstractBlock.Settings settings)
    {
        return settings.requiresTool().strength(1.5f, 6.0f);
    }

}
