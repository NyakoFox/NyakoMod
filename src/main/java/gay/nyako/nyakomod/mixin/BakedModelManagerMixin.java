package gay.nyako.nyakomod.mixin;

import java.util.LinkedHashSet;
import java.util.Map;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.render.model.json.ItemModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gay.nyako.nyakomod.NyakoMod;

import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.resource.metadata.AnimationResourceMetadata;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

@Mixin(BakedModelManager.class)
public abstract class BakedModelManagerMixin {
//   @Shadow
//   public TextureManager textureManager;

//   @Shadow
//   public Map<Identifier, BakedModel> models;

//   @Shadow
//   public SpriteAtlasManager atlasManager;
  
//   public ModelLoader modelLoader;
//   public Profiler profiler;

//   private static final ItemModelGenerator ITEM_MODEL_GENERATOR = new ItemModelGenerator();


//   @Inject(at = @At("HEAD"), method = "apply(Lnet/minecraft/client/render/model/ModelLoader;Lnet/minecraft/resource/ResourceManager;Lnet/minecraft/util/profiler/Profiler;)V", cancellable=true)
//   public void apply(ModelLoader modelLoader, ResourceManager resourceManager, Profiler profiler, CallbackInfo cir) {
//     this.modelLoader = modelLoader;
//     this.profiler = profiler;
//   }

//   @Inject(at = @At("HEAD"), method = "getModel(Lnet/minecraft/client/util/ModelIdentifier;)Lnet/minecraft/client/render/model/BakedModel;", cancellable=true)
//   public void getModel(ModelIdentifier identifier, CallbackInfoReturnable<BakedModel> cir) {
//     if ("nyakomod_custom".equals(identifier.getNamespace())) {
//       var id = new ModelIdentifier(identifier.getNamespace(), identifier.getPath(), "inventory");

//       if (this.models.containsKey(id)) {
//         cir.setReturnValue(this.models.get(id));
//         cir.cancel();
//         return;
//       }


//       var loader = ((ModelLoaderAccessor)(Object)modelLoader);
//       var unbakedModels = loader.getUnbakedModels();
//       var modelsToBake = loader.getModelsToBake();
//       var bakedModels = loader.getBakedModels();

// //      var s = NyakoMod.createItemModelJson(identifier.toString(), "generated");
// //      var unbakedModel = JsonUnbakedModel.deserialize(s);
//       var unbakedModel = NyakoMod.createModel(id);
//       unbakedModels.put(id, unbakedModel);
//       modelsToBake.put(id, unbakedModel);

//       BakedModel model = createBakedModel(unbakedModel, id);

// //      BakedModel model = modelLoader.bake(identifier, ModelRotation.X0_Y0);
//       // this.atlasManager = modelLoader.upload(this.textureManager, profiler);
//       this.models.put(id, model);
//       bakedModels.put(id, model);

//       cir.setReturnValue(model);
//     }
//   }

//   public BakedModel createBakedModel(JsonUnbakedModel unbakedModel, ModelIdentifier identifier) {
//     LinkedHashSet iOException = Sets.newLinkedHashSet();
//     unbakedModel.getTextureDependencies(modelLoader::getOrLoadModel, iOException);

//     var newModel = ITEM_MODEL_GENERATOR.create((id) -> {
//       var atlasId = new Identifier("minecraft:textures/atlas/blocks.png");
//       id = new SpriteIdentifier(atlasId, new Identifier("minecraft:item/diamond"));
//       System.out.println("Getting sprite for " + id);

//       var atlas = this.atlasManager.getAtlas(atlasId);

//       var info = new Sprite.Info(id.getTextureId(), 16, 16, AnimationResourceMetadata.EMPTY);
//       var image = NyakoMod.downloadImage("https://media.discordapp.net/attachments/681973653746745415/920394919712018532/testimonial.png");
//       var sprite = new Sprite(atlas, info, 1, RenderSystem.maxSupportedTextureSize(), RenderSystem.maxSupportedTextureSize())
//       System.out.println("Found: " + sprite.getId());
//       System.out.println(sprite.getWidth() + "," + sprite.getHeight());
//       return sprite;
//     }, unbakedModel);
//     BakedModel model = modelLoader.bake(identifier, ModelRotation.X0_Y0);
// //    BakedModel model = unbakedModel.bake(modelLoader, (id) -> {
// //      id = new SpriteIdentifier(new Identifier("minecraft:textures/atlas/blocks.png"), new Identifier("minecraft:item/diamond"));
// //      System.out.println("Getting sprite for " + id);
// //      var sprite = this.atlasManager.getSprite(id);
// //      System.out.println("Found: " + sprite.getId());
// //      System.out.println(sprite.getWidth() + "," + sprite.getHeight());
// //      return sprite;
// //    }, ModelRotation.X0_Y0, identifier);

//     return model;
//   }

//   public BakedModel getBakedModel(ModelIdentifier identifier) {
    

//     if (!"nyakomod".equals(identifier.getNamespace())) return null;

//     var loader = ((ModelLoaderAccessor)(Object)modelLoader);
//     var unbakedModels = loader.getUnbakedModels();
//     var modelsToBake = loader.getModelsToBake();
//     var unbakedModel = NyakoMod.createModel(identifier);
//     unbakedModels.put(identifier, unbakedModel);
//     modelsToBake.put(identifier, unbakedModel);
//     // System.out.println(loader);
//     // loader.addNewModel(identifier, );
//     modelLoader.upload(this.textureManager, profiler);
//     var models = modelLoader.getBakedModelMap();
//     var model = models.get(identifier);
//     this.models.put(identifier, model);

//     return model;
//     // return null;
//   }
}
