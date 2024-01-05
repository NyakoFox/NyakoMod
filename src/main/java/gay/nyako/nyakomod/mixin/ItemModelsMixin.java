package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(ItemModels.class)
public abstract class ItemModelsMixin {
    @Final
    @Shadow
    public Int2ObjectMap<ModelIdentifier> modelIds;

    @Final
    @Shadow
    private Int2ObjectMap<BakedModel> models;

    @Shadow
    public abstract BakedModelManager getModelManager();

    @Inject(at = @At("HEAD"), method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", cancellable=true)
    public void getModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
        var nbt = stack.getNbt();
        if (stack.isOf(NyakoItems.DEV_NULL)) {
            if (nbt != null && nbt.contains("stored_item")) {
                var storedNbt = nbt.getCompound("stored_item");
                var stored = ItemStack.fromNbt(storedNbt);
                if (!storedNbt.contains("modelId")) {
                    var id = Item.getRawId(stored.getItem());
                    cir.setReturnValue(models.get(id));
                    return;
                } else {
                    nbt = storedNbt;
                }
            }
        }

        if (nbt != null) {
            if (nbt.contains("modelId")) {
                var modelId = nbt.getString("modelId");
                var identifier = new ModelIdentifier(new Identifier(modelId), "inventory");
                cir.setReturnValue(getBakedModel(identifier));
            }
        }
    }

    BakedModel getBakedModel(ModelIdentifier identifier) {
        var model = getModelManager().getModel(identifier);
        if (model != getModelManager().getMissingModel()) {
            return model;
        }

        for (Map.Entry entry : this.modelIds.entrySet()) {
            if (((ModelIdentifier)entry.getValue()).compareTo(identifier) == 0) {
                return this.models.get((Integer)entry.getKey());
            }
        }

        return getModelManager().getMissingModel();
    }
}
