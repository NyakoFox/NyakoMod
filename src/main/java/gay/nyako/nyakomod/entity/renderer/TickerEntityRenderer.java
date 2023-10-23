package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.entity.TickerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class TickerEntityRenderer extends EntityRenderer<TickerEntity> {

    private static final Identifier RING_TEXTURE = Identifier.of("nyakomod", "textures/time_ring.png");

    public TickerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }
    @Override
    public Identifier getTexture(TickerEntity entity) {
        return RING_TEXTURE;
    }

    @Override
    public void render(TickerEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));
        matrices.push();
        matrices.translate(0, 0.5, 0);

        Vector4f vec1, vec2, vec3, vec4;
        for (Direction dir : Direction.values()) {
            matrices.push();
            Vec3i dirVector = dir.getVector();
            float angle = entity.age+tickDelta;
            matrices.multiply(new Quaternionf().fromAxisAngleDeg(new Vector3f(dirVector.getX(), dirVector.getY(), dirVector.getZ()), angle));
            MatrixStack.Entry entry = matrices.peek();

            float offset = 0.5001f * (dir.getDirection() == Direction.AxisDirection.NEGATIVE ? -1 : 1); // 0.5001 is to prevent Z-fighting
            if (dir.getAxis() == Direction.Axis.X) {
                vec1 = new Vector4f(offset, -0.5f, -0.5f, 1.0f);
                vec2 = new Vector4f(offset,  0.5f, -0.5f, 1.0f);
                vec3 = new Vector4f(offset,  0.5f,  0.5f, 1.0f);
                vec4 = new Vector4f(offset, -0.5f,  0.5f, 1.0f);
            } else if (dir.getAxis() == Direction.Axis.Y) {
                vec1 = new Vector4f(-0.5f, offset, -0.5f, 1.0f);
                vec2 = new Vector4f(-0.5f, offset,  0.5f, 1.0f);
                vec3 = new Vector4f( 0.5f, offset,  0.5f, 1.0f);
                vec4 = new Vector4f( 0.5f, offset, -0.5f, 1.0f);
            } else {
                vec1 = new Vector4f(-0.5f, -0.5f, offset, 1.0f);
                vec2 = new Vector4f(-0.5f,  0.5f, offset, 1.0f);
                vec3 = new Vector4f( 0.5f,  0.5f, offset, 1.0f);
                vec4 = new Vector4f( 0.5f, -0.5f, offset, 1.0f);
            }

            vec1.mul(entry.getPositionMatrix());
            vec2.mul(entry.getPositionMatrix());
            vec3.mul(entry.getPositionMatrix());
            vec4.mul(entry.getPositionMatrix());

            int frame = Math.min(entity.getSpeed(), 5);

            float minU = frame / 6f;
            float maxU = (frame + 1) / 6f;

            consumer.vertex(vec1.x(), vec1.y(), vec1.z()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(minU, 1.0f)
                    .overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(dirVector.getX(), dirVector.getY(), dirVector.getZ()).next();
            consumer.vertex(vec2.x(), vec2.y(), vec2.z()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(minU, 0.0f)
                    .overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(dirVector.getX(), dirVector.getY(), dirVector.getZ()).next();
            consumer.vertex(vec3.x(), vec3.y(), vec3.z()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(maxU, 0.0f)
                    .overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(dirVector.getX(), dirVector.getY(), dirVector.getZ()).next();
            consumer.vertex(vec4.x(), vec4.y(), vec4.z()).color(1.0f, 1.0f, 1.0f, 1.0f).texture(maxU, 1.0f)
                    .overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(dirVector.getX(), dirVector.getY(), dirVector.getZ()).next();
            matrices.pop();
        }
        matrices.pop();
    }
}
