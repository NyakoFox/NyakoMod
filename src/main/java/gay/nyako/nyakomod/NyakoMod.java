package gay.nyako.nyakomod;

import com.google.gson.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import gay.nyako.nyakomod.access.ServerPlayerEntityAccess;
import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.command.*;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.entity.TickerEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.mixin.ScoreboardCriterionMixin;
import gay.nyako.nyakomod.screens.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static net.minecraft.server.command.CommandManager.argument;

public class NyakoMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("nyakomod");

	public static final gay.nyako.nyakomod.NyakoConfig CONFIG = gay.nyako.nyakomod.NyakoConfig.createAndLoad();

	public static final ScreenHandlerType<IconScreenHandler> ICON_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(IconScreenHandler::new);
	public static final ScreenHandlerType<CunkShopScreenHandler> CUNK_SHOP_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(CunkShopScreenHandler::new);
	public static final ScreenHandlerType<BlueprintWorkbenchScreenHandler> BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE = new ScreenHandlerType<>(BlueprintWorkbenchScreenHandler::new);
	public static final ScreenHandlerType<NBPScreenHandler> NBP_SCREEN_HANDLER_TYPE = new ExtendedScreenHandlerType<>(NBPScreenHandler::new);

	public static final IntProperty COINS_PROPERTY = IntProperty.of("coins", 1, SingleCoinBlock.MAX_COINS);

	private static SlimeSkyManager SLIME_SKY_MANAGER;

	// Drip
	public static final ArmorMaterial customArmorMaterial = new CustomArmorMaterial();

	public static final BlockEntityType<BlueprintWorkbenchBlockEntity> BLUEPRINT_WORKBENCH_ENTITY = FabricBlockEntityTypeBuilder.create(BlueprintWorkbenchBlockEntity::new, NyakoModBlock.BLUEPRINT_WORKBENCH).build(null);
	public static final BlockEntityType<NoteBlockPlusBlockEntity> NOTE_BLOCK_PLUS_ENTITY = FabricBlockEntityTypeBuilder.create(NoteBlockPlusBlockEntity::new, NyakoModBlock.NOTE_BLOCK_PLUS).build(null);

	public static final ScoreboardCriterion COIN_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:coins");

	public static final EntityType<TickerEntity> TICKER = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("nyakomod", "ticker"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, TickerEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build()
	);

	public static Enchantment CUNKLESS_CURSE_ENCHANTMENT = Registry.register(
			Registry.ENCHANTMENT,
			new Identifier("nyakomod", "cunkless_curse"),
			new CunkCurseEnchantment()
	);

	// Entities
	public static final EntityType<PetSpriteEntity> PET_SPRITE = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("nyakomod", "petsprite"),
			FabricEntityTypeBuilder
					.create(SpawnGroup.MISC, PetSpriteEntity::new)
					.dimensions(EntityDimensions.changing(0.1f, 1.8f))
					.trackRangeBlocks(10)
					.build()
	);

	public static final EntityType<PetDragonEntity> PET_DRAGON = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("nyakomod", "petdragon"),
			FabricEntityTypeBuilder
					.create(SpawnGroup.MISC, PetDragonEntity::new)
					.dimensions(EntityDimensions.changing(0.6f, 1f))
					.trackRangeBlocks(10)
					.build()
	);


	@Environment(EnvType.SERVER)
	public static CachedResourcePack CACHED_RESOURCE_PACK = new CachedResourcePack();

	@Environment(EnvType.SERVER)
	public static ModelManager MODEL_MANAGER = new ModelManager();

	public static void openShop(PlayerEntity player, World world, Identifier shop) {
		player.openHandledScreen(new ExtendedScreenHandlerFactory() {
			@Override
			public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
				buf.writeIdentifier(shop);
				var string = ShopEntries.savedJson.get(shop).toString();
				var length = string.getBytes(StandardCharsets.UTF_8).length;
				buf.writeInt(length);
				buf.writeString(string, length);
			}

			@Override
			public Text getDisplayName() {
				return Text.literal("Icon Selector");
			}

			@Override
			public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
				return new CunkShopScreenHandler(syncId, inv, ScreenHandlerContext.create(world, player.getBlockPos()));
			}
		});
	}

	public static void storeShopModelJson(JsonObject shopJson, Identifier shop) {
		ShopEntries.savedJson.put(shop, shopJson);
	}

	public static void loadShopModelFromJson(JsonObject shopJson, ShopData shopData) {
		shopJson.getAsJsonArray("entries").forEach(entry -> {
			JsonObject entryJson = entry.getAsJsonObject();
			var stacks = new ArrayList<ItemStack>();
			entryJson.getAsJsonArray("items").forEach(item -> {
				var jsonObject = item.getAsJsonObject();
				if (jsonObject.has("nbt")) {
					var snbt = jsonObject.get("nbt").getAsString();
					var stack = new ItemStack(Registry.ITEM.get(new Identifier(jsonObject.get("id").getAsString())));
					if (jsonObject.has("count")) {
						stack.setCount(jsonObject.get("count").getAsInt());
					} else if (jsonObject.has("Count")) {
						stack.setCount(jsonObject.get("Count").getAsInt());
					}
					try {
						stack.setNbt(NbtHelper.fromNbtProviderString(snbt));
					} catch (CommandSyntaxException e) {
						e.printStackTrace();
					}
					stacks.add(stack);
				} else {
					// Convert Name from json to a string
					// Lore needs something like this as well
					/*if ((jsonObject.get("tag") != null) &&
							(jsonObject.get("tag").getAsJsonObject().get("display") != null) &&
							(jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject().get("Name") != null)) {
						var display = jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject();
						var string = gson.toJson(display.get("Name"));
						display.addProperty("Name",  string);
					}*/
					NbtCompound converted = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonObject);
					stacks.add(ItemStack.fromNbt(converted));
				}
			});
			shopData.add(
					new ShopEntry(
							stacks,
							entryJson.get("price").getAsInt(),
							Text.of(entryJson.get("name").getAsString()),
							Text.of(entryJson.get("description").getAsString())
					)
			);
		});

		ShopEntries.register(shopData);
	}

	@Override
	public void onInitialize() {
		NyakoModLoot.register();

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier("nyakomod", "shops");
			}

			@Override
			public void reload(ResourceManager manager) {
				ShopEntries.shops.clear();

				manager.findResources("shops", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
					try {
						var shopId = new Identifier(
								resourceId.getNamespace(),
								resourceId.getPath().substring(6, resourceId.getPath().length() - 5)
						);

						var shopData = new ShopData(shopId);

						// Use GSon to parse the JSON file into a JsonObject
						JsonObject shopJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
						loadShopModelFromJson(shopJson, shopData);
						storeShopModelJson(shopJson, shopId);
					} catch (Exception e) {
						NyakoMod.LOGGER.error("Error occurred while loading resource json " + resourceId, e);
					}
				});
			}
		});

		NyakoModNetworking.registerGlobalReceivers();
		NyakoModDisc.registerAll();

		Registry.register(Registry.SCREEN_HANDLER, new Identifier("nyakomod", "cunk_shop"), NyakoMod.CUNK_SHOP_SCREEN_HANDLER_TYPE);
		Registry.register(Registry.SCREEN_HANDLER, new Identifier("nyakomod", "icon_menu"), NyakoMod.ICON_SCREEN_HANDLER_TYPE);
		Registry.register(Registry.SCREEN_HANDLER, new Identifier("nyakomod", "blueprint_workbench"), NyakoMod.BLUEPRINT_WORKBENCH_SCREEN_HANDLER_TYPE);
		Registry.register(Registry.SCREEN_HANDLER, new Identifier("nyakomod", "note_block_plus"), NyakoMod.NBP_SCREEN_HANDLER_TYPE);

		var blueprintWorkbenchId = new Identifier("nyakomod", "blueprint_workbench");
		Registry.register(Registry.BLOCK_ENTITY_TYPE, blueprintWorkbenchId, BLUEPRINT_WORKBENCH_ENTITY);
		Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier("nyakomod", "note_block_plus"), NOTE_BLOCK_PLUS_ENTITY);
		
		InstrumentRegistry.register();

		NyakoModGacha.register();

		FabricDefaultAttributeRegistry.register(PET_SPRITE, PetSpriteEntity.createPetAttributes());
		FabricDefaultAttributeRegistry.register(PET_DRAGON, PetDragonEntity.createPetAttributes());


		DispenserBlock.registerBehavior(NyakoModItem.SOUL_JAR, new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos pos = pointer.getPos();
				World world = pointer.getWorld();
				if (!stack.getOrCreateNbt().contains("entity")) {
					BlockPos blockPos = pointer.getPos().offset(direction);
					List<Entity> list = pointer.getWorld().getEntitiesByClass(
							Entity.class,
							new Box(blockPos), Entity::isAlive
					);

					Iterator<Entity> var5 = list.iterator();

					Entity entity;
					ItemStack newStack;
					do {
						if (!var5.hasNext()) {
							return super.dispenseSilently(pointer, stack);
						}

						entity = var5.next();
						newStack = ((SoulJarItem) stack.getItem()).captureEntity(stack, null, (LivingEntity) entity);
					} while (newStack == null);

					return newStack;
				}
				((SoulJarItem) stack.getItem()).spawnEntity(pos, direction, world, stack);
				return stack;
			}
		});

		var BagDispenseBehavior = new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				Position pos = DispenserBlock.getOutputLocation(pointer);
				World world = pointer.getWorld();

				var nbt = stack.getOrCreateNbt();

				dispenseCoins(nbt, "copper", NyakoModItem.COPPER_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "gold", NyakoModItem.GOLD_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "emerald", NyakoModItem.EMERALD_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "diamond", NyakoModItem.DIAMOND_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "netherite", NyakoModItem.NETHERITE_COIN_ITEM, direction, pos, world);

				stack.setNbt(nbt);

				return stack;
			}
		};

		DispenserBlock.registerBehavior(NyakoModItem.BAG_OF_COINS_ITEM, BagDispenseBehavior);
		DispenserBlock.registerBehavior(NyakoModItem.HUNGRY_BAG_OF_COINS_ITEM, BagDispenseBehavior);

		CunkCoinUtils.registerCoinAmounts();
		registerCommands();

		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			CachedResourcePack.setPlayerResourcePack(handler.player);
			((ServerPlayerEntityAccess)handler.player).setSafeMode(true);
		}));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SLIME_SKY_MANAGER = SlimeSkyManager.forWorld(server.getWorld(World.OVERWORLD));
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			for (ServerPlayerEntity player : world.getPlayers()) {
				if (((ServerPlayerEntityAccess)player).isInSafeMode()) {
					if (!((ServerPlayerEntityAccess)player).getJoinPos().equals(player.getPos())) {
						((ServerPlayerEntityAccess)player).setSafeMode(false);
					}
				}
			}

			if (world.getRegistryKey() == World.OVERWORLD) {
				if (SLIME_SKY_MANAGER == null) return;

				SLIME_SKY_MANAGER.tick();
			}
		});
	}

	public void dispenseCoins(NbtCompound nbt, String tag, Item type, Direction direction, Position pos, World world) {
		if (nbt.contains(tag)) {
			int coins = nbt.getInt(tag);
			if (coins > 0) {
				var stack = new ItemStack(type);
				stack.setCount(coins);
				nbt.remove(tag);

				ItemDispenserBehavior.spawnItem(world, stack, 6, direction, pos);
			}
		}
	}

	public static String hash(String input) {
		try {
			var digest = MessageDigest.getInstance("SHA-256");
			digest.update(input.getBytes(StandardCharsets.UTF_8));
			var hash = toHexString(digest.digest());
			return hash.substring(0, 10);
		} catch (NoSuchAlgorithmException ex) {
			return "default";
		}
	}

	public static String hashString(String input) {
		return "custom/" + hash(input);
	}

	private static String toHexString(byte[] bytes) {
		Formatter result = new Formatter();
		try (result) {
			for (var b : bytes) {
				result.format("%02x", b & 0xff);
			}
			return result.toString();
		}
	}



	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			BackCommand.register(dispatcher);
			XpCommand.register(dispatcher);
			LoreCommand.register(dispatcher);
			RenameCommand.register(dispatcher);
			PackCommand.register(dispatcher);

			// "Loose" commands
			// TODO: move these to their own files
			dispatcher.register(CommandManager.literal("smite")
					.requires(source -> source.hasPermissionLevel(2))
					.executes(context -> {
						ServerPlayerEntity player = context.getSource().getPlayer();
						if (player == null) {
							return 0;
						}

						var smiteDistance = 128;

						Vec3d pos = player.getCameraPosVec(0.0F);
						Vec3d ray = pos.add(player.getRotationVector().multiply(smiteDistance));

						EntityHitResult entityHitResult = net.minecraft.entity.projectile.ProjectileUtil.getEntityCollision(player.world, player, pos, ray, player.getBoundingBox().expand(smiteDistance), entity -> true);

						if (entityHitResult != null && entityHitResult.getType() == HitResult.Type.ENTITY) {
							Entity entity = entityHitResult.getEntity();
							entity.damage(DamageSource.LIGHTNING_BOLT, 5);
							var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, player.world);
							lightning.setPosition(entity.getPos());
							player.world.spawnEntity(lightning);
							lightning.setCosmetic(true);
							return 1;
						}

						var result = player.raycast(smiteDistance, 0, false);
						if (result.getType() == HitResult.Type.BLOCK) {
							BlockPos blockPos = ((BlockHitResult) result).getBlockPos();
							var lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, player.world);
							lightning.setPosition(blockPos.getX(), blockPos.getY(), blockPos.getZ());
							player.world.spawnEntity(lightning);
						}
						return 1;
					})
			);
			dispatcher.register(CommandManager.literal("icons")
					.requires(source -> source.hasPermissionLevel(2))
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ServerWorld world = source.getWorld();

						player.openHandledScreen(new ExtendedScreenHandlerFactory() {
							@Override
							public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
								var json = MODEL_MANAGER.getManifest().toString();
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
			dispatcher.register(CommandManager.literal("slimedebug")
					.requires(source -> source.hasPermissionLevel(2))
					.then(argument("ticks", IntegerArgumentType.integer()).executes(context -> {
						SLIME_SKY_MANAGER.stateLength = IntegerArgumentType.getInteger(context, "ticks");
						return 0;
					}))
			);
			dispatcher.register(CommandManager.literal("shop")
					.requires(source -> source.hasPermissionLevel(2))
					.then(argument("name", StringArgumentType.greedyString()).executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ServerWorld world = source.getWorld();

						openShop(player, world, new Identifier("nyakomod", context.getArgument("name", String.class)));
						return 0;
					}))
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ServerWorld world = source.getWorld();

						openShop(player, world, ShopEntries.MAIN);

						return 0;
					})
			);
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
		});
	}
}