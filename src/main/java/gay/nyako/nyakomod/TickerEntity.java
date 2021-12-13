package gay.nyako.nyakomod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class TickerEntity extends Entity {

    private static final TrackedData<Integer> SPEED = DataTracker.registerData(TickerEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public TickerEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public static TickerEntity summon(World world, BlockPos pos, Direction direction) {
        return new TickerEntity(NyakoMod.TICKER, world);
    }

    @Override
    public void tick() {
        super.tick();

        BlockPos currentBlockPos = new BlockPos(getX(), getY(), getZ());

        if(!world.isClient()) {
            BlockEntity blockEntity = getEntityWorld().getBlockEntity(currentBlockPos);
            if (blockEntity == null) {
                // This... isn't even a block entity?
                destroyTicker();
                return;
            }
            Block block = blockEntity.getCachedState().getBlock();
            BlockEntityProvider blockEntityProvider = (BlockEntityProvider) block;

            if (blockEntityProvider.getTicker(blockEntity.getWorld(),blockEntity.getCachedState(),blockEntity.getType()) == null) {
                // This isn't a tickable block!!
                destroyTicker();
                return;
            }

            for(int i = 0; i < Math.pow(getSpeed(),2); i++) {
                //blockEntityProvider.getTicker(blockEntity.getWorld(),blockEntity.getCachedState(),blockEntity.getType()).tick(blockEntity.getWorld(),blockEntity.getPos(),blockEntity.getCachedState(),null);

                BlockEntityTicker ticker = blockEntityProvider.getTicker(blockEntity.getWorld(),blockEntity.getCachedState(),blockEntity.getType());
                ticker.tick(blockEntity.getWorld(),blockEntity.getPos(),blockEntity.getCachedState(), blockEntity);
            }

            if (age >= 600) {
                destroyTicker();
                return;
            }

        }
    }

    public void destroyTicker() {
        BlockPos currentBlockPos = new BlockPos(getX(), getY(), getZ());
        getEntityWorld().playSound(null, currentBlockPos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 1);
        kill();
    }

    public void setSpeed(int speed){
        if (!world.isClient) {
            getDataTracker().set(SPEED, speed);
        }
    }

    public int getSpeed(){
        return getDataTracker().get(SPEED);
    }

    @Override
    protected void initDataTracker() {
        getDataTracker().startTracking(SPEED, 1);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound tag) {
        if (tag.contains("Speed")) {
            setSpeed(tag.getInt("Speed"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound tag) {
        tag.putInt("Speed", getSpeed());
    }

    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, getId());
    }
}