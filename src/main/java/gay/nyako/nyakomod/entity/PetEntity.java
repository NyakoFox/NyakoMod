package gay.nyako.nyakomod.entity;

import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

public class PetEntity extends PathAwareEntity {
    public PetEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);

        this.setPathfindingPenalty(PathNodeType.POWDER_SNOW, -1.0f);
        this.setPathfindingPenalty(PathNodeType.DANGER_POWDER_SNOW, -1.0f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.goalSelector.add(1, new WanderAroundGoal(this, 5.0, 100));
    }

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        if (world.getBlockState(pos.down()).isOf(Blocks.GRASS_BLOCK)) {
            return 10.0f;
        }
        return world.getPhototaxisFavor(pos);
    }

    @Override
    public void tick() {
        // this.goalSelector.tick();

        var runningGoal = this.goalSelector.getRunningGoals().findFirst();
        if (runningGoal.isPresent()) {
            Log.debug(LogCategory.LOG, "goal is running: " + runningGoal.toString());
        }

        super.tick();
    }
}
