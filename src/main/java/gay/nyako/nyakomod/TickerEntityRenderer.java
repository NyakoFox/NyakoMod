package gay.nyako.nyakomod;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3f;

public class TickerEntityRenderer extends EntityRenderer<TickerEntity> {
    private final ItemRenderer itemRenderer;
    public TickerEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }
    @Override
    public Identifier getTexture(TickerEntity entity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
    @Override
    public void render(TickerEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        ItemStack tiabStack = new ItemStack(NyakoMod.TIME_IN_A_BOTTLE);
        for (Direction direction : Direction.values()) {
            if (direction == direction.UP || direction == direction.DOWN) continue;
            matrices.push();

            // center in block
            matrices.translate(direction.getOffsetX()/1.95f, (direction.getOffsetY()/1.95f + 0.5), direction.getOffsetZ()/1.95f);

            // align to block sides
            matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(direction.asRotation()));

            //MinecraftClient.getInstance().getItemRenderer().renderItem(tiabStack, ModelTransformation.Mode.GROUND, 242, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            this.itemRenderer.renderItem(tiabStack, ModelTransformation.Mode.GROUND, 242, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers,entity.getId());
            //this.itemRenderer.renderItem(itemStack, ModelTransformation.Mode.FIXED, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, itemFrameEntity.getId());
            matrices.pop();
        }
    }
}
