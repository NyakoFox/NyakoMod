package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import gay.nyako.nyakomod.ChatPrefix;
import gay.nyako.nyakomod.ChatPrefixes;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class AFKCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("afk")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayerOrThrow();
                    ServerPlayerEntityAccess access = (ServerPlayerEntityAccess)player;
                    if (access.isInSafeMode())
                    {
                        access.setSafeMode(false);
                        context.getSource().sendFeedback(ChatPrefixes.SUCCESS.apply("You are no longer AFK."), false);
                    }
                    else
                    {
                        access.setSafeMode(true);
                        context.getSource().sendFeedback(ChatPrefixes.SUCCESS.apply("You are now AFK."), false);
                    }

                    return 0;
                })
        );
    }
}
