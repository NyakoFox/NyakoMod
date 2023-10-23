package gay.nyako.nyakomod.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class PetSpriteRenderer extends EntityRenderer<PetSpriteEntity> {
    public static final Identifier TEXTURE = new Identifier("nyakomod", "textures/entity/dragon.png");
    public PetSpriteRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(PetSpriteEntity entity) {
        if (entity.customTextureId != null) {
            return entity.customTextureId;
        }
        return TEXTURE;
    }

    @Override
    public void render(PetSpriteEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f - entity.headYaw));
        float h = 0.0625f;
        matrices.scale(h, h, h);


        RenderSystem.setShaderTexture(0, getTexture(entity));

        renderSide(matrices, light, false, entity.getPetSize());
        renderSide(matrices, light, true, entity.getPetSize());

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    public void renderSide(MatrixStack matrices, int light, boolean reversed, float petSize) {
        matrices.push();
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();

        if (reversed) {
             matrices.multiply(RotationAxis.NEGATIVE_Y.rotationDegrees(180f));
        }

        int size = (int)(16 * petSize);

        int width = size;
        int height = size;

        var x0 = -width / 2;
        var x1 = width / 2;
        var y0 = 0;
        var y1 = height;
        var z = 0;

        var u0 = 1;
        var u1 = 0;
        var v0 = 1;
        var v1 = 0;
        if (reversed) {
            u0 = 0;
            u1 = 1;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x0, y1, z).texture(u0, v1).light(light).next();
        bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).light(light).next();
        bufferBuilder.vertex(matrix, x1, y0, z).texture(u1, v0).light(light).next();
        bufferBuilder.vertex(matrix, x0, y0, z).texture(u0, v0).light(light).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        matrices.pop();
    }
}
