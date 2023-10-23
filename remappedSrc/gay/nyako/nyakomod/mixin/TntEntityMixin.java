package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.TntEntityAccess;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(TntEntity.class)
public abstract class TntEntityMixin extends Entity implements TntEntityAccess {
    private boolean modifiedBlockState = false;

    private static final TrackedData<Optional<BlockState>> BLOCKSTATE_TYPE = DataTracker.registerData(TntEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE);

    public TntEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    private void init(CallbackInfo callbackInfo) {
        this.dataTracker.startTracking(BLOCKSTATE_TYPE, Optional.of(Blocks.TNT.getDefaultState()));
    }

    @Override
    public void setCopyBlockState(BlockState state) {
        dataTracker.set(BLOCKSTATE_TYPE, Optional.of(state));
        modifiedBlockState = true;
    }

    @Override
    public BlockState getCopyBlockState() {
        return dataTracker.get(BLOCKSTATE_TYPE).orElse(Blocks.TNT.getDefaultState());
    }

    @Inject(method = "writeCustomDataToNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("modifiedBlockState", modifiedBlockState);
        nbt.putString("blockState", Registry.BLOCK.getId(getCopyBlockState().getBlock()).toString());
    }

    @Inject(method = "readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        modifiedBlockState = nbt.getBoolean("modifiedBlockState");
        if (modifiedBlockState) {
            var identifier = Identifier.tryParse(nbt.getString("blockState"));
            if (identifier != null) {
                var state = Registry.BLOCK.get(identifier);
                setCopyBlockState(state.getDefaultState());
            }
        }
    }

    @Redirect(method = "explode()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;createExplosion(Lnet/minecraft/entity/Entity;DDDFLnet/minecraft/world/explosion/Explosion$DestructionType;)Lnet/minecraft/world/explosion/Explosion;"))
    private Explosion injected(World instance, Entity entity, double x, double y, double z, float power, Explosion.DestructionType destructionType) {
        return instance.createExplosion(entity, x, y, z, power, modifiedBlockState ? Explosion.DestructionType.NONE : destructionType);
    }
}