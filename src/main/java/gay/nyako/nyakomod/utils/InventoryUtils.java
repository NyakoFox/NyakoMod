package gay.nyako.nyakomod.utils;

import gay.nyako.nyakomod.NyakoNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

public class InventoryUtils {
    public static void openEnderChest(ItemStack stack, PlayerEntity player) {
        EnderChestInventory enderChestInventory = player.getEnderChestInventory();
        var pos = player.getPos();
        var world = player.world;

        world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

        if (world.isClient) {
            sendPacket(stack);
            return;
        }

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player2) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.translatable("container.enderchest")));
        player.incrementStat(Stats.OPEN_ENDERCHEST);
    }

    public static void openShulkerBox(ItemStack stack, PlayerEntity player) {

    }

    static void sendPacket(ItemStack stack) {
        PacketByteBuf passedData = NyakoNetworking.getBuf();
        passedData.writeItemStack(stack);
        ClientPlayNetworking.send(NyakoNetworking.RIGHT_CLICK_INVENTORY, passedData);
    }
}
