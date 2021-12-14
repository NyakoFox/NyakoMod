package gay.nyako.nyakomod;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class NyakoClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		KeyBinding killBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.nyakomod.killbind", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_G, // The keycode of the key
				"category.nyakomod.binds" // The translation key of the keybinding's category.
		));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (killBinding.wasPressed()) {
				PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
				ClientPlayNetworking.send(NyakoMod.KILL_PLAYER_PACKET_ID, passedData);
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NyakoMod.PLAYER_SMITE_PACKET_ID, (client, handler, buf, responseSender) -> {
			client.execute(() -> {
				client.player.addVelocity(0D, 5D, 0D);
			});
		});

		EntityRendererRegistry.register(NyakoMod.TICKER, TickerEntityRenderer::new);

		FabricModelPredicateProviderRegistry.register(new Identifier("nyakomod", "has_entity"), (stack, world, entity, i) ->
		{
			if (stack.getOrCreateNbt().contains("entity")) {
				return 1;
			}

			return 0;
		});

		ClientPlayNetworking.registerGlobalReceiver(NyakoMod.IMAGE_DOWNLOAD_PACKET_ID, (client, handler, buf, responseSender) -> {
			String url = buf.readString();
			Identifier identifier = buf.readIdentifier();
			System.out.println("Received texture client-side " + identifier + " " + url);

			client.execute(() -> {
				// Run code in the client thread
				NyakoMod.downloadSprite(url, identifier);
			});
		});
	}
}
