package gay.nyako.nyakomod.command;

import com.google.gson.JsonObject;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class DumpJsonCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("dumpjson")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayerOrThrow();

                    // Get stack in hand
                    ItemStack stack = player.getMainHandStack();
                    if (stack.isEmpty()) {
                        return 0;
                    }
                    // Get the NBT
                    NbtCompound tag = stack.getOrCreateNbt();
                    JsonObject converted = (JsonObject) Dynamic.convert(NbtOps.INSTANCE, JsonOps.INSTANCE, tag);
                    System.out.println(converted.toString());
                    player.sendMessage(Text.literal("Dumped to console!"));
                    return 0;
                })
        );
    }
}
