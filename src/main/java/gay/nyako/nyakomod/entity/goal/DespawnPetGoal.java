package gay.nyako.nyakomod.entity.goal;

import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.PetEntity;
import gay.nyako.nyakomod.item.PetSummonItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class DespawnPetGoal
        extends Goal {
    private final PetEntity tameable;

    public DespawnPetGoal(PetEntity tameable) {
        this.tameable = tameable;
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity == null) {
            return false;
        }

        if (livingEntity.getWorld().isClient()) {
            return false;
        }

        var stack = this.tameable.getSummonItem();

        if (stack != null && stack.getCount() > 0 && stack.getItem() instanceof PetSummonItem<?> item) {
            if (!this.tameable.getType().equals(item.entityType)) {
                return true;
            }

            return item.getSummonedPet(stack, livingEntity) != this.tameable;
        }

        return true;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        tameable.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void stop() {
    }

    @Override
    public void tick() {
    }
}

