package gay.nyako.nyakomod.entity.renderer;

import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.model.PetDragonModel;
import gay.nyako.nyakomod.item.PetChangeSummonItem;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

/*
 * A renderer is used to provide an entity model, shadow size, and texture.
 */
public class PetDragonRenderer extends MobEntityRenderer<PetDragonEntity, PetDragonModel> {

    public PetDragonRenderer(EntityRendererFactory.Context context) {
        super(context, new PetDragonModel(context.getPart(NyakoClientMod.MODEL_DRAGON_LAYER)), 0.3f);
    }

    @Override
    public Identifier getTexture(PetDragonEntity entity) {
        var stack = entity.getSummonItem();
        if (stack != null) {
            var item = stack.getItem();
            if (item instanceof PetChangeSummonItem<?> petItem) {
                return petItem.getVariation(stack).texture;
            }
        }

        return new Identifier("nyakomod", "textures/entity/pet/dragon.png");
    }
}
