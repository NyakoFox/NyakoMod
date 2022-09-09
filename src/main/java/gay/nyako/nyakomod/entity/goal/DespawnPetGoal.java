package gay.nyako.nyakomod.entity.goal;

import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.PetEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;

import java.util.EnumSet;

public class DespawnPetGoal
        extends Goal {
    private final PetEntity tameable;
    private LivingEntity owner;
    private final WorldView world;
    private int updateCountdownTicks;
    private float oldWaterPathfindingPenalty;

    public DespawnPetGoal(PetEntity tameable) {
        this.tameable = tameable;
        this.world = tameable.world;
    }

    @Override
    public boolean canStart() {
        LivingEntity livingEntity = this.tameable.getOwner();
        if (livingEntity == null) {
            return false;
        }

        var comp = TrinketsApi.getTrinketComponent(livingEntity);
        return comp.isPresent() && !comp.get().isEquipped(NyakoMod.PET_SPRITE_SUMMON_ITEM);
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

