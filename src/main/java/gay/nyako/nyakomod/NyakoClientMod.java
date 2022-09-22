package gay.nyako.nyakomod;

import gay.nyako.nyakomod.entity.model.PetDragonModel;
import gay.nyako.nyakomod.entity.renderer.PetDragonRenderer;
import gay.nyako.nyakomod.entity.renderer.PetSpriteRenderer;
import gay.nyako.nyakomod.screens.CunkShopHandledScreen;
import gay.nyako.nyakomod.screens.CunkShopScreenHandler;
import gay.nyako.nyakomod.screens.ModelScreen;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class NyakoClientMod implements ClientModInitializer {
	public static final EntityModelLayer MODEL_DRAGON_LAYER = new EntityModelLayer(new Identifier("nyakomod", "dragon"), "main");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(NyakoMod.PET_SPRITE, PetSpriteRenderer::new);

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

		ClientPlayNetworking.registerGlobalReceiver(NyakoMod.PLAYER_SMITE_PACKET_ID,
				(client, handler, buffer, sender) -> client.execute(() -> {
					client.player.addVelocity(0D, 5D, 0D);
				}));

		EntityRendererRegistry.register(NyakoMod.TICKER, TickerEntityRenderer::new);
		EntityRendererRegistry.register(NyakoMod.PET_DRAGON, PetDragonRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_DRAGON_LAYER, PetDragonModel::getTexturedModelData);

		FabricModelPredicateProviderRegistry.register(new Identifier("nyakomod", "has_entity"), (stack, world, entity, i) ->
		{
			if (stack.getOrCreateNbt().contains("entity")) {
				return 1;
			}

			return 0;
		});

		HandledScreens.register(NyakoMod.ICON_SCREEN_HANDLER_TYPE, IconScreen::new);
		HandledScreens.register(NyakoMod.CUNK_SHOP_SCREEN_HANDLER_TYPE, CunkShopHandledScreen::new);
    
    	BlockRenderLayerMap.INSTANCE.putBlock(NyakoMod.COPPER_SINGLE_COIN_BLOCK,    RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoMod.GOLD_SINGLE_COIN_BLOCK,      RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoMod.EMERALD_SINGLE_COIN_BLOCK,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoMod.DIAMOND_SINGLE_COIN_BLOCK,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoMod.NETHERITE_SINGLE_COIN_BLOCK, RenderLayer.getCutout());

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(ClientCommandManager.literal("models").executes(context -> {
			var client = context.getSource().getClient();
			client.send(() -> client.setScreen(new ModelScreen()));
			return 1;
		})));
	}

	private static final List<String> downloadedUrls = new ArrayList<>();

	public static BufferedImage downloadImage(String urlPath) {
		BufferedImage image = null;
		URL url;

		System.out.println("downloading " + urlPath);

		try {
			url = new URL(urlPath);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "NyakoMod");
			connection.connect();
			image = ImageIO.read(connection.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (image == null) {
			// ?? we just did this server side but client side it failed so whatever
			System.out.print("failed to dl...?");
			return null;
		}

		return image;
	}

	public static Identifier downloadSprite(String urlPath) {
		var hash = NyakoMod.hashString(urlPath);
		var id = new Identifier("nyakomod", hash);

		if (downloadedUrls.contains(urlPath)) {
			return id;
		}

		downloadedUrls.add(urlPath);

		var image = downloadImage(urlPath);

		if (image == null) {
			return null;
		}

		NativeImage nativeImage;
		try {
			nativeImage = getFromBuffered(image);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);

		MinecraftClient client = MinecraftClient.getInstance();
		client.getTextureManager().registerTexture(id, nativeImageBackedTexture);
		System.out.println("finished downloading");

		return id;
	}

	public static NativeImage getFromBuffered(BufferedImage image) throws IOException {
		try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
			ImageIO.write(image, "PNG", outputStream);
			return NativeImage.read(new FastByteArrayInputStream(outputStream.array));
		}
	}
}
