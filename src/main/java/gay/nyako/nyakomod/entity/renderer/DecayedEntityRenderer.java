package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.NyakoClientMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ZombieEntityRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;

@Environment(value= EnvType.CLIENT)
public class DecayedEntityRenderer
        extends ZombieEntityRenderer {
    private static final Identifier TEXTURE = new Identifier("nyakomod", "textures/entity/zombie/decayed.png");

    public DecayedEntityRenderer(EntityRendererFactory.Context context) {
        super(context, NyakoClientMod.MODEL_DECAYED_LAYER, NyakoClientMod.MODEL_DECAYED_INNER_ARMOR_LAYER, NyakoClientMod.MODEL_DECAYED_OUTER_ARMOR_LAYER);
    }

    @Override
    protected void scale(ZombieEntity zombieEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(1.0625f, 1.0625f, 1.0625f);
        super.scale(zombieEntity, matrixStack, f);
    }

    @Override
    public Identifier getTexture(ZombieEntity zombieEntity) {
        return TEXTURE;
    }

    public static TexturedModelData getTexturedModelData() {
        return TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0.0f), 64, 64);
    }

    public static TexturedModelData getInnerArmorModelData() {
        return TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(0.5f), 0.0f), 64, 32);
    }

    public static TexturedModelData getOuterArmorModelData() {
        return TexturedModelData.of(BipedEntityModel.getModelData(new Dilation(1.0f), 0.0f), 64, 32);
    }
}