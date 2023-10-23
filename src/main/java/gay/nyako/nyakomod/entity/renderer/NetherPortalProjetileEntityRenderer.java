package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.entity.NetherPortalProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;

@Environment(value=EnvType.CLIENT)
public class NetherPortalProjetileEntityRenderer
        extends EntityRenderer<NetherPortalProjectileEntity> {
    private final ItemRenderer itemRenderer;

    public NetherPortalProjetileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(NetherPortalProjectileEntity netherPortalProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(this.dispatcher.getRotation());
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
        if (true) {
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0f));
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        }
        this.itemRenderer.renderItem(netherPortalProjectileEntity.getStack(), ModelTransformationMode.GROUND, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, netherPortalProjectileEntity.getWorld(), netherPortalProjectileEntity.getId());
        matrixStack.pop();
        super.render(netherPortalProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(NetherPortalProjectileEntity netherPortalProjectileEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}


