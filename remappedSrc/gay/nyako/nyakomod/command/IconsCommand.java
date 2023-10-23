package gay.nyako.nyakomod.command;

import I;
import com.mojang.brigadier.CommandDispatcher;
import gay.nyako.nyakomod.NyakoMod;
import gay.nyako.nyakomod.screens.IconScreenHandler;
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

public final class IconsCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("icons")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(context -> {
                    ServerCommandSource source = context.getSource();
                    PlayerEntity player = source.getPlayerOrThrow();
                    ServerWorld world = source.getWorld();

                    player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                        @Override
                        public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                            var json = NyakoMod.MODEL_MANAGER.getManifest().toString();
                            var length = json.getBytes(StandardCharsets.UTF_8).length;
                            buf.writeInt(length);
                            buf.writeString(json, length);
                        }

                        @Override
                        public Text getDisplayName() {
                            return Text.literal("Drafting Table");
                        }

                        @Override
                        public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                            return new IconScreenHandler(syncId, inv, ScreenHandlerContext.create(world, player.getBlockPos()));
                        }
                    });

                    return 0;
                })
        );
    }
}
