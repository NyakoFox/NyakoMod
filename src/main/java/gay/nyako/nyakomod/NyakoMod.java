package gay.nyako.nyakomod;

import com.google.gson.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.command.*;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.entity.TickerEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.GachaItem;
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
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
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
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

	public static final IntProperty COINS_PROPERTY = IntProperty.of("coins", 1, SingleCoinBlock.MAX_COINS);

	private static SlimeSkyManager SLIME_SKY_MANAGER;

	// Spunch block
	public static final Identifier SPUNCH_BLOCK_SOUND = new Identifier("nyakomod:vine_boom");
	public static SoundEvent SPUNCH_BLOCK_SOUND_EVENT = new SoundEvent(SPUNCH_BLOCK_SOUND);
	public static final BlockSoundGroup SPUNCH_BLOCK_SOUND_GROUP = new BlockSoundGroup(1.0f, 1.2f, SPUNCH_BLOCK_SOUND_EVENT, SoundEvents.BLOCK_STONE_STEP, SPUNCH_BLOCK_SOUND_EVENT, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	// Drip
	public static final ArmorMaterial customArmorMaterial = new CustomArmorMaterial();

	public static final BlockEntityType<BlueprintWorkbenchBlockEntity> BLUEPRINT_WORKBENCH_ENTITY = FabricBlockEntityTypeBuilder.create(BlueprintWorkbenchBlockEntity::new, NyakoModBlock.BLUEPRINT_WORKBENCH).build(null);

	public static final Identifier COIN_COLLECT_SOUND = new Identifier("nyakomod:coin_collect");
	public static SoundEvent COIN_COLLECT_SOUND_EVENT = new SoundEvent(COIN_COLLECT_SOUND);

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

	public static final Identifier WOLVES_SOUND = new Identifier("nyakomod:wolves");
	public static SoundEvent WOLVES_SOUND_EVENT = new SoundEvent(WOLVES_SOUND);

	// Gacha-related stuff starts here


	// Gacha items

	public record GachaEntry(
			Text name,
			ItemStack itemStack,
			int rarity,
			double weight
	) {}

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


	public static List<GachaEntry> gachaEntryList = new ArrayList<>();

	public static final Identifier DISCORD_SOUND = new Identifier("nyakomod:discord");
	public static SoundEvent DISCORD_SOUND_EVENT = new SoundEvent(DISCORD_SOUND);

	@Environment(EnvType.SERVER)
	public static CachedResourcePack CACHED_RESOURCE_PACK = new CachedResourcePack();

	@Environment(EnvType.SERVER)
	public static ModelManager MODEL_MANAGER = new ModelManager();

	public void registerGachaItems() {
		// Sounds
		Registry.register(Registry.SOUND_EVENT, DISCORD_SOUND, DISCORD_SOUND_EVENT);

		/* 1 STAR */
		// 1 Gold CunkCoin
		registerGachaItem(Text.of("1 §6Gold CunkCoin™"), new ItemStack(NyakoModItem.GOLD_COIN_ITEM), 1);
		registerGachaItem(Text.of("16 §6Dirt"), Items.DIRT, 16, 1);
		registerGachaItem(Text.of("8 §6Oak Logs"), Items.OAK_LOG, 8, 1);
		registerGachaItem(Text.of("8 §6Dark Oak Logs"), Items.DARK_OAK_LOG, 8, 1);
		registerGachaItem(Text.of("8 §6Spruce Logs"), Items.SPRUCE_LOG, 8, 1);
		registerGachaItem(Text.of("8 §6Acacia Logs"), Items.ACACIA_LOG, 8, 1);
		registerGachaItem(Text.of("8 §6Birch Logs"), Items.BIRCH_LOG, 8, 1);
		registerGachaItem(Text.of("8 §6Jungle Logs"), Items.JUNGLE_LOG, 8, 1);
		registerGachaItem(Text.of("8 §cCrimson Fungi"), Items.CRIMSON_FUNGUS, 8, 1);
		registerGachaItem(Text.of("8 §bWarped Fungi"), Items.WARPED_FUNGUS, 8, 1);

		// Bow
		registerGachaItem(Text.of("a §7Bow"), Items.BOW, 1, 1);

		/* 2 STAR */
		registerGachaItem(Text.of("an §5Uncraftable Potion...?"), new ItemStack(Items.POTION), 2);
		// wolves
		registerGachaItem(Text.of("a §bMusic Disc"), new ItemStack(NyakoModDisc.get("wolves").discItem), 2);
		registerGachaItem(Text.of("16 §bSquishy Diamonds"), (GachaItem) NyakoModItem.DIAMOND_GACHA_ITEM, 16);
		registerGachaItem(Text.of("32 §6Cookies"), Items.COOKIE, 32, 2);

		registerGachaItem(Text.of("32 §7Sticks"), Items.STICK, 32, 2);
		registerGachaItem(Text.of("a §7Fishing Rod"), Items.FISHING_ROD, 1, 2);
		registerGachaItem(Text.of("2 §dCow Spawn Eggs"), Items.COW_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dPig Spawn Eggs"), Items.PIG_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dSheep Spawn Eggs"), Items.SHEEP_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dRabbit Spawn Eggs"), Items.RABBIT_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dChicken Spawn Eggs"), Items.CHICKEN_SPAWN_EGG, 2, 2);

		/* 3 STAR */
		registerGachaItem(Text.of("the §9Discord Logo"), (GachaItem) NyakoModItem.DISCORD_GACHA_ITEM);
		registerGachaItem(Text.of("a §2Potion of Luck"), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LUCK), 3);
		registerGachaItem(Text.of("a §2Potion of Unluck"), PotionUtil.setPotion(new ItemStack(Items.POTION), NyakoModPotion.UNLUCK), 3);
		registerGachaItem(Text.of("5 §6Gold CunkCoin™"), NyakoModItem.GOLD_COIN_ITEM, 5, 3);
		registerGachaItem(Text.of("64 §7Cobblestone"), Items.COBBLESTONE, 64, 3);
		registerGachaItem(Text.of("64 §cTorches"), Items.TORCH, 64, 3);
		registerGachaItem(Text.of("16 §7Iron Ingots"), Items.IRON_INGOT, 16, 3);
		registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 3);
		registerGachaItem(Text.of("16 §dItem Frames"), Items.ITEM_FRAME, 16, 3);
		registerGachaItem(Text.of("16 §7Arrows"), Items.ARROW, 16, 3);

		// Mario and Luigi
		ItemStack brotherStack = new ItemStack(NyakoModItem.PRESENT_ITEM);
		PresentItem.addToPresent(brotherStack, new ItemStack(NyakoModItem.MARIO_GACHA_ITEM));
		PresentItem.addToPresent(brotherStack, new ItemStack(NyakoModItem.LUIGI_GACHA_ITEM));
		brotherStack.setCustomName(Text.of("Present (Brothers)"));
		registerGachaItem(Text.of("§cThe §4Brothers"), brotherStack, 3);

		/* 4 STAR */
		registerGachaItem(Text.of("10 §6Gold CunkCoin™"), NyakoModItem.GOLD_COIN_ITEM, 10, 4);
		registerGachaItem(Text.of("the §5Staff of Vorbulation"), (GachaItem) NyakoModItem.STAFF_OF_VORBULATION_ITEM);
		registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 4);
		registerGachaItem(Text.of("16 §eGlowing Item Frames"), Items.GLOW_ITEM_FRAME, 16, 4);
		registerGachaItem(Text.of("16 §eSpectral Arrows"), Items.SPECTRAL_ARROW, 16, 4);
		registerGachaItem(Text.of("32 §bGlass"), Items.GLASS, 32, 4);
		registerGachaItem(Text.of("1 §dVillager Spawn Egg"), Items.VILLAGER_SPAWN_EGG, 1, 4);
		registerGachaItem(Text.of("4 §dWandering Trader Spawn Eggs"), Items.WANDERING_TRADER_SPAWN_EGG, 4, 4);
		registerGachaItem(Text.of("16 §cTNT"), Items.TNT, 16, 4);
		registerGachaItem(Text.of("16 §5Dragon's Breath"), Items.DRAGON_BREATH, 16, 4);

		/* 5 STAR */
		registerGachaItem(Text.of("a §dDragon §5Egg"), Items.DRAGON_EGG, 1, 5);
		registerGachaItem(Text.of("20 §6Gold CunkCoin™"), NyakoModItem.GOLD_COIN_ITEM, 20, 5);
		registerGachaItem(Text.of("1 §4Ancient Debris"), Items.ANCIENT_DEBRIS, 1, 5);
		registerGachaItem(Text.of("8 §bDiamonds"), Items.DIAMOND, 8, 5);

		/*// Diamond tool kit
		ItemStack toolStack = new ItemStack(PRESENT_ITEM);
		PresentItem.addToPresent(toolStack, new ItemStack(Items.DIAMOND_PICKAXE));
		PresentItem.addToPresent(toolStack, new ItemStack(Items.DIAMOND_SWORD));
		PresentItem.addToPresent(toolStack, new ItemStack(Items.DIAMOND_AXE));
		PresentItem.addToPresent(toolStack, new ItemStack(Items.DIAMOND_SHOVEL));
		PresentItem.addToPresent(toolStack, new ItemStack(Items.DIAMOND_HOE));
		toolStack.setCustomName(Text.of("Present (Diamond Tool Kit)"));
		registerGachaItem(Text.of("a Diamond Tool Kit"), toolStack, 5);
		// Diamond armor kit
		ItemStack armorStack = new ItemStack(PRESENT_ITEM);
		PresentItem.addToPresent(armorStack, new ItemStack(Items.DIAMOND_HELMET));
		PresentItem.addToPresent(armorStack, new ItemStack(Items.DIAMOND_CHESTPLATE));
		PresentItem.addToPresent(armorStack, new ItemStack(Items.DIAMOND_LEGGINGS));
		PresentItem.addToPresent(armorStack, new ItemStack(Items.DIAMOND_BOOTS));
		armorStack.setCustomName(Text.of("Present (Diamond Armor Kit)"));
		registerGachaItem(Text.of("a Diamond Armor Kit"), armorStack, 5);*/
	}

	public void registerGachaItem(Text name, GachaItem item) {
		registerGachaItem(name, item, 1);
	}

	public void registerGachaItem(Text name, GachaItem item, int amount) {
		int rarity = item.getRarity();
		ItemStack itemStack = new ItemStack(item);
		itemStack.setCount(amount);
		registerGachaItem(name, itemStack, rarity);
	}

	public void registerGachaItem(Text name, Item item, int amount, int rarity) {
		ItemStack itemStack = new ItemStack(item);
		itemStack.setCount(amount);
		registerGachaItem(name, itemStack, rarity);
	}

	public void registerGachaItem(Text name, ItemStack itemStack, int rarity) {
		GachaEntry gachaEntry = new GachaEntry(
				name, itemStack, rarity, (1d / (rarity * 2d))
		);
		gachaEntryList.add(gachaEntry);
	}


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

		// Spunch block
		Registry.register(Registry.SOUND_EVENT, SPUNCH_BLOCK_SOUND, SPUNCH_BLOCK_SOUND_EVENT);

		var blueprintWorkbenchId = new Identifier("nyakomod", "blueprint_workbench");
		Registry.register(Registry.BLOCK_ENTITY_TYPE, blueprintWorkbenchId, BLUEPRINT_WORKBENCH_ENTITY);

		Registry.register(Registry.SOUND_EVENT, COIN_COLLECT_SOUND, COIN_COLLECT_SOUND_EVENT);

		Registry.register(Registry.SOUND_EVENT, WOLVES_SOUND, WOLVES_SOUND_EVENT);
		InstrumentRegistry.register();

		registerGachaItems();

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

		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> CachedResourcePack.setPlayerResourcePack(handler.player)));

		ServerLifecycleEvents.SERVER_STARTED.register(server -> {
			SLIME_SKY_MANAGER = SlimeSkyManager.forWorld(server.getWorld(World.OVERWORLD));
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
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