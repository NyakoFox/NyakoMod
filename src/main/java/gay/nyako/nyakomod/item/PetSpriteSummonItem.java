package gay.nyako.nyakomod.item;

import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.screens.PetSpriteScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import java.util.List;

public class PetSpriteSummonItem extends TrinketItem {
    public PetSpriteSummonItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onEquip(ItemStack stack, SlotReference slot, LivingEntity entity) {
        super.onEquip(stack, slot, entity);
    }

    @Override
    public void tick(ItemStack stack, SlotReference slot, LivingEntity entity) {
        if (!entity.world.isClient()) {
            var entities = entity.world.getEntitiesByType(NyakoMod.PET_SPRITE,
                    new Box(entity.getX() - 200, entity.getY() - 100, entity.getZ() - 100, entity.getX() + 100, entity.getY() + 100, entity.getZ() + 100),
                    (e) -> e.getOwnerUuid() != null && e.getOwnerUuid().equals(entity.getUuid()));

            if (entities.size() == 0) {
                var pet = new PetSpriteEntity(NyakoMod.PET_SPRITE, entity.world);
                pet.setOwnerUuid(entity.getUuid());
                pet.setPosition(entity.getX(), entity.getY(), entity.getZ());
                pet.setInvulnerable(true);
                var nbt = stack.getOrCreateNbt();
                if (nbt.contains("custom_sprite")) {
                    pet.setCustomSprite(nbt.getString("custom_sprite"));
                }
                entity.world.spawnEntity(pet);
            } else if (entities.size() > 1) {
                for (var e : entities.subList(1, entities.size())) {
                    e.remove(Entity.RemovalReason.DISCARDED);
                }
            }
        }
        super.tick(stack, slot, entity);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (player.isSneaking()) {
            if (player.world.isClient()) {
                MinecraftClient.getInstance().setScreen(new PetSpriteScreen());
            }
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {
        NbtCompound tag = itemStack.getOrCreateNbt();
        if (tag.contains("custom_sprite")) {
            var url = tag.getString("custom_sprite");

            tooltip.add(Text.translatable("item.nyakomod.pet_sprite_summon.tooltip", url));
        }
    }
}
