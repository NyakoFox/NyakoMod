package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.*;
import gay.nyako.nyakomod.screens.MonitorScreen;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.AbstractRedstoneGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public class MonitorEntity extends Entity {

    protected static final TrackedData<String> TEXTURE_URL = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.STRING);
    protected static final TrackedData<Direction> FACING = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.FACING);
    protected static final TrackedData<Integer> WIDTH = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> HEIGHT = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public Identifier identifier;

    private static final double THICKNESS = 1D / 16D;
    private static final int MAX_WIDTH = 8;
    private static final int MAX_HEIGHT = 8;

    public MonitorEntity(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        setBoundingBox(new Box(0D, 0D, 0D, 0D, 0D, 0D));
    }

    public MonitorEntity(World world, BlockPos pos, Direction direction) {
        super(NyakoEntities.MONITOR, world);
        this.setPosition(pos.getX(), pos.getY(), pos.getZ());
        this.setFacing(direction);
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.0f;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(TEXTURE_URL, "");
        this.dataTracker.startTracking(FACING, Direction.NORTH);
        this.dataTracker.startTracking(WIDTH, 1);
        this.dataTracker.startTracking(HEIGHT, 1);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (TEXTURE_URL.equals(data)) setURL(this.dataTracker.get(TEXTURE_URL));
        if (FACING.equals(data)) setFacing(this.dataTracker.get(FACING));
        if (WIDTH.equals(data)) setMonitorWidth(this.dataTracker.get(WIDTH));
        if (HEIGHT.equals(data)) setMonitorHeight(this.dataTracker.get(HEIGHT));

        super.onTrackedDataSet(data);
    }

    public int getMonitorWidth() {
        return this.dataTracker.get(WIDTH);
    }

    public int getMonitorHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    public boolean setMonitorWidth(int width) {
        width = MathHelper.clamp(width, 1, MAX_WIDTH);

        var oldWidth = getMonitorWidth();
        this.dataTracker.set(WIDTH, width);
        return width != oldWidth;
    }

    public boolean setMonitorHeight(int height) {
        height = MathHelper.clamp(height, 1, MAX_HEIGHT);

        var oldHeight = getMonitorHeight();
        this.dataTracker.set(HEIGHT, height);

        return height != oldHeight;
        //updateClient();
    }

    /*public void updateClient() {
        if (world.isClient()) return;
        ServerWorld serverWorld = (ServerWorld) world;
        for (PlayerEntity player : world.getPlayers()) {
            System.out.println("sending packet to " + player.getName().getString());
            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeInt(this.getId());
            passedData.writeBlockPos(this.getBlockPos());
            passedData.writeDouble(this.getPos().x);
            passedData.writeDouble(this.getPos().y);
            passedData.writeDouble(this.getPos().z);
            passedData.writeBlockPos(this.getDecorationBlockPos());
            passedData.writeInt(this.getMonitorWidth());
            passedData.writeInt(this.getMonitorHeight());
            ServerPlayNetworking.send((ServerPlayerEntity) player, NyakoNetworking.MONITOR_UPDATE, passedData);
        }
    }*/

    public String getURL() {
        return this.dataTracker.get(TEXTURE_URL);
    }

    public void setURL(String newURL) {
        this.dataTracker.set(TEXTURE_URL, newURL);
        if (world.isClient()) {
            identifier = null;
            if (!newURL.equals("")) {
                identifier = NyakoClientMod.downloadSprite(newURL);
            }
        }
    }

    @Override
    public ActionResult interact(PlayerEntity player, Hand hand) {
        var canModify = true;

        if (!canModify) return ActionResult.FAIL;

        if (player.isSneaking()) {
            if (world.isClient()) {
                // Open GUI here
                openGUI();
            }
            return ActionResult.SUCCESS;
        }

        return super.interact(player, hand);
    }

    @Environment(EnvType.CLIENT)
    public void openGUI() {
        var client = MinecraftClient.getInstance();
        client.setScreen(new MonitorScreen(this));
    }

    public int getWidthPixels() {
        return 16 * getMonitorWidth();
    }

    public int getHeightPixels() {
        return 16 * getMonitorHeight();
    }

    public void onPlace() {
        this.playSound(this.getPlaceSound(), 1.0f, 1.0f);
    }

    public SoundEvent getPlaceSound() {
        return SoundEvents.ENTITY_ITEM_FRAME_PLACE;
    }

    public void setFacing(Direction facing) {
        dataTracker.set(FACING, facing);
        updateBoundingBox();
    }

    public Direction getFacing() {
        return dataTracker.get(FACING);
    }

    private void updateBoundingBox() {
        Direction facing = getFacing();

        if (facing.getAxis().isHorizontal()) {
            setPitch(0.0f);
            setYaw(facing.getHorizontal() * 90);
        } else {
            setPitch(-90 * facing.getDirection().offset());
            setYaw(0.0f);
        }
        prevYaw = getYaw();
        prevPitch = getPitch();
        setBoundingBox(calculateBoundingBox());
    }

    @Override
    protected Box calculateBoundingBox() {
        return calculateBoundingBox(getBlockPos(), getFacing(), getMonitorWidth(), getMonitorHeight());
    }

    private Box calculateBoundingBox(BlockPos pos, Direction facing, double width, double height) {
        return switch (facing) {
            default -> new Box(pos.getX() + 1D, pos.getY(), pos.getZ() + 1D - THICKNESS, pos.getX() - width + 1D, pos.getY() + height, pos.getZ() + 1D);
            case SOUTH -> new Box(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + width, pos.getY() + height, pos.getZ() + THICKNESS);
            case WEST -> new Box(pos.getX() + 1D - THICKNESS, pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + height, pos.getZ() + width);
            case EAST -> new Box(pos.getX(), pos.getY(), pos.getZ() + 1D, pos.getX() + THICKNESS, pos.getY() + height, pos.getZ() - width + 1D);
        };
    }

    public BlockPos getCenterPosition() {
        Vec3d center = getCenter(getBoundingBox());
        return new BlockPos(center.x, center.y, center.z);
    }

    public Vec3d getCenter(Box aabb) {
        return new Vec3d(aabb.minX + (aabb.maxX - aabb.minX) * 0.5D, aabb.minY + (aabb.maxY - aabb.minY) * 0.5D, aabb.minZ + (aabb.maxZ - aabb.minZ) * 0.5D);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, getFacing().getId(), getBlockPos());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putByte("Facing", (byte)getFacing().getId());
        nbt.putString("URL", getURL());
        nbt.putInt("Width", getMonitorWidth());
        nbt.putInt("Height", getMonitorHeight());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setFacing(Direction.byId(nbt.getByte("Facing")));
        setURL(nbt.getString("URL"));
        setMonitorWidth(nbt.getInt("Width"));
        setMonitorHeight(nbt.getInt("Height"));
    }

    @Override
    public void tick() {
        updateBoundingBox();
        super.tick();
        checkValid();
    }

    public boolean isValid() {
        return world.canCollide(this, getBoundingBox()) && world.getEntitiesByClass(MonitorEntity.class, getBoundingBox().contract(getFacing().getOffsetX() == 0 ? 2D / 16D : 0D, getFacing().getOffsetY() == 0 ? 2D / 16D : 0D, getFacing().getOffsetZ() == 0 ? 2D / 16D : 0D), image -> image != this).isEmpty();
    }

    public void checkValid() {
        if (!isValid()) {
            removeMonitor(null);
        }
    }

    @Override
    protected boolean shouldSetPositionOnLoad() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source == DamageSource.OUT_OF_WORLD || source.isSourceCreativePlayer()) {
            return super.damage(source, amount);
        }
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        return super.damage(source, amount);
    }


    @Override
    public ItemStack getPickBlockStack() {
        return this.getAsItemStack();
    }

    protected ItemStack getAsItemStack() {
        return new ItemStack(NyakoItems.MONITOR);
    }

    public void removeMonitor(Entity entity) {
        if (!isRemoved() && !world.isClient()) {
            onBreak(entity);
            kill();
        }
    }

    public void onBreak(@Nullable Entity entity) {
        this.playSound(this.getBreakSound(), 1.0f, 1.0f);
        if (entity instanceof PlayerEntity playerEntity) {
            if (playerEntity.isCreative()) {
                return;
            }
        }
        this.dropStack(this.getAsItemStack());
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.ENTITY_ITEM_FRAME_BREAK;
    }

    public Identifier getIdentifier() {
        return identifier;
    }

    public void resize(MonitorDirection direction, boolean larger) {
        int amount = larger ? 1 : -1;
        switch (direction) {
            case UP:
                setMonitorHeight(getMonitorHeight() + amount);
                break;
            case DOWN:
                if (setMonitorHeight(getMonitorHeight() + amount)) {
                    setImagePosition(getBlockPos().offset(Direction.DOWN, amount));
                }
                break;
            case RIGHT:
                setMonitorWidth(getMonitorWidth() + amount);
                break;
            case LEFT:
                if (setMonitorWidth(getMonitorWidth() + amount)) {
                    setImagePosition(getBlockPos().offset(getResizeOffset(), amount));
                }
                break;
        }
    }

    public void setImagePosition(BlockPos position) {
        updatePositionAndAngles(position.getX() + 0.5D, position.getY(), position.getZ() + 0.5D, getPitch(), getYaw());
        updateBoundingBox();
    }

    private Direction getResizeOffset() {
        return switch (getFacing()) {
            case WEST -> Direction.NORTH;
            case NORTH -> Direction.EAST;
            case SOUTH -> Direction.WEST;
            default -> Direction.SOUTH;
        };
    }
}