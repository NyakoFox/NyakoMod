package gay.nyako.nyakomod.command;

import com.mojang.brigadier.CommandDispatcher;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.NyakoNetworking;
import gay.nyako.nyakomod.screens.IconScreenHandler;
import gay.nyako.nyakomod.screens.ModelScreen;
import gay.nyako.nyakomod.utils.ChatPrefixes;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public final class ModelsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("models")
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    ServerPlayerEntity player = source.getPlayerOrThrow();
                    ServerWorld world = source.getWorld();

                    if (!NyakoMod.CONFIG.resourcePackEnabled())
                    {
                        context.getSource().sendError(ChatPrefixes.ERROR.apply("<white>The pack feature is disabled in the config.</white>"));
                        return 0;
                    }

                    PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
                    ServerPlayNetworking.send(player, NyakoNetworking.OPEN_MODELS, passedData);

                    return 0;
                })
        );
    }
}