package gay.nyako.nyakomod.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.screens.PetSpriteScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PetChangeSummonItem<T extends PetEntity> extends PetSummonItem<T> {
    public static class PetVariation {
        public Text name;
        public Identifier texture;

        public PetVariation(Text name, String texture) {
            this.name = name;
            this.texture = new Identifier("nyakomod", "textures/entity/pet/" + texture + ".png");
        }
    }

    public List<PetVariation> variations;

    public PetChangeSummonItem(Settings settings, EntityType<T> entityType, CreatePet createPet) {
        super(settings, entityType, createPet);

        variations = new ArrayList<>();
    }

    public PetChangeSummonItem<T> addVariation(Text name, String texture) {
        variations.add(new PetVariation(name, texture));

        return this;
    }

    @Override
    public TypedActionResult<ItemStack> performAction(World world, PlayerEntity player, Hand hand) {
        var stack = player.getStackInHand(hand);
        var nbt = stack.getOrCreateNbt();

        if (nbt.contains("variation")) {
            var index = nbt.getInt("variation") + 1;
            if (index >= variations.size()) {
                index = 0;
            }
            nbt.putInt("variation", index);
        } else {
            nbt.putInt("variation", 1);
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    public PetVariation getVariation(ItemStack stack) {
        var nbt = stack.getOrCreateNbt();
        if (nbt.contains("variation")) {
            var index = nbt.getInt("variation");
            if (index < variations.size()) {
                return variations.get(index);
            }
        }

        return variations.get(0);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound tag = itemStack.getOrCreateNbt();
        PetVariation variation = variations.get(0);

        if (tag.contains("variation")) {
            var index = tag.getInt("variation");
            if (index < variations.size()) {
                variation = variations.get(index);
            }
        }

        tooltip.add(Text.translatable("item.nyakomod.pet_change_summon.tooltip.variation", variation.name));
    }

    @Override
    public boolean canUse(World world, PlayerEntity player, Hand hand) {
        return player.isSneaking();
    }
}