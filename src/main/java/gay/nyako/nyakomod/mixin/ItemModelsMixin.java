package gay.nyako.nyakomod.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.item.ItemModels;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.item.ItemStack;

@Mixin(ItemModels.class)
public abstract class ItemModelsMixin {
  @Shadow
  public Int2ObjectMap<ModelIdentifier> modelIds;

  @Shadow
  private Int2ObjectMap<BakedModel> models;

  @Shadow
  public abstract BakedModelManager getModelManager();

  @Inject(at = @At("HEAD"), method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/BakedModel;", cancellable=true)
  public void getModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir) {
    var nbt = stack.getNbt();
    if (nbt != null) {
      if (nbt.contains("modelId")) {
        var modelId = nbt.getString("modelId");
        var identifier = new ModelIdentifier(modelId);
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
