package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoPaintingVariants;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PaintingEntity.class)
public abstract class PaintingEntityMixin extends Entity {
    @Shadow public abstract RegistryEntry<PaintingVariant> getVariant();

    @Shadow public abstract void onBreak(@Nullable Entity entity);

    public PaintingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public void tick() {
        if (getVariant().value() == NyakoPaintingVariants.HEDGEHOG_ECHIDNA)
        {
            if (age > 2)
            {
                if (!this.isRemoved()) {
                    this.discard();
                    this.onBreak(null);
                }
            }
        }
        super.tick();
    }
}
