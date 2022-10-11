package gay.nyako.nyakomod.mixin;

import com.mojang.authlib.GameProfile;
import gay.nyako.nyakomod.NyakoCriteria;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.access.PlayerEntityAccess;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.command.XpCommand;
import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.utils.NyakoUtils;
import net.minecraft.SharedConstants;
import net.minecraft.block.Blocks;
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
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
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
    private static final TrackedData<Integer> MILK_TIMER = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);

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

    public void setMilkTimer(int time) {
        this.dataTracker.set(MILK_TIMER, time);
    }

    public int getMilkTimer() {
        return this.dataTracker.get(MILK_TIMER);
    }

    @Inject(at = @At("TAIL"), method = "initDataTracker()V")
    private void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(MILK, 10);
        this.dataTracker.startTracking(MILK_SATURATION, 2);
        this.dataTracker.startTracking(MILK_TIMER, 0);
    }

    @Inject(at = @At("TAIL"), method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V")
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("Milk", this.dataTracker.get(MILK));
        nbt.putInt("MilkSaturation", this.dataTracker.get(MILK_SATURATION));
        nbt.putInt("MilkTimer", this.dataTracker.get(MILK_TIMER));
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

        int milkTimer = 0;
        if (nbt.contains("MilkTimer")) {
            milkTimer = nbt.getInt("MilkTimer");
        }
        this.dataTracker.set(MILK_TIMER, milkTimer);
    }



    @Override
    public final ActionResult interact(PlayerEntity player, Hand hand) {
        if (!this.isAlive()) {
            return ActionResult.PASS;
        }

        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.BUCKET)) {
            if (NyakoUtils.blockedByShield(this, player.getPos())) {
                this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
                player.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 1.0F);
                return ActionResult.success(this.world.isClient);
            }
            var casted = (PlayerEntityAccess) this;
            if (casted.getMilk() >= 5) {
                casted.setMilk(casted.getMilk() - 5);
                player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0f, 1.0f);

                var milkStack = Items.MILK_BUCKET.getDefaultStack();

                NbtCompound nbt = milkStack.getOrCreateNbt();
                nbt.putBoolean("isFromPlayer", true);
                nbt.putUuid("playerUuid", this.getUuid());
                NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
                NbtList nbtLore = nbtDisplay.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);
                nbtLore.add(NbtString.of(Text.Serializer.toJson(Text.translatable("item.nyakomod.milk_bucket.tooltip", this.getName()).formatted(Formatting.GRAY))));
                nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
                nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
                milkStack.setNbt(nbt);

                ItemStack itemStack2 = ItemUsage.exchangeStack(itemStack, player, milkStack);

                player.setStackInHand(hand, itemStack2);
                if (!this.world.isClient) {
                    NyakoCriteria.PLAYER_MILKED.trigger((ServerPlayerEntity) (Object) this);
                    player.getScoreboard().forEachScore(NyakoMod.TIMES_MILKED_CRITERIA, this.getEntityName(), score -> score.setScore(score.getScore() + 1));
                    player.getScoreboard().forEachScore(NyakoMod.PLAYERS_MILKED_CRITERIA, player.getEntityName(), score -> score.setScore(score.getScore() + 1));
                }
                this.emitGameEvent(GameEvent.ENTITY_INTERACT);
                return ActionResult.success(this.world.isClient);
            }
        }

        return super.interact(player, hand);
    }
}
