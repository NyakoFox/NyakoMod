package gay.nyako.nyakomod.mixin;

import com.mojang.authlib.GameProfile;
import gay.nyako.nyakomod.NyakoCriteria;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.command.XpCommand;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import net.minecraft.SharedConstants;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements PlayerEntityAccess {

    private static final TrackedData<Integer> MILK = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> MILK_SATURATION = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    public int getMilk() {
        return this.dataTracker.get(MILK);
    }

    public void setMilk(int milk) {
        this.dataTracker.set(MILK, milk);
    }

    public int getMilkSaturation() {
        return this.dataTracker.get(MILK_SATURATION);
    }

    public void setMilkSaturation(int milkSaturation) {
        this.dataTracker.set(MILK_SATURATION, milkSaturation);
    }

    public void addMilk(int milk) {
        this.setMilk(Math.min(this.getMilk() + milk, 20));
    }

    public void addMilkSaturation(int milkSaturation) {
        this.setMilkSaturation(Math.min(this.getMilkSaturation() + milkSaturation, 5));
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker()V")
    private void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(MILK, 10);
        this.dataTracker.startTracking(MILK_SATURATION, 2);
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("Milk", this.dataTracker.get(MILK));
        nbt.putInt("MilkSaturation", this.dataTracker.get(MILK_SATURATION));
    }

    @Inject(at = @At("TAIL"), method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        int milk = 10;
        if (nbt.contains("Milk")) {
            milk = nbt.getInt("Milk");
        }
        this.dataTracker.set(MILK, milk);

        int milkSaturation = 2;
        if (nbt.contains("MilkSaturation")) {
            milkSaturation = nbt.getInt("MilkSaturation");
        }
        this.dataTracker.set(MILK_SATURATION, milkSaturation);
    }



    @Override
    public final ActionResult interact(PlayerEntity player, Hand hand) {
        if (!this.isAlive()) {
            return ActionResult.PASS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BUCKET)) {
            var casted = (PlayerEntityAccess) this;
            if (casted.getMilk() >= 5) {
                casted.setMilk(casted.getMilk() - 5);
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f);
                ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, Items.MILK_BUCKET.getDefaultStack());
                player.setStackInHand(hand, itemStack2);
                if (!this.world.isClient) {
                    NyakoCriteria.PLAYER_MILKED.trigger((ServerPlayerEntity) (Object) this);
                }
                this.emitGameEvent(GameEvent.ENTITY_INTERACT);
                return ActionResult.success(this.world.isClient);
            }
        }

        return super.interact(player, hand);
    }
}
