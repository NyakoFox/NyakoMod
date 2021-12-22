package gay.nyako.nyakomod.entity;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import org.lwjgl.opengl.GL11;

public class MonitorEntityRenderer extends EntityRenderer<MonitorEntity> {
    private final ModelPart base;

    public MonitorEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        ModelPart root = ctx.getPart(NyakoClientMod.MODEL_MONITOR_LAYER);
        base = root.getChild(EntityModelPartNames.CUBE);
    }

    public boolean hasTexture = false;
    public Identifier identifier = new Identifier("nyakomod", "monitor_image");

    @Override
    public Identifier getTexture(MonitorEntity entity) {
        return new Identifier("nyakomod", "textures/entity/monitor/monitor.png");
    }


    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 1.0F), ModelTransform.pivot(0F, 24F, 0F));
        // modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 1.0F, 16.0F, 16.0F), ModelTransform.pivot(0F, 0F, 0F));
        // modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-1F, 16F, -8F, 1F, 16F, 16F), ModelTransform.pivot(0F, 0F, 0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(MonitorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (!hasTexture) {
            NyakoMod.downloadSprite("https://files.catbox.moe/lumuta.png", identifier);
            hasTexture = true;
        }

        matrices.push();
        float pitch = entity.getPitch();
        float entityYaw = entity.getYaw();
        matrices.multiply(new Quaternion(pitch, MathHelper.wrapDegrees(180-entityYaw), 0, true));

        matrices.translate(0f, -1f, 0.5f - (1d/16d)/2);
        VertexConsumer vertices = vertexConsumers.getBuffer(getLayer(entity));
        base.render(matrices, vertices, light, OverlayTexture.DEFAULT_UV);
        matrices.translate(0f, 1f,-0.5f + (1d/16d)/2);
        matrices.translate(-0.5f, -0.5, -0.55d/16d);
        RenderSystem.setShaderTexture(0, identifier);
        this.drawTexture(matrices, 0, 0, 0, 0f, 0f, 1, 1, 1, 1);
        matrices.pop();
    }

    private RenderLayer getLayer(MonitorEntity entity)
    {
        return RenderLayer.getArmorCutoutNoCull(getTexture(entity));
    }

    /**
     * Draws a textured rectangle from a region in a texture.
     *
     * <p>The width and height of the region are the same as
     * the dimensions of the rectangle.
     *
     * @param u the left-most coordinate of the texture region
     * @param z the Z coordinate of the rectangle
     * @param y the Y coordinate of the rectangle
     * @param x the X coordinate of the rectangle
     * @param matrices the matrix stack used for rendering
     * @param textureHeight the height of the entire texture
     * @param textureWidth the width of the entire texture
     * @param height the height of the rectangle
     * @param width the width of the rectangle
     * @param v the top-most coordinate of the texture region
     */
    public static void drawTexture(MatrixStack matrices, int x, int y, int z, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    private static void drawTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0f) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0f) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x0, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, x1, y0, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrix, x0, y0, z).texture(u0, v0).next();
        bufferBuilder.end();
        BufferRenderer.draw(bufferBuilder);
    }
}