package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.NyakoItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import qouteall.imm_ptl.core.portal.nether_portal.BreakablePortalEntity;
import qouteall.imm_ptl.core.portal.nether_portal.NetherPortalEntity;

@Mixin(NetherPortalEntity.class)
public abstract class ImmPtlNetherPortalEntityMixin extends BreakablePortalEntity {
    public ImmPtlNetherPortalEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canCollideWithEntity(Entity entity) {
        var portal = (BreakablePortalEntity) (Object) this;
        if (portal instanceof NetherPortalEntity) {
            if (entity instanceof ItemEntity itemEntity) {
                var stack = itemEntity.getStack();
                if (stack.isOf(Items.BUCKET)) {
                    if (!entity.getWorld().isClient()) {
                        markShouldBreak();

                        var netherPortalStack = new ItemStack(NyakoItems.NETHER_PORTAL_BUCKET);
                        entity.getWorld().playSound(null, itemEntity.getBlockPos(), SoundEvents.ITEM_BUCKET_FILL, SoundCategory.BLOCKS, 1.0f, 1.0f);
                        itemEntity.setStack(netherPortalStack);
                    }
                    return true;
                }
            }
        }

        return super.canCollideWithEntity(entity);
    }
}
