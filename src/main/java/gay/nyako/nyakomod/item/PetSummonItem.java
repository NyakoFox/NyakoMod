package gay.nyako.nyakomod.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.entity.PetEntity;
import gay.nyako.nyakomod.screens.PetSpriteScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

public class PetSummonItem<T extends PetEntity> extends TrinketItem {
    public EntityType<T> entityType;
    CreatePet createPetMethod;

    PetEntity summonedPet;

    public interface CreatePet {
        PetEntity create(ItemStack stack, LivingEntity entity);
    }

    public PetSummonItem(Settings settings, EntityType<T> entityType, CreatePet createPet) {
        super(settings);

        this.createPetMethod = createPet;
        this.entityType = entityType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (canUse(world, player, hand)) {
            return performAction(world, player, hand);
        } else {
            return super.use(world, player, hand);
        }
    }

    public TypedActionResult<ItemStack> performAction(World world, PlayerEntity player, Hand hand) {
        return TypedActionResult.success(player.getStackInHand(hand));
    }

    public PetEntity getSummonedPet() {
        return summonedPet;
    }

    public boolean canUse(World world, PlayerEntity player, Hand hand) {
        return false;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            boolean shouldResummonPet = false;

            if (summonedPet == null || !summonedPet.isAlive()) {
                shouldResummonPet = true;
            } else if (summonedPet.world != entity.world) {
                shouldResummonPet = true;
                summonedPet.remove(Entity.RemovalReason.DISCARDED);
            }

            if (shouldResummonPet) {
                var pet = createPetMethod.create(stack, entity);
                entity.world.spawnEntity(pet);

                summonedPet = pet;
            }
        }
        super.tick(stack, slot, entity);
    }
}