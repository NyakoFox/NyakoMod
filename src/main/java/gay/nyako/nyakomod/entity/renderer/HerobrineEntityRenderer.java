package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.entity.HerobrineEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.ZombieEntityModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;

public class HerobrineEntityRenderer<T extends HerobrineEntity, M extends BipedEntityModel<T>>
        extends BipedEntityRenderer<T, M> {
    private static final Identifier TEXTURE = new Identifier("nyakomod", "textures/entity/herobrine/herobrine.png");

    public HerobrineEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer bodyModel, EntityModelLayer legsArmorModel, EntityModelLayer bodyArmorModel) {
        super(ctx, (M) new BipedEntityModel<HerobrineEntity>(ctx.getPart(bodyModel)), 0.5f);
        this.addFeature(new ArmorFeatureRenderer(this, new BipedEntityModel<HerobrineEntity>(ctx.getPart(legsArmorModel)), new BipedEntityModel<HerobrineEntity>(ctx.getPart(bodyArmorModel)), ctx.getModelManager()));
    }

    public HerobrineEntityRenderer(EntityRendererFactory.Context context) {
        this(context, NyakoClientMod.MODEL_HEROBRINE_LAYER, NyakoClientMod.MODEL_HEROBRINE_INNER_ARMOR_LAYER, NyakoClientMod.MODEL_HEROBRINE_OUTER_ARMOR_LAYER);
    }

    @Override
    public Identifier getTexture(HerobrineEntity entity) {
        return TEXTURE;
    }

    @Override
    protected boolean isShaking(T entity) {
        return entity.age < 60;
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


