package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.ServerConfigHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class PetSpriteEntity extends PetEntity {
    public Identifier customTextureId;
    protected static final TrackedData<Optional<Text>> TEXTURE_URL = DataTracker.registerData(PetEntity.class, TrackedDataHandlerRegistry.OPTIONAL_TEXT_COMPONENT);

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
    public void tick() {
        super.tick();
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TEXTURE_URL, Optional.empty());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.getCustomSprite() != null) {
            nbt.putString("custom_sprite", this.getCustomSprite().getString());
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        String customSprite = nbt.getString("custom_sprite");

        this.setCustomSprite(customSprite);
    }

    @Nullable
    public Text getCustomSprite() {
        return this.dataTracker.get(TEXTURE_URL).orElse(null);
    }

    public void setCustomSprite(@Nullable String customSprite) {
        var text = Text.of(customSprite);
        this.dataTracker.set(TEXTURE_URL, Optional.ofNullable(text));
    }
}
