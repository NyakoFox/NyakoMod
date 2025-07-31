package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.FireArrowEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(value = EnvType.CLIENT)
public class FireArrowEntityRenderer extends ProjectileEntityRenderer<FireArrowEntity> {
    public static final Identifier TEXTURE = NyakoMod.id("textures/entity/projectiles/fire_arrow.png");

    public FireArrowEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(FireArrowEntity fireArrowEntity) {
        return TEXTURE;
    }
}
