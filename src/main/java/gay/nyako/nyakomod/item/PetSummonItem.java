package gay.nyako.nyakomod.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.entity.PetEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Box;

public class PetSummonItem<T extends PetEntity> extends TrinketItem {
    public EntityType<T> entityType;
    CreatePet createPetMethod;

    public interface CreatePet {
        PetEntity create(ItemStack stack, LivingEntity entity);
    }

    public PetSummonItem(Settings settings, EntityType<T> entityType, CreatePet createPet) {
        super(settings);

        this.createPetMethod = createPet;
        this.entityType = entityType;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            var entities = entity.world.getEntitiesByType(entityType,
                    new Box(entity.getX() - 200, entity.getY() - 100, entity.getZ() - 100, entity.getX() + 100, entity.getY() + 100, entity.getZ() + 100),
                    (e) -> e.getOwnerUuid() != null && e.getOwnerUuid().equals(entity.getUuid()));

            if (entities.size() == 0) {
                var pet = createPetMethod.create(stack, entity);
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