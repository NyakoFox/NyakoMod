package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public final class DumpNbtCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("dumpnbt")
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
                    System.out.println(tag.toString());
                    player.sendMessage(Text.literal("Dumped to console!"));
                    return 0;
                })
        );
    }
}
