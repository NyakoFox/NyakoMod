package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ModelLoader.class)
public interface ModelLoaderAccessor {
    @Accessor
    public Map<Identifier, UnbakedModel> getUnbakedModels();
    @Accessor
    public Map<Identifier, UnbakedModel> getModelsToBake();
    @Accessor
    public Map<Identifier, BakedModel> getBakedModels();

}