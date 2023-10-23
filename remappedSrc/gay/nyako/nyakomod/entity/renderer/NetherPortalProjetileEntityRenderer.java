package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.entity.NetherPortalProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;

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
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
        if (true) {
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0f));
            matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0f));
            matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90.0f));
        }
        this.itemRenderer.renderItem(netherPortalProjectileEntity.getStack(), ModelTransformation.Mode.GROUND, i, OverlayTexture.DEFAULT_UV, matrixStack, vertexConsumerProvider, netherPortalProjectileEntity.getId());
        matrixStack.pop();
        super.render(netherPortalProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(NetherPortalProjectileEntity netherPortalProjectileEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }
}


