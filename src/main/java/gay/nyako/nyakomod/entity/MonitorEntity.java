package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public class MonitorEntity extends AbstractDecorationEntity {

    private static final TrackedData<ItemStack> ITEM_STACK = DataTracker.registerData(ItemFrameEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);

    public MonitorEntity(EntityType entityType, World world) {
        super(entityType, world);
    }

    public MonitorEntity(World world, BlockPos pos, Direction facing) {
        this(NyakoMod.MONITOR_ENTITY, world, pos, facing);
    }

    public MonitorEntity(EntityType type, World world, BlockPos pos, Direction facing) {
        super(type, world, pos);
        this.setFacing(facing);
    }

    @Override
    protected void initDataTracker() {
        this.getDataTracker().startTracking(ITEM_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void setFacing(Direction facing) {
        Validate.notNull(facing);
        this.facing = facing;
        if (facing.getAxis().isHorizontal()) {
            this.setPitch(0.0f);
            this.setYaw(this.facing.getHorizontal() * 90);
        } else {
            this.setPitch(-90 * facing.getDirection().offset());
            this.setYaw(0.0f);
        }
        this.prevPitch = this.getPitch();
        this.prevYaw = this.getYaw();
        this.updateAttachmentPosition();
    }

    @Override
    public int getWidthPixels() {
        return 16;
    }

    @Override
    public int getHeightPixels() {
        return 16;
    }

    @Override
    public void onBreak(@Nullable Entity entity) {

    }

    @Override
    public void onPlace() {

    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, getType(), this.facing.getId(), this.getDecorationBlockPos());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (!this.getHeldItemStack().isEmpty()) {
            nbt.put("Item", this.getHeldItemStack().writeNbt(new NbtCompound()));
        }
        nbt.putByte("Facing", (byte)this.facing.getId());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        NbtCompound nbtCompound = nbt.getCompound("Item");
        if (nbtCompound != null && !nbtCompound.isEmpty()) {
            ItemStack itemStack = ItemStack.fromNbt(nbtCompound);
            this.setHeldItemStack(itemStack, false);
        }
        this.setFacing(Direction.byId(nbt.getByte("Facing")));
    }

    public ItemStack getHeldItemStack() {
        return this.getDataTracker().get(ITEM_STACK);
    }

    public void setHeldItemStack(ItemStack stack) {
        this.setHeldItemStack(stack, true);
    }

    public void setHeldItemStack(ItemStack value, boolean update) {
        if (!value.isEmpty()) {
            value = value.copy();
            value.setCount(1);
            value.setHolder(this);
        }
        this.getDataTracker().set(ITEM_STACK, value);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        ItemStack itemStack;
        if (data.equals(ITEM_STACK) && !(itemStack = this.getHeldItemStack()).isEmpty()) {
            itemStack.setHolder(this);
        }
    }

    @Override
    protected void updateAttachmentPosition() {
        if (this.facing == null) {
            return;
        }
        double e = (double)this.attachmentPos.getX() + 0.5 - (double)this.facing.getOffsetX() * 0.46875;
        double f = (double)this.attachmentPos.getY() + 0.5 - (double)this.facing.getOffsetY() * 0.46875;
        double g = (double)this.attachmentPos.getZ() + 0.5 - (double)this.facing.getOffsetZ() * 0.46875;
        this.setPos(e, f, g);
        double h = this.getWidthPixels();
        double i = this.getHeightPixels();
        double j = this.getWidthPixels();
        Direction.Axis axis = this.facing.getAxis();
        switch (axis) {
            case X: {
                h = 1.0;
                break;
            }
            case Y: {
                i = 1.0;
                break;
            }
            case Z: {
                j = 1.0;
            }
        }
        this.setBoundingBox(new Box(e - (h /= 32.0), f - (i /= 32.0), g - (j /= 32.0), e + h, f + i, g + j));
    }

    @Override
    public boolean canStayAttached() {
        if (!this.world.isSpaceEmpty(this)) {
            return false;
        }
        BlockState blockState = this.world.getBlockState(this.attachmentPos.offset(this.facing.getOpposite()));
        if (!(blockState.getMaterial().isSolid() || this.facing.getAxis().isHorizontal() && AbstractRedstoneGateBlock.isRedstoneGate(blockState))) {
            return false;
        }
        return this.world.getOtherEntities(this, this.getBoundingBox(), PREDICATE).isEmpty();
    }
}
