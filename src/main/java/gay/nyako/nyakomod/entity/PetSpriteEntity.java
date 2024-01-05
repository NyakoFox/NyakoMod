package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoClientMod;
import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.entity.goal.DespawnPetGoal;
import gay.nyako.nyakomod.entity.goal.PetFollowOwnerGoal;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.EntityView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class PetSpriteEntity extends PetEntity {
    public Identifier customTextureId;
    protected static final TrackedData<String> TEXTURE_URL = DataTracker.registerData(PetSpriteEntity.class, TrackedDataHandlerRegistry.STRING);
    protected static final TrackedData<Float> PET_SIZE = DataTracker.registerData(PetSpriteEntity.class, TrackedDataHandlerRegistry.FLOAT);
    protected static final TrackedData<Boolean> STILL = DataTracker.registerData(PetSpriteEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

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

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(0, new DespawnPetGoal(this));
        if (!this.isStill()) {
            this.goalSelector.add(0, new SwimGoal(this));
            this.goalSelector.add(0, new EscapeDangerGoal(this, 1.25));
            this.goalSelector.add(2, new PetFollowOwnerGoal(this, 1.0, 5, 10.0f, false));
            this.goalSelector.add(10, new LookAroundGoal(this));
            this.goalSelector.add(3, new WanderAroundFarGoal(this, 1.0));
        }
    }

    public static PetEntity createPet(ItemStack stack, LivingEntity entity) {
        var pet = new PetSpriteEntity(NyakoEntities.PET_SPRITE, entity.getWorld());
        pet.setOwnerUuid(entity.getUuid());
        pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
        pet.setInvulnerable(true);
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("custom_sprite")) {
            pet.setCustomSprite(nbt.getString("custom_sprite"));
        }
        if (nbt.contains("pet_size")) {
            pet.setPetSize(nbt.getFloat("pet_size"));
        }
        if (nbt.contains("still")) {
            pet.setStill(nbt.getBoolean("still"));
            if (pet.isStill())
            {
                //pet.goalSelector.clear(new Predicate);
                for (var goal : pet.goalSelector.getGoals()) {
                    pet.goalSelector.remove(goal);
                }
                pet.goalSelector.add(1, new LookAtEntityGoal(pet, PlayerEntity.class, 8.0f));
                pet.goalSelector.add(0, new DespawnPetGoal(pet));
            }
        }

        return pet;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        var size = getPetSize();
        return super.getDimensions(pose).scaled(1f, size);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TEXTURE_URL, "");
        this.dataTracker.startTracking(PET_SIZE, 2f);
        this.dataTracker.startTracking(STILL, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getCustomSprite() != null) {
            nbt.putString("custom_sprite", this.getCustomSprite());
            nbt.putDouble("pet_size", this.getPetSize());
            nbt.putBoolean("still", this.isStill());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        String customSprite = nbt.getString("custom_sprite");
        float petSize = nbt.getFloat("pet_size");
        boolean still = nbt.getBoolean("still");

        this.setCustomSprite(customSprite);
        this.setPetSize(petSize);
        this.setStill(still);
    }

    @Nullable
    public String getCustomSprite() {
        return this.dataTracker.get(TEXTURE_URL);
    }

    public float getPetSize() {
        return this.dataTracker.get(PET_SIZE);
    }

    public void setCustomSprite(@Nullable String customSprite) {
        this.dataTracker.set(TEXTURE_URL, customSprite);

        if (getWorld().isClient() && customSprite != null) {
            customTextureId = null;
            if (!customSprite.isEmpty()) {
                (new Thread(() -> customTextureId = NyakoClientMod.downloadSprite(customSprite))).start();
            }
        }
    }

    public void setPetSize(float size) {
        this.dataTracker.set(PET_SIZE, size);

        this.calculateDimensions();
    }

    public void setStill(boolean still) {
        this.dataTracker.set(STILL, still);
    }

    public boolean isStill() {
        return this.dataTracker.get(STILL);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (TEXTURE_URL.equals(data)) {
            setCustomSprite(this.dataTracker.get(TEXTURE_URL));
        }
        if (PET_SIZE.equals(data)) {
            setPetSize(this.dataTracker.get(PET_SIZE));
        }
        if (STILL.equals(data)) {
            setStill(this.dataTracker.get(STILL));
        }

        super.onTrackedDataSet(data);
    }

    @Override
    public EntityView method_48926() {
        return getWorld();
    }
}
