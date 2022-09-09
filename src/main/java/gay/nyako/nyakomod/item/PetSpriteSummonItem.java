package gay.nyako.nyakomod.item;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;
import org.spongepowered.include.com.google.common.base.Predicate;

import java.util.UUID;

public class PetSpriteSummonItem extends TrinketItem {
    public PetSpriteSummonItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            var entities = entity.world.getEntitiesByType(NyakoMod.PET_SPRITE,
                    new Box(entity.getX() - 200, entity.getY() - 100, entity.getZ() - 100, entity.getX() + 100, entity.getY() + 100, entity.getZ() + 100),
                    (e) -> e.getOwnerUuid().equals(entity.getUuid()));

            if (entities.size() == 0) {
                var pet = new PetSpriteEntity(NyakoMod.PET_SPRITE, entity.world);
                pet.setOwnerUuid(entity.getUuid());
                pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
                pet.setInvulnerable(true);
                entity.world.spawnEntity(pet);
            } else if (entities.size() > 1) {
                for (var e : entities.subList(1, entities.size())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        super.tick(stack, slot, entity);
    }
}
