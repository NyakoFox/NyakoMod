package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PetSpriteEntity extends PetEntity {
    public static final TrackedDataHandler<Optional<Double>> OPTIONAL_DOUBLE_COMPONENT = TrackedDataHandler.ofOptional(PacketByteBuf::writeDouble, PacketByteBuf::readDouble);

    static {
        TrackedDataHandlerRegistry.register(OPTIONAL_DOUBLE_COMPONENT);
    }

    public Identifier customTextureId;
    protected static final TrackedData<Optional<Text>> TEXTURE_URL = DataTracker.registerData(PetSpriteEntity.class, TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);
    protected static final TrackedData<Optional<Double>> PET_SIZE = DataTracker.registerData(PetSpriteEntity.class, OPTIONAL_DOUBLE_COMPONENT);

    public PetSpriteEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createPetAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0)
                .add(EntityAttributes.GENERIC_FLYING_SPEED, 1f)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0);
    }

    public static PetEntity createPet(ItemStack stack, LivingEntity entity) {
        var pet = new PetSpriteEntity(NyakoEntities.PET_SPRITE, entity.world);
        pet.setOwnerUuid(entity.getUuid());
        pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
        pet.setInvulnerable(true);
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("custom_sprite")) {
            pet.setCustomSprite(nbt.getString("custom_sprite"));
        }
        if (nbt.contains("pet_size")) {
            pet.setPetSize(nbt.getDouble("pet_size"));
        }

        return pet;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var size = getPetSize();
        return super.getDimensions(pose).scaled(1f, size.floatValue());
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TEXTURE_URL, Optional.empty());
        this.dataTracker.startTracking(PET_SIZE, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getCustomSprite() != null) {
            nbt.putString("custom_sprite", this.getCustomSprite().getString());
            nbt.putDouble("pet_size", this.getPetSize());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        String customSprite = nbt.getString("custom_sprite");
        Double petSize = nbt.getDouble("pet_size");

        this.setCustomSprite(customSprite);
        this.setPetSize(petSize);
    }

    @Nullable
    public Text getCustomSprite() {
        return this.dataTracker.get(TEXTURE_URL).orElse(null);
    }

    public Double getPetSize() {
        return this.dataTracker.get(PET_SIZE).orElse(2.0);
    }

    public void setCustomSprite(@Nullable String customSprite) {
        var text = Text.of(customSprite);
        this.dataTracker.set(TEXTURE_URL, Optional.ofNullable(text));
    }

    public void setPetSize(Double size) {
        this.dataTracker.set(PET_SIZE, Optional.of(size));

        this.calculateDimensions();
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        calculateDimensions();
        super.onTrackedDataSet(data);
    }
}
