package gay.nyako.nyakomod;

import gay.nyako.nyakomod.entity.renderer.*;
import gay.nyako.nyakomod.entity.model.PetDragonModel;
import gay.nyako.nyakomod.screens.*;
import gay.nyako.nyakomod.utils.NyakoUtils;
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
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
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
	public static final EntityModelLayer MODEL_MONITOR_LAYER = new EntityModelLayer(new Identifier("nyakomod", "monitor"), "main");
	public static final EntityModelLayer MODEL_HEROBRINE_LAYER = new EntityModelLayer(new Identifier("nyakomod", "herobrine"), "main");
	public static final EntityModelLayer MODEL_HEROBRINE_INNER_ARMOR_LAYER = new EntityModelLayer(new Identifier("nyakomod", "herobrine"), "inner_armor");
	public static final EntityModelLayer MODEL_HEROBRINE_OUTER_ARMOR_LAYER = new EntityModelLayer(new Identifier("nyakomod", "herobrine"), "outer_armor");

	@Override
	public void onInitializeClient() {
		EntityRendererRegistry.register(NyakoEntities.PET_SPRITE, PetSpriteRenderer::new);

		KeyBinding killBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.nyakomod.killbind", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_G, // The keycode of the key
				"category.nyakomod.binds" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (killBinding.wasPressed()) {
				PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
				ClientPlayNetworking.send(NyakoNetworking.KILL_PLAYER, passedData);
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NyakoNetworking.PLAYER_SMITE,
				(client, handler, buffer, sender) -> client.execute(() -> {
					client.player.addVelocity(0D, 5D, 0D);
				}));

		EntityRendererRegistry.register(NyakoEntities.TICKER, TickerEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.PET_DRAGON, PetDragonRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_DRAGON_LAYER, PetDragonModel::getTexturedModelData);
		EntityRendererRegistry.register(NyakoEntities.MONITOR, MonitorEntityRenderer::new);
		EntityModelLayerRegistry.registerModelLayer(MODEL_MONITOR_LAYER, MonitorEntityRenderer::getTexturedModelData);
		EntityRendererRegistry.register(NyakoEntities.NETHER_PORTAL, NetherPortalProjetileEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.HEROBRINE, HerobrineEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_LAYER, HerobrineEntityRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_INNER_ARMOR_LAYER, HerobrineEntityRenderer::getInnerArmorModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_HEROBRINE_OUTER_ARMOR_LAYER, HerobrineEntityRenderer::getOuterArmorModelData);


		FabricModelPredicateProviderRegistry.register(new Identifier("nyakomod", "has_entity"), (stack, world, entity, i) ->
		{
			if (stack.getOrCreateNbt().contains("entity")) {
				return 1;
			}

			return 0;
		});

		FabricModelPredicateProviderRegistry.register(new Identifier("nyakomod", "has_blueprint"), (stack, world, entity, i) ->
		{
			if (stack.getOrCreateNbt().contains("blueprint")) {
				return 1;
			}

			return 0;
		});

		ModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("portal"), (stack, world, entity, seed) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.hasProjectile(stack, NyakoItems.NETHER_PORTAL_STRUCTURE) ? 1.0f : 0.0f);
		ModelPredicateProviderRegistry.register(NyakoItems.ENCUMBERING_STONE, new Identifier("nyakomod", "locked"), (stack, world, entity, seed) -> {
			var nbt = stack.getNbt();
			if (nbt != null && nbt.contains("locked") && !nbt.getBoolean("locked")) {
				return 1;
			}

			return 0;
		});

		FabricModelPredicateProviderRegistry.register(new Identifier("nyakomod", "variation"), (stack, world, entity, i) ->
		{
			var nbt = stack.getOrCreateNbt();
			if (nbt.contains("variation")) {
				return (float)nbt.getInt("variation") / 100f;
			}

			return 0;
		});

		HandledScreens.register(NyakoScreenHandlers.ICON_SCREEN_HANDLER_TYPE, IconScreen::new);
		HandledScreens.register(NyakoScreenHandlers.CUNK_SHOP_SCREEN_HANDLER_TYPE, CunkShopHandledScreen::new);
		HandledScreens.register(NyakoScreenHandlers.BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE, BlueprintWorkbenchScreen::new);
		HandledScreens.register(NyakoScreenHandlers.PRESENT_WRAPPER_SCREEN_HANDLER_TYPE, PresentWrapperScreen::new);
		HandledScreens.register(NyakoScreenHandlers.NBP_SCREEN_HANDLER_TYPE, NBPHandledScreen::new);

    	BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.COPPER_SINGLE_COIN,    RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.GOLD_SINGLE_COIN,      RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.EMERALD_SINGLE_COIN,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.DIAMOND_SINGLE_COIN,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.NETHERITE_SINGLE_COIN, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.DRAFTING_TABLE, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_PORTAL, RenderLayer.getTranslucent());

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("models").executes(context -> {
				var client = context.getSource().getClient();
				client.send(() -> client.setScreen(new ModelScreen()));
				return 1;
			}));
		});

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 4159204, NyakoItems.WATER);

		NyakoParticleTypes.registerClient();
	}

	public static final List<String> downloadedUrls = new ArrayList<>();

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
		var hash = NyakoUtils.hashString(urlPath);
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
