package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.entity.PetSpriteEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

public class PetSpriteRenderer extends EntityRenderer<PetSpriteEntity> {
    public static final Identifier TEXTURE = new Identifier("nyakomod", "textures/entity/dragon.png");
    public PetSpriteRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PetSpriteEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(PetSpriteEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f - entity.headYaw));
        float h = 0.0625f;
        matrices.scale(h, h, h);
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(TEXTURE));

        renderSide(matrices, vertexConsumer, light, false);
        renderSide(matrices, vertexConsumer, light, true);

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public void renderSide(MatrixStack matrices, VertexConsumer consumer, int light, boolean reversed) {
        matrices.push();
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        if (reversed) {
            matrices.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180f));
        }

        if (reversed) {
            this.vertex(matrix4f, matrix3f, consumer, -16, -16, 0, 0, 1, light);
            this.vertex(matrix4f, matrix3f, consumer, -16, 16, 0, 0, 0, light);
            this.vertex(matrix4f, matrix3f, consumer, 16, 16, 0, 1, 0, light);
            this.vertex(matrix4f, matrix3f, consumer, 16, -16, 0, 1, 1, light);
        } else {
            this.vertex(matrix4f, matrix3f, consumer, -16, -16, 0, 1, 1, light);
            this.vertex(matrix4f, matrix3f, consumer, -16, 16, 0, 1, 0, light);
            this.vertex(matrix4f, matrix3f, consumer, 16, 16, 0, 0, 0, light);
            this.vertex(matrix4f, matrix3f, consumer, 16, -16, 0, 0, 1, light);
        }
        matrices.pop();
    }

    public void vertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int light) {
        vertexConsumer.vertex(positionMatrix, x, y, z).color(255, 255, 255, 0).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0f, 1f, 0f).next();
    }
}
