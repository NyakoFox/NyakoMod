package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.ObsidianArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class ObsidianArrowEntityRenderer extends ProjectileEntityRenderer<ObsidianArrowEntity> {
    public static final Identifier TEXTURE = NyakoMod.id("textures/entity/projectiles/obsidian_arrow.png");

    public ObsidianArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(ObsidianArrowEntity obsidianArrowEntity) {
        return TEXTURE;
    }
}

