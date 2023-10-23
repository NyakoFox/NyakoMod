package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.VillagerEntityAccess;
import net.minecraft.client.render.entity.VillagerEntityRenderer;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(VillagerEntityRenderer.class)
public class VillagerEntityRendererMixin {
    private static final Identifier TEXTURE = new Identifier("textures/entity/villager/villager.png");
    private static final Identifier TEXTURE_PEELED = new Identifier("nyakomod", "textures/entity/villager/villager_peeled.png");

    /**
     * @author NyakoFox
     * @reason Peeled Villagers hehe :)
     */
    @Overwrite()
    public Identifier getTexture(VillagerEntity villagerEntity) {
        return ((VillagerEntityAccess) villagerEntity).isPeeled() ? TEXTURE_PEELED : TEXTURE;
    }
}
