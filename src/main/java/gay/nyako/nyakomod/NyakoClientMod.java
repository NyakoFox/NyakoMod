package gay.nyako.nyakomod;

import gay.nyako.nyakomod.access.ClientPlayerEntityAccess;
import gay.nyako.nyakomod.entity.renderer.*;
import gay.nyako.nyakomod.entity.model.PetDragonModel;
import gay.nyako.nyakomod.item.MagnetItem;
import gay.nyako.nyakomod.screens.*;
import gay.nyako.nyakomod.utils.NyakoUtils;
import gay.nyako.nyakomod.worldgen.EchoLandsDimensionEffects;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.DimensionEffects;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.HangingSignBlockEntityRenderer;
import net.minecraft.client.render.block.entity.SignBlockEntityRenderer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
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
import java.util.UUID;

public class NyakoClientMod implements ClientModInitializer {
	public static final EntityModelLayer MODEL_DRAGON_LAYER = new EntityModelLayer(NyakoMod.id("dragon"), "main");
	public static final EntityModelLayer MODEL_MONITOR_LAYER = new EntityModelLayer(NyakoMod.id("monitor"), "main");
	public static final EntityModelLayer MODEL_HEROBRINE_LAYER = new EntityModelLayer(NyakoMod.id("herobrine"), "main");
	public static final EntityModelLayer MODEL_HEROBRINE_INNER_ARMOR_LAYER = new EntityModelLayer(NyakoMod.id("herobrine"), "inner_armor");
	public static final EntityModelLayer MODEL_HEROBRINE_OUTER_ARMOR_LAYER = new EntityModelLayer(NyakoMod.id("herobrine"), "outer_armor");
	public static final EntityModelLayer MODEL_DECAYED_LAYER = new EntityModelLayer(NyakoMod.id("decayed"), "main");
	public static final EntityModelLayer MODEL_DECAYED_INNER_ARMOR_LAYER = new EntityModelLayer(NyakoMod.id("decayed"), "inner_armor");
	public static final EntityModelLayer MODEL_DECAYED_OUTER_ARMOR_LAYER = new EntityModelLayer(NyakoMod.id("decayed"), "outer_armor");

	@Environment(EnvType.CLIENT)
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

		EntityRendererRegistry.register(NyakoEntities.DECAYED, DecayedEntityRenderer::new);

		EntityModelLayerRegistry.registerModelLayer(MODEL_DECAYED_LAYER, DecayedEntityRenderer::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_DECAYED_INNER_ARMOR_LAYER, DecayedEntityRenderer::getInnerArmorModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_DECAYED_OUTER_ARMOR_LAYER, DecayedEntityRenderer::getOuterArmorModelData);

		BlockEntityRendererFactories.register(NyakoEntities.CUSTOM_SIGN_BLOCK_ENTITY, SignBlockEntityRenderer::new);
		BlockEntityRendererFactories.register(NyakoEntities.CUSTOM_HANGING_SIGN_BLOCK_ENTITY, HangingSignBlockEntityRenderer::new);

		EntityRendererRegistry.register(NyakoEntities.OBSIDIAN_ARROW, ObsidianArrowEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.FIRE_ARROW, FireArrowEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.BOMB, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.SAFETY_BOMB, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.GRENADE, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.MOSS_BOMB, FlyingItemEntityRenderer::new);
		EntityRendererRegistry.register(NyakoEntities.BOBM, FlyingItemEntityRenderer::new);

		FabricModelPredicateProviderRegistry.register(NyakoMod.id("has_entity"), (stack, world, entity, i) ->
                stack.getOrCreateNbt().contains("entity") ? 1 : 0);

		FabricModelPredicateProviderRegistry.register(NyakoMod.id("is_broken"), (stack, world, entity, i) ->
                MagnetItem.isUsable(stack) ? 0 : 1);

		FabricModelPredicateProviderRegistry.register(NyakoMod.id("has_blueprint"), (stack, world, entity, i) ->
                stack.getOrCreateNbt().contains("blueprint") ? 1 : 0);

		FabricModelPredicateProviderRegistry.register(Items.CROSSBOW, new Identifier("portal"), (stack, world, entity, seed) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.hasProjectile(stack, NyakoItems.NETHER_PORTAL_STRUCTURE) ? 1.0f : 0.0f);
		FabricModelPredicateProviderRegistry.register(NyakoItems.ENCUMBERING_STONE, NyakoMod.id("locked"), (stack, world, entity, seed) -> stack.getOrCreateNbt().contains("locked") && stack.getNbt().getBoolean("locked") ? 0 : 1);

		FabricModelPredicateProviderRegistry.register(NyakoMod.id("variation"), (stack, world, entity, i) ->
                stack.getOrCreateNbt().contains("variation") ? (float) stack.getOrCreateNbt().getInt("variation") / 100f : 0f);

		HandledScreens.register(NyakoScreenHandlers.FLETCHING_TABLE, FletchingTableScreen::new);
		HandledScreens.register(NyakoScreenHandlers.CUNK_SHOP_SCREEN_HANDLER_TYPE, CunkShopHandledScreen::new);
		HandledScreens.register(NyakoScreenHandlers.BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE, BlueprintWorkbenchScreen::new);
		HandledScreens.register(NyakoScreenHandlers.PRESENT_WRAPPER_SCREEN_HANDLER_TYPE, PresentWrapperScreen::new);
		HandledScreens.register(NyakoScreenHandlers.NBP_SCREEN_HANDLER_TYPE, NBPHandledScreen::new);
		HandledScreens.register(NyakoScreenHandlers.ICON_SCREEN_HANDLER_TYPE, IconScreen::new);

    	BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.COPPER_SINGLE_COIN,    RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.GOLD_SINGLE_COIN,      RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.EMERALD_SINGLE_COIN,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.DIAMOND_SINGLE_COIN,   RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.NETHERITE_SINGLE_COIN, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.DRAFTING_TABLE, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_PORTAL, RenderLayer.getTranslucent());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_GROWTH, RenderLayer.getCutoutMipped());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_TRAPDOOR, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.BENTHIC_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.BENTHIC_TRAPDOOR, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_ROOTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.POTTED_ECHO_ROOTS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_LEAVES, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_SPROUTBULB, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_SPROUTHEART, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.BENTHIC_LEAVES, RenderLayer.getCutout());

		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.ECHO_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.BENTHIC_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.POTTED_ECHO_SAPLING, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(NyakoBlocks.POTTED_BENTHIC_SAPLING, RenderLayer.getCutout());

		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
			dispatcher.register(ClientCommandManager.literal("models").executes(context -> {
				var client = context.getSource().getClient();
				client.send(() -> client.setScreen(new ModelScreen()));
				return 1;
			}));
		});

		ColorProviderRegistry.ITEM.register((stack, tintIndex) -> 4159204, NyakoItems.WATER);

		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if (world == null || pos == null) {
				return GrassColors.getDefaultColor();
			}
			return BiomeColors.getGrassColor(world, pos);
		}, NyakoBlocks.ECHO_GROWTH);

		ColorProviderRegistry.BLOCK.register((state, world, pos, tintIndex) -> {
			if (world == null || pos == null) {
				return GrassColors.getDefaultColor();
			}
			return BiomeColors.getFoliageColor(world, pos);
		}, NyakoBlocks.ECHO_LEAVES, NyakoBlocks.BENTHIC_LEAVES);

		DimensionEffects.BY_IDENTIFIER.put(NyakoMod.id("echolands"), new EchoLandsDimensionEffects());

		ClientTickEvents.START_CLIENT_TICK.register(StickerSystem::tick);
		HudRenderCallback.EVENT.register(StickerSystem::render);

		KeyBinding stickerBind = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.nyakomod.sticker", // The translation key of the keybinding's name
				InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
				GLFW.GLFW_KEY_H, // The keycode of the key
				"category.nyakomod.binds" // The translation key of the keybinding's category.
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (stickerBind.wasPressed()) {
				client.setScreen(new StickerScreen());
			}
		});

		ClientPlayNetworking.registerGlobalReceiver(NyakoNetworking.SEND_STICKER_TO_CLIENT,
				(client, handler, buffer, sender) -> {
					var name = buffer.readString();
					var playerName = buffer.readText();
					var uuid = buffer.readUuid();
					client.execute(() -> {
								StickerSystem.addSticker(playerName, name, uuid);
							}
					);
				}
		);

		ClientPlayNetworking.registerGlobalReceiver(NyakoNetworking.FLASHBANG,
				(client, handler, buffer, sender) -> {
					double distance = buffer.readDouble();
					client.execute(() -> {
						client.player.playSound(NyakoSoundEvents.FLASHBANG, 1.0f, 1.0f);
						((ClientPlayerEntityAccess) client.player).setFlashbangDistance((float) distance);
						((ClientPlayerEntityAccess) client.player).setFlashbangStrength(1);
					});
				}
		);
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
		var id = NyakoMod.id(hash);

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
