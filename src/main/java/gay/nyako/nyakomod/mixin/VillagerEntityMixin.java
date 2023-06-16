package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.VillagerEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import gay.nyako.nyakomod.item.SoulJarItem;

import java.util.Optional;

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements VillagerEntityAccess {

	private static final TrackedData<Boolean> PEELED_TYPE = DataTracker.registerData(VillagerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
		super(entityType, world);
	}

	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void init(CallbackInfo callbackInfo) {
		this.dataTracker.startTracking(PEELED_TYPE, false);
	}

	@Override
	public void setPeeled(boolean peeled) {
		dataTracker.set(PEELED_TYPE, peeled);
	}

	@Override
	public boolean isPeeled() {
		return dataTracker.get(PEELED_TYPE);
	}

	@Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
	private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
		nbt.putBoolean("peeled", isPeeled());
	}

	@Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
	private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
		setPeeled(nbt.getBoolean("peeled"));
	}

	@Inject(at = @At("HEAD"), method = "interactMob(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;", cancellable = true)
	public void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() instanceof SoulJarItem) {
			cir.setReturnValue(ActionResult.PASS);
		} else if (itemStack.getItem() instanceof ShearsItem) {
			if (!this.isPeeled()) {
				this.setPeeled(true);
				itemStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
				cir.setReturnValue(ActionResult.SUCCESS);
				player.getWorld().playSound(player, this.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, this.getSoundCategory(), 1.0F, 1.0F);
			} else {
				cir.setReturnValue(ActionResult.PASS);
			}
		}
	}
}
