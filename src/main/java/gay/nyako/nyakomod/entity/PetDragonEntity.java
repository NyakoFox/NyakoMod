package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoMod;
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
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PetDragonEntity extends PetEntity {
    public PetDragonEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
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
        var pet = new PetDragonEntity(NyakoMod.PET_DRAGON, entity.world);
        pet.setOwnerUuid(entity.getUuid());
        pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
        pet.setInvulnerable(true);
        return pet;
    }
}
