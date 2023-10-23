package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.screens.PetSpriteScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.List;

public class PetSpriteSummonItem extends PetSummonItem<PetSpriteEntity> {
    public PetSpriteSummonItem(Settings settings) {
        super(settings, NyakoEntities.PET_SPRITE, PetSpriteEntity::createPet);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public TypedActionResult<ItemStack> performAction(World world, PlayerEntity player, Hand hand) {
        if (world.isClient()) {
            MinecraftClient.getInstance().setScreen(new PetSpriteScreen(player.getStackInHand(hand)));
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public boolean canUse(World world, PlayerEntity player, Hand hand) {
        return player.isSneaking();
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound tag = itemStack.getOrCreateNbt();
        if (tag.contains("custom_sprite")) {
            var url = tag.getString("custom_sprite");

            tooltip.add(Text.translatable("item.nyakomod.pet_sprite_summon.tooltip", url));
        }

        if (tag.contains("pet_size")) {
            tooltip.add(Text.translatable("item.nyakomod.pet_sprite_summon.tooltip.size", tag.getDouble("pet_size")));
        }

        if (tag.contains("still")) {
            tooltip.add(Text.translatable("item.nyakomod.pet_sprite_summon.tooltip.still"));
        }
    }
}
