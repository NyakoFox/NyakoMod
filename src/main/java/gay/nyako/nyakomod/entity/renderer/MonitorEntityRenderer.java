package gay.nyako.nyakomod.entity.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.entity.MonitorEntity;
import gay.nyako.nyakomod.utils.NyakoUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;

public class MonitorEntityRenderer extends EntityRenderer<MonitorEntity> {
    private final ModelPart base;

    private static final Identifier TEXTURE = new Identifier("nyakomod", "textures/entity/monitor/monitor.png");
    private static final Identifier TEXTURE_ON = new Identifier("nyakomod", "textures/entity/monitor/monitor_on.png");

    public MonitorEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx);
        ModelPart root = ctx.getPart(NyakoClientMod.MODEL_MONITOR_LAYER);
        base = root.getChild(EntityModelPartNames.CUBE);
    }

    @Override
    public Identifier getTexture(MonitorEntity entity) {
        if (entity.getIdentifier() != null) {
            return TEXTURE_ON;
        } else {
            return TEXTURE;
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild(EntityModelPartNames.CUBE, ModelPartBuilder.create().uv(0, 0).cuboid(-8.0F, -16.0F, -8.0F, 16.0F, 16.0F, 1.0F), ModelTransform.pivot(0F, 24F, 0F));
        return TexturedModelData.of(modelData, 64, 32);
    }

    @Override
    public void render(MonitorEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        Identifier identifier = entity.getIdentifier();

        matrices.push();
        float pitch = entity.getPitch();
        float entityYaw = entity.getYaw();
        matrices.multiply(new Quaternionf().rotateXYZ(pitch, MathHelper.wrapDegrees(180-entityYaw), 180));

        matrices.translate(0f, -1f, 0.5f - (1d/16d)/2);
        VertexConsumer vertices = vertexConsumers.getBuffer(getLayer(entity));
        matrices.push();
        matrices.translate(0f, -(entity.getMonitorHeight() - 1), 0f);
        matrices.scale(entity.getMonitorWidth(), entity.getMonitorHeight(), 1f);

        base.render(matrices, vertices, 255, OverlayTexture.DEFAULT_UV);
        matrices.pop();
        if (identifier != null) {
            var texture = MinecraftClient.getInstance().getTextureManager().getTexture(identifier);
            if (texture instanceof NativeImageBackedTexture nativeImageBackedTexture) {
                var image = nativeImageBackedTexture.getImage();
                if (image != null) {
                    var width = (float) image.getWidth();
                    var height = (float) image.getHeight();
                    matrices.translate(0f, 1f, -0.5f + (1d / 16d) / 2);

                    var distanceFromModel = 1d;
                    matrices.translate(-0.5f, -0.5, -distanceFromModel / 16d);

                    matrices.translate((float) (-entity.getMonitorWidth()) / 2f, (float) (-entity.getMonitorHeight()) / 2f, 0f);

                    matrices.translate(0.5f, 0.5f, 0f);

                    RenderSystem.setShaderTexture(0, identifier);
                    // keep aspect ratio while keeping coordinates between 0-1
                    var x = 0f;
                    var y = 0f;
                    var outputWidth = 1f;
                    var outputHeight = 1f;
                    if (width > height) {
                        outputHeight = height / width;
                        y = (1f - outputHeight) / 2f;
                    } else {
                        outputWidth = width / height;
                        x = (1f - outputWidth) / 2f;
                    }

                    outputWidth *= entity.getMonitorWidth();
                    outputHeight *= entity.getMonitorHeight();
                    x *= entity.getMonitorWidth();
                    y *= entity.getMonitorHeight();

                    drawTexture(matrices, x, y, 0f, 0f, 0f, outputWidth, outputHeight, outputWidth, outputHeight);
                }
            }
        }
        matrices.pop();
    }

    private RenderLayer getLayer(MonitorEntity entity)
    {
        return RenderLayer.getArmorCutoutNoCull(getTexture(entity));
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float z, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    private static void drawTexture(MatrixStack matrices, float x0, float x1, float y0, float y1, float z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0f) / textureWidth, (u + regionWidth) / textureWidth, (v + 0.0f) / textureHeight, (v + regionHeight) / textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrices, x0, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrices, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrices, x1, y0, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrices, x0, y0, z).texture(u0, v0).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}
