package gay.nyako.nyakomod.utils;

import gay.nyako.nyakomod.NyakoNetworking;
import gay.nyako.nyakomod.inventory.ShulkerBoxInventory;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;

public class InventoryUtils {
    public static void openEnderChest(Slot slot, PlayerEntity player) {
        EnderChestInventory enderChestInventory = player.getEnderChestInventory();
        var pos = player.getPos();
        var world = player.getWorld();

        world.playSound(player, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_ENDER_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

        if (world.isClient) {
            sendPacket(slot);
            return;
        }

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player2) -> GenericContainerScreenHandler.createGeneric9x3(syncId, inventory, enderChestInventory), Text.translatable("container.enderchest")));
        player.incrementStat(Stats.OPEN_ENDERCHEST);
    }

    public static void openShulkerBox(Slot slot, PlayerEntity player) {
        // ShulkerBoxScreenHandler
        var pos = player.getPos();
        var world = player.getWorld();

        if (world.isClient) {
            sendPacket(slot);
            return;
        }

        world.playSound(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 0.5f, world.random.nextFloat() * 0.1f + 0.9f);

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, inventory, player2) ->
                new ShulkerBoxScreenHandler(syncId, inventory, new ShulkerBoxInventory(slot.getStack())), Text.translatable("container.shulkerBox")));
        player.incrementStat(Stats.OPEN_SHULKER_BOX);
    }

    static void sendPacket(Slot slot) {
        PacketByteBuf passedData = NyakoNetworking.getBuf();
        var index = slot.id;
        passedData.writeInt(index);
        ClientPlayNetworking.send(NyakoNetworking.RIGHT_CLICK_INVENTORY, passedData);
    }
}
