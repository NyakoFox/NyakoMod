package gay.nyako.nyakomod.entity;

import com.google.common.collect.ImmutableList;
import gay.nyako.nyakomod.NyakoClientMod;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;

public class MonitorEntityModel extends EntityModel<MonitorEntity> {
    private final ModelPart base;

    public MonitorEntityModel(EntityRendererFactory.Context renderContext) {
        //this.base = modelPart.getChild(EntityModelPartNames.CUBE);
        ModelPart root = renderContext.getPart(NyakoClientMod.MODEL_MONITOR_LAYER);
        base = root.getChild(EntityModelPartNames.CUBE);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 1.0F, 16.0F, 16.0F), ModelTransform.pivot(0F, 0F, 0F));
        // modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-1F, 16F, -8F, 1F, 16F, 16F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void setAngles(MonitorEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {

        //ImmutableList.of(this.base).forEach((modelRenderer) -> {
        //    modelRenderer.render(matrices, vertices, light, overlay, red, green, blue, alpha);
        //});
    }
}
