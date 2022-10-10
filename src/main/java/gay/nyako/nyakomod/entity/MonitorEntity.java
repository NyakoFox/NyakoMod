package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoSoundEvents;
import gay.nyako.nyakomod.screens.MonitorScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public class MonitorEntity extends AbstractDecorationEntity {

    protected static final TrackedData<String> TEXTURE_URL = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.STRING);
    protected static final TrackedData<Integer> WIDTH = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.INTEGER);
    protected static final TrackedData<Integer> HEIGHT = DataTracker.registerData(MonitorEntity.class, TrackedDataHandlerRegistry.INTEGER);

    public Identifier identifier;

    public MonitorEntity(EntityType<? extends AbstractDecorationEntity> entityType, World world) {
        super(entityType, world);
    }

    public MonitorEntity(World world, BlockPos pos, Direction direction) {
        super(NyakoEntities.MONITOR, world, pos);
        this.setFacing(direction);
    }

    @Override
    protected float getEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return 0.0f;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TEXTURE_URL, "");
        this.dataTracker.startTracking(WIDTH, 1);
        this.dataTracker.startTracking(HEIGHT, 1);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (TEXTURE_URL.equals(data)) setURL(this.dataTracker.get(TEXTURE_URL));
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

    public void setMonitorWidth(int width) {
        this.dataTracker.set(WIDTH, width);
        updateAttachmentPosition();
    }

    public void setMonitorHeight(int height) {
        this.dataTracker.set(HEIGHT, height);
        updateAttachmentPosition();
    }

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
        return 16 * getMonitorWidth();
    }

    @Override
    public int getHeightPixels() {
        return 16 * getMonitorHeight();
    }

    @Override
    public void onPlace() {
        this.playSound(this.getPlaceSound(), 1.0f, 1.0f);
    }

    public SoundEvent getPlaceSound() {
        return SoundEvents.ENTITY_ITEM_FRAME_PLACE;
    }


    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this, this.facing.getId(), this.getDecorationBlockPos());
    }

    @Override
    public void onSpawnPacket(EntitySpawnS2CPacket packet) {
        super.onSpawnPacket(packet);
        this.setFacing(Direction.byId(packet.getEntityData()));
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putByte("Facing", (byte)this.facing.getId());
        nbt.putString("URL", getURL());
        nbt.putInt("Width", getMonitorWidth());
        nbt.putInt("Height", getMonitorHeight());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.setFacing(Direction.byId(nbt.getByte("Facing")));
        setURL(nbt.getString("URL"));
        setMonitorWidth(nbt.getInt("Width"));
        setMonitorHeight(nbt.getInt("Height"));
    }

    @Override
    protected void updateAttachmentPosition() {
        if (this.facing == null) {
            return;
        }
        double add = 0.5;

        double e = (double)this.attachmentPos.getX() + 0.5;
        double f = (double)this.attachmentPos.getY() + 0.5;
        double g = (double)this.attachmentPos.getZ() + 0.5;

        double h = this.method_6893(this.getWidthPixels());
        double i = this.method_6893(this.getHeightPixels());
        e -= (double)this.facing.getOffsetX() * 0.46875;
        f -= (double)this.facing.getOffsetY() * 0.46875;
        g -= (double)this.facing.getOffsetZ() * 0.46875;

        if (this.facing.getAxis().isHorizontal()) {
            Direction direction = this.facing.rotateYCounterclockwise();
            this.setPos(e += h * (double)direction.getOffsetX(), f += i, g += h * (double)direction.getOffsetZ());
        } else {
            this.setPos(e += h, f, g += h);
        }

        h = this.getWidthPixels();
        i = this.getHeightPixels();
        double j = this.getWidthPixels();

        Direction.Axis axis = this.facing.getAxis();
        switch (axis) {
            case X -> h = 1.0;
            case Y -> i = 1.0;
            case Z -> j = 1.0;
        }
        this.setBoundingBox(new Box(e - (h /= 32.0), f - (i /= 32.0), g - (j /= 32.0), e + h, f + i, g + j));
    }

    private double method_6893(int i) {
        return i % 32 == 0 ? 0.5 : 0.0;
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


    @Override
    public void onBreak(@Nullable Entity entity) {
        this.playSound(this.getBreakSound(), 1.0f, 1.0f);
        this.dropStack(this.getAsItemStack());
    }

    public SoundEvent getBreakSound() {
        return SoundEvents.ENTITY_ITEM_FRAME_BREAK;
    }

    public Identifier getIdentifier() {
        return identifier;
    }
}