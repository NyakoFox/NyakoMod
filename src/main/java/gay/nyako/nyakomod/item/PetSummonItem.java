package gay.nyako.nyakomod.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.entity.PetEntity;
import gay.nyako.nyakomod.screens.PetSpriteScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

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

    public PetEntity getSummonedPet(ItemStack stack, LivingEntity entity) {
        var nbt = stack.getNbt();
        if (nbt != null && nbt.contains("entity")) {
            var uuid = nbt.getUuid("entity");

            if (entity.world instanceof ServerWorld serverWorld) {
                for (var world : serverWorld.getServer().getWorlds()) {
                    var e = world.getEntity(uuid);
                    if (e instanceof PetEntity petEntity) {
                        return petEntity;
                    }
                }
            }
        }

        return null;
    }

    public void setSummonedPet(ItemStack stack, PetEntity entity) {
        var nbt = stack.getOrCreateNbt();
        nbt.putUuid("entity", entity.getUuid());

        stack.setNbt(nbt);
    }

    public boolean canUse(World world, PlayerEntity player, Hand hand) {
        return false;
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            boolean shouldResummonPet = false;

            var summonedPet = getSummonedPet(stack, entity);

            if (summonedPet == null || !summonedPet.isAlive()) {
                shouldResummonPet = true;
            } else if (summonedPet.world != entity.world) {
                shouldResummonPet = true;
                summonedPet.remove(Entity.RemovalReason.DISCARDED);
            }

            if (shouldResummonPet) {
                var pet = createPetMethod.create(stack, entity);
                entity.world.spawnEntity(pet);

                if (stack.hasCustomName()) {
                    pet.setCustomName(stack.getName());
                }

                setSummonedPet(stack, pet);
            }
        }
        super.tick(stack, slot, entity);
    }
}