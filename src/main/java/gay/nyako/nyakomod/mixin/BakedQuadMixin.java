package gay.nyako.nyakomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.texture.Sprite;

@Mixin(BakedQuad.class)
public class BakedQuadMixin {
  @Shadow
  public Sprite sprite;

  @Inject(at = @At("HEAD"), method = "getSprite()Lnet/minecraft/client/texture/Sprite;", cancellable=true)
  public void getSprite(CallbackInfoReturnable<Sprite> cir) {
    System.out.println("Getting sprite " + this.sprite.getId());
  }
}
