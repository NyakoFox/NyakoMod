package gay.nyako.nyakomod.entity;

import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.world.World;

public class HerobrineEntity extends HostileEntity {
    public HerobrineEntity(EntityType<? extends HerobrineEntity> entityType, World world) {
        super(entityType, world);
        setInvulnerable(true);
        setAiDisabled(true);
        setCustomName(Text.translatable("entity.nyakomod.herobrine"));
        setCustomNameVisible(true);
        setEquipmentDropChance(EquipmentSlot.MAINHAND, 0.0f);
        setEquipmentDropChance(EquipmentSlot.FEET, 0.0f);
        setEquipmentDropChance(EquipmentSlot.LEGS, 0.0f);
        setEquipmentDropChance(EquipmentSlot.CHEST, 0.0f);
        setEquipmentDropChance(EquipmentSlot.HEAD, 0.0f);
    }

    public HerobrineEntity(World world) {
        this(NyakoEntities.HEROBRINE, world);
    }

    public static DefaultAttributeContainer.Builder createHerobrineAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 64.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.5)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ARMOR, 0.0f)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 8.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.initCustomGoals();
    }

    protected void initCustomGoals() {
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0, false));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
    }

    @Override
    public void tick() {
        if (this.age % 20 == 0) {
            this.heal(1.0f);
        }

        if (this.age == 20)
        {
            equipStack(EquipmentSlot.FEET, new ItemStack(Items.NETHERITE_BOOTS));
        }

        if (this.age == 40)
        {
            equipStack(EquipmentSlot.LEGS, new ItemStack(Items.NETHERITE_LEGGINGS));
        }

        if (this.age == 60)
        {
            equipStack(EquipmentSlot.CHEST, new ItemStack(Items.NETHERITE_CHESTPLATE));
        }

        if (this.age == 80)
        {
            equipStack(EquipmentSlot.HEAD, new ItemStack(Items.NETHERITE_HELMET));
        }

        if (this.age >= 100 && isInvulnerable())
        {
            setInvulnerable(false);
            equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.NETHERITE_SWORD));
            playSound(SoundEvents.ENTITY_WITHER_SPAWN, 1.0f, 1.0f);
            setAiDisabled(false);
        }

        super.tick();
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }
}
