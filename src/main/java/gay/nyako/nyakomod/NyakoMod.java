package gay.nyako.nyakomod;

import com.mojang.brigadier.arguments.StringArgumentType;
import eu.pb4.placeholders.TextParser;
import io.netty.buffer.Unpooled;
import it.unimi.dsi.fastutil.io.FastByteArrayInputStream;
import it.unimi.dsi.fastutil.io.FastByteArrayOutputStream;
import net.devtech.arrp.api.RRPCallback;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.blockstate.JState;
import net.devtech.arrp.json.models.JModel;
import net.devtech.arrp.json.models.JTextures;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.registry.RegistryIdRemapCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.condition.RandomChanceWithLootingLootCondition;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootingEnchantLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;

import net.minecraft.item.Items;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gay.nyako.nyakomod.command.BackCommand;
import net.minecraft.world.World;

import javax.imageio.ImageIO;

public class NyakoMod implements ModInitializer {
	public static final RuntimeResourcePack RESOURCE_PACK = RuntimeResourcePack.create("nyakomod:test");

	// Killbinding
	public static final Identifier KILL_PLAYER_PACKET_ID = new Identifier("nyakomod", "killplayer");
	// Spunch block
	public static final Identifier SPUNCH_BLOCK_SOUND = new Identifier("nyakomod:vine_boom");
	public static SoundEvent SPUNCH_BLOCK_SOUND_EVENT = new SoundEvent(SPUNCH_BLOCK_SOUND);
	public static final BlockSoundGroup SPUNCH_BLOCK_SOUND_GROUP = new BlockSoundGroup(1.0f, 1.2f, SPUNCH_BLOCK_SOUND_EVENT, SoundEvents.BLOCK_STONE_STEP, SPUNCH_BLOCK_SOUND_EVENT, SoundEvents.BLOCK_STONE_HIT, SoundEvents.BLOCK_STONE_FALL);
	public static final Block SPUNCH_BLOCK = new Block(FabricBlockSettings.copy(Blocks.STONE).sounds(SPUNCH_BLOCK_SOUND_GROUP).requiresTool());
	// Drip
	public static final ArmorMaterial customArmorMaterial = new CustomArmorMaterial();
	public static final Item DRIP_JACKET = new ArmorItem(customArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT).fireproof());
	// Launcher
	public static final Block LAUNCHER_BLOCK = new LauncherBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool());
	// Staff of Vorbulation
	public static final Item STAFF_OF_VORBULATION_ITEM = new StaffOfVorbulationItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());
	// Staff of Smiting
	public static final Item STAFF_OF_SMITING_ITEM = new StaffOfSmitingItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());

	// Coins
	public static final Item COPPER_COIN_ITEM    = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100));
	public static final Item GOLD_COIN_ITEM      = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100));
	public static final Item EMERALD_COIN_ITEM   = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100));
	public static final Item DIAMOND_COIN_ITEM   = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100));
	public static final Item NETHERITE_COIN_ITEM = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof());
	public static final Identifier COIN_COLLECT_SOUND = new Identifier("nyakomod:coin_collect");
	public static SoundEvent COIN_COLLECT_SOUND_EVENT = new SoundEvent(COIN_COLLECT_SOUND);

	public static Map<EntityType<?>, Integer> coinMap = new HashMap<>();

	// Bag of coins
	public static final Item BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Time in a bottle
	public static final TimeInABottleItem TIME_IN_A_BOTTLE = new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Soul jar
	public static final SoulJarItem SOUL_JAR = new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Player smite packet
	public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");

	// Image download packet
	public static final Identifier IMAGE_DOWNLOAD_PACKET_ID = new Identifier("nyakomod", "image_download");

	public static final EntityType<TickerEntity> TICKER = Registry.register(
			Registry.ENTITY_TYPE,
			new Identifier("nyakomod", "ticker"),
			FabricEntityTypeBuilder.create(SpawnGroup.MISC, TickerEntity::new).dimensions(EntityDimensions.fixed(0.25F, 0.25F)).build()
	);

	@Override
	public void onInitialize() {
		System.out.println("owo");

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {


			dispatcher.register(CommandManager.literal("test")
					.then(CommandManager.argument("name", StringArgumentType.string())
						.then(CommandManager.argument("url", StringArgumentType.greedyString())
								.executes(context -> {
									ServerCommandSource source = context.getSource();
									PlayerEntity player = source.getPlayer();
									ItemStack heldStack = player.getMainHandStack();
									String inputName = context.getArgument("name", String.class);
									String input = context.getArgument("url", String.class);

									BufferedImage image = null;
									URL url = null;

									try {
										url = new URL(input);
										URLConnection connection = url.openConnection();
										connection.setRequestProperty("User-Agent", "NyakoMod");
										connection.connect();
										image = ImageIO.read(connection.getInputStream());
									} catch (Exception e) {
										url = null;
										image = null;
									}

									if (image == null) {
										context.getSource().sendError(new LiteralText("Cringe URL").formatted(Formatting.RED));
										return 1;
									}

									// Our checks passed (valid URL and valid image) so let's send it to the client

									// Actually first we'll set the custom model id
									NbtCompound nbt = heldStack.getOrCreateNbt();
									nbt.putString("modelId", "nyakomod_custom:" + inputName);

									// Build the packet

									PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
									passedData.writeString(input);
									passedData.writeIdentifier(new Identifier("nyakomod_custom", inputName));

									// Then we'll send the packet to all the players
									MinecraftServer server = source.getServer();
									PlayerManager playerManager = server.getPlayerManager();
									List<ServerPlayerEntity> playerList = playerManager.getPlayerList();

									playerList.forEach(currentPlayer ->
										ServerPlayNetworking.send(currentPlayer, IMAGE_DOWNLOAD_PACKET_ID, passedData));

									return 1;
								})
						)
					)
			);





			dispatcher.register(CommandManager.literal("rename")
				.executes(context -> {
					ServerCommandSource source = context.getSource();
					PlayerEntity player = source.getPlayer();
					ItemStack heldStack = player.getMainHandStack();
					if (heldStack.isEmpty()) {
						context.getSource().sendError(new LiteralText("You can't rename nothing!").formatted(Formatting.RED));
					} else {
						heldStack.removeCustomName();
						context.getSource().sendFeedback(new LiteralText("Your item's name has been cleared."), false);
					}
					return 1;
				})
				.then(CommandManager.argument("name", StringArgumentType.greedyString())
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayer();
						ItemStack heldStack = player.getMainHandStack();
						Text newName = TextParser.parse(context.getArgument("name", String.class));
						if (heldStack.isEmpty()) {
							context.getSource().sendError(new LiteralText("You can't rename nothing!").formatted(Formatting.RED));
						} else {
							heldStack.setCustomName(newName);
							context.getSource().sendFeedback(new TranslatableText("Your item has been renamed to \"%s\".", newName), false);
						}
						return 1;
					})
				)
			);

			dispatcher.register(CommandManager.literal("lore")
				.then(CommandManager.literal("clear")
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayer();
						ItemStack heldStack = player.getMainHandStack();
						if (heldStack.isEmpty()) {
							context.getSource().sendError(new LiteralText("You can't clear the lore of nothing!").formatted(Formatting.RED));
						} else {
							NbtCompound nbt = heldStack.getOrCreateNbt();
							NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
							NbtList nbtLore = new NbtList();

							nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
							nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
							heldStack.setNbt(nbt);
							context.getSource().sendFeedback(new LiteralText("Lore cleared."), false);
						}
						return 1;
					})
				)
				.then(CommandManager.literal("add")
					.then(CommandManager.argument("text", StringArgumentType.greedyString())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							PlayerEntity player = source.getPlayer();
							ItemStack heldStack = player.getMainHandStack();
							Text newText = TextParser.parse(context.getArgument("text", String.class));
							if (heldStack.isEmpty()) {
								context.getSource().sendError(new LiteralText("You can't add lore to nothing!").formatted(Formatting.RED));
							} else {
								NbtCompound nbt = heldStack.getOrCreateNbt();
								NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
								NbtList nbtLore = nbtDisplay.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);

								nbtLore.add(NbtString.of(Text.Serializer.toJson(newText)));

								nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
								nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
								heldStack.setNbt(nbt);
								context.getSource().sendFeedback(new LiteralText("Lore applied."), false);
							}
							return 1;
						})
					)
				)
			);
		});



		// Killbind
		ServerPlayNetworking.registerGlobalReceiver(KILL_PLAYER_PACKET_ID, (server, player, packetContext, attachedData, packetSender) -> {
			server.execute(() -> {
				player.damage(DamageSource.MAGIC, 3.4028235E38F);
			});
		});

		// Spunch block
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "spunch_block"), SPUNCH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "spunch_block"), new BlockItem(SPUNCH_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.SOUND_EVENT, SPUNCH_BLOCK_SOUND, SPUNCH_BLOCK_SOUND_EVENT);

		// Drip
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "drip_jacket"), DRIP_JACKET);

		// Launcher
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "launcher"), LAUNCHER_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "launcher"), new BlockItem(LAUNCHER_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

		// Staff of Vorbulation
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "staff_of_vorbulation"), STAFF_OF_VORBULATION_ITEM);

		// Staff of Smiting
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "staff_of_smiting"), STAFF_OF_SMITING_ITEM);

		// Coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "copper_coin"),    COPPER_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "gold_coin"),      GOLD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "emerald_coin"),   EMERALD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "diamond_coin"),   DIAMOND_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "netherite_coin"), NETHERITE_COIN_ITEM);
		Registry.register(Registry.SOUND_EVENT, COIN_COLLECT_SOUND, COIN_COLLECT_SOUND_EVENT);

		// Bag of coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "bag_of_coins"), BAG_OF_COINS_ITEM);

		// TIAB
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "time_in_a_bottle"), TIME_IN_A_BOTTLE);

		// Soul jar
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "soul_jar"), SOUL_JAR);
		DispenserBlock.registerBehavior(SOUL_JAR, new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = (Direction)pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos pos = pointer.getPos(); // getBlockPos?
				World world = pointer.getWorld();
				if (!stack.getOrCreateNbt().contains("entity")) {
					BlockPos blockPos = pointer.getPos().offset((Direction)pointer.getBlockState().get(DispenserBlock.FACING));
					List<Entity> list = pointer.getWorld().getEntitiesByClass(Entity.class, new Box(blockPos), (Entityx) -> {
						return Entityx.isAlive();
					});
					Iterator var5 = list.iterator();

					Entity entity;
					ItemStack newStack;
					do {
						if (!var5.hasNext()) {
							return super.dispenseSilently(pointer, stack);
						}

						entity = (Entity)var5.next();
						newStack = ((SoulJarItem)stack.getItem()).captureEntity(stack,null,(LivingEntity) entity);
					} while(newStack == null);

					return newStack;
				}
				((SoulJarItem) stack.getItem()).spawnEntity(pos,direction,world,stack);
				return stack;
			}
		});

		registerCoinAmounts();
		registerCommands();

		RRPCallback.EVENT.register(a -> a.add(RESOURCE_PACK));
	}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatch, dedicated) -> {
			BackCommand.register(dispatch);
		});
	}

	public static int getCoinValue(EntityType<?> entity) {
		Integer value = coinMap.get(entity);
		if (value == null) {
			System.out.println("Not in map, returning 0");
			return 0;
		}

		System.out.println("In map, returning " + value);
		return value;
	}

	public static void registerCoinAmounts() {
		// Basic hostile mobs
		registerCoinAmount(EntityType.SKELETON, 80, 0, 0, 0,0);
		registerCoinAmount(EntityType.STRAY,    0, 1, 0, 0,0);
		registerCoinAmount(EntityType.CREEPER,  50, 1, 0, 0,0);

		// Zombies
		registerCoinAmount(EntityType.ZOMBIE,          60, 0, 0, 0,0);
		registerCoinAmount(EntityType.DROWNED,         90,  0, 0, 0,0);
		registerCoinAmount(EntityType.ZOMBIE_VILLAGER, 90, 0, 0, 0,0);

		// "Medium difficulty" hostile mobs
		registerCoinAmount(EntityType.WITHER_SKELETON, 0, 1, 0, 0,0);
		registerCoinAmount(EntityType.WITCH,           0, 1, 0, 0,0);
		// Endermen only get 25 copper because of farms
		registerCoinAmount(EntityType.ENDERMAN,    25, 0, 0, 0,0);
		registerCoinAmount(EntityType.BLAZE,       50, 3, 0, 0,0);
		registerCoinAmount(EntityType.SPIDER,      80, 0, 0, 0,0);
		registerCoinAmount(EntityType.CAVE_SPIDER, 0,  1, 0, 0,0);
		registerCoinAmount(EntityType.ENDERMITE,   50, 0, 0, 0,0);

		// Hard hostile mobs
		registerCoinAmount(EntityType.HOGLIN,     0, 2, 0, 0,0);
		registerCoinAmount(EntityType.ZOGLIN,     0, 2, 0, 0,0);
		registerCoinAmount(EntityType.GHAST,      0, 3, 0, 0,0);
		registerCoinAmount(EntityType.GUARDIAN,   0, 1, 0, 0,0);
		registerCoinAmount(EntityType.MAGMA_CUBE, 0, 1, 0, 0,0);
		registerCoinAmount(EntityType.PHANTOM,    0, 1, 0, 0,0);
		registerCoinAmount(EntityType.PIGLIN,     0, 1, 0, 0,0);
		registerCoinAmount(EntityType.PIGLIN_BRUTE, 0, 6, 0, 0,0);
		registerCoinAmount(EntityType.PILLAGER, 0, 1, 0, 0,0);
		registerCoinAmount(EntityType.RAVAGER, 0, 6, 0, 0,0);
		registerCoinAmount(EntityType.SLIME, 0, 1, 0, 0,0);
		registerCoinAmount(EntityType.VINDICATOR, 0, 2, 0, 0,0);
		registerCoinAmount(EntityType.VEX, 50, 0, 0, 0,0);
		registerCoinAmount(EntityType.SHULKER, 0, 1, 0, 0,0);
		registerCoinAmount(EntityType.SILVERFISH, 20, 0, 0, 0,0);

		registerCoinAmount(EntityType.ILLUSIONER, 0, 2, 0, 0,0);
		registerCoinAmount(EntityType.EVOKER, 0, 2, 0, 0,0);

		// Minibosses
		registerCoinAmount(EntityType.IRON_GOLEM, 0, 50, 0, 0, 0);

		// Bosses
		registerCoinAmount(EntityType.WITHER,         0, 0,  4,  0,0);
		registerCoinAmount(EntityType.ENDER_DRAGON,   0, 0,  4, 0,0);
		registerCoinAmount(EntityType.ELDER_GUARDIAN, 0, 50, 0,  0,0);

		// Passive mobs you'd normally kill
		registerCoinAmount(EntityType.CHICKEN,   40, 0, 0, 0,0);
		registerCoinAmount(EntityType.PIG,       60, 0, 0, 0,0);
		registerCoinAmount(EntityType.SHEEP,     60, 0, 0, 0,0);
		registerCoinAmount(EntityType.RABBIT,    80, 0, 0, 0,0);
		registerCoinAmount(EntityType.COW,       80, 0, 0, 0,0);
		registerCoinAmount(EntityType.MOOSHROOM, 50, 2, 0, 0,0);

		registerCoinAmount(EntityType.SALMON,        20, 0, 0, 0,0);
		registerCoinAmount(EntityType.COD,           20, 0, 0, 0,0);
		registerCoinAmount(EntityType.TROPICAL_FISH, 20, 0, 0, 0,0);
		registerCoinAmount(EntityType.PUFFERFISH,    20, 0, 0, 0,0);

		registerCoinAmount(EntityType.SQUID, 20, 0, 0, 0,0);
		registerCoinAmount(EntityType.GLOW_SQUID, 40, 0, 0, 0,0);
		registerCoinAmount(EntityType.GIANT, 0, 50, 0, 0,0);


		// Horses and horse-likes
		registerCoinAmount(EntityType.HORSE,   40, 0, 0, 0,0);
		registerCoinAmount(EntityType.DONKEY,  40, 0, 0, 0,0);
		registerCoinAmount(EntityType.MULE,    40, 0, 0, 0,0);
		registerCoinAmount(EntityType.SKELETON_HORSE, 80, 0, 0, 0,0);
		registerCoinAmount(EntityType.ZOMBIE_HORSE,   80, 0, 0, 0,0);
		registerCoinAmount(EntityType.LLAMA,        40, 0, 0, 0,0);
		registerCoinAmount(EntityType.TRADER_LLAMA, 40, 0, 0, 0,0);


		// Passive mobs you wouldn't normally kill
		registerCoinAmount(EntityType.AXOLOTL, 50, 0, 0, 0,0);
		registerCoinAmount(EntityType.BAT,     0,  4, 0, 0,0);
		registerCoinAmount(EntityType.BEE,     50, 0, 0, 0,0);
		registerCoinAmount(EntityType.CAT,     50, 0, 0, 0,0);
		registerCoinAmount(EntityType.DOLPHIN, 50, 0, 0, 0,0);
		registerCoinAmount(EntityType.FOX,     50, 0, 0, 0,0);
		registerCoinAmount(EntityType.GOAT,    50, 0, 0, 0,0);
		registerCoinAmount(EntityType.OCELOT,  50, 0, 0, 0,0);
		registerCoinAmount(EntityType.PANDA,   50, 0, 0, 0,0);
		registerCoinAmount(EntityType.PARROT,  50, 0, 0, 0,0);
		registerCoinAmount(EntityType.POLAR_BEAR, 50, 1, 0, 0,0);
		registerCoinAmount(EntityType.SNOW_GOLEM, 50, 0, 0, 0,0);

		registerCoinAmount(EntityType.STRIDER, 60, 0, 0, 0,0);

		registerCoinAmount(EntityType.TURTLE, 40, 0, 0, 0,0);
		registerCoinAmount(EntityType.VILLAGER, 60, 0, 0, 0,0);
		registerCoinAmount(EntityType.WANDERING_TRADER, 60, 0, 0, 0,0);
		registerCoinAmount(EntityType.WOLF, 0, 1, 0, 0,0);

		registerCoinAmount(EntityType.ZOMBIFIED_PIGLIN, 50, 1, 0, 0,0);
	}

	public static void registerCoinAmount(EntityType<?> type, int copper, int gold, int emerald, int diamond, int netherite) {
		int total = splitToValue(copper, gold, emerald, diamond, netherite);
		coinMap.put(type, total);
	}

	public static int splitToValue(int copper, int gold, int emerald, int diamond, int netherite) {
		int total = copper
				+ (gold * 100)
				+ (int) (emerald * Math.pow(100, 2))
				+ (int) (diamond * Math.pow(100, 3))
				+ (int) (netherite * Math.pow(100, 4));
		return total;
	}

	public static Map<CoinValue, Integer> valueToSplit(int total) {
		Map<CoinValue, Integer> splitMap = new HashMap<>();
		splitMap.put(CoinValue.COPPER,    total % 100);
		splitMap.put(CoinValue.GOLD,      total / 100);
		splitMap.put(CoinValue.EMERALD,   total / (int) Math.pow(100, 2));
		splitMap.put(CoinValue.DIAMOND,   total / (int) Math.pow(100, 3));
		splitMap.put(CoinValue.NETHERITE, total / (int) Math.pow(100, 4));
		return splitMap;
	}

	public static NativeImage downloadImage(String urlPath) {
		BufferedImage image = null;
		URL url = null;

		System.out.println("downloading " + urlPath);

		try {
			url = new URL(urlPath);
			URLConnection connection = url.openConnection();
			connection.setRequestProperty("User-Agent", "NyakoMod");
			connection.connect();
			image = ImageIO.read(connection.getInputStream());
		} catch (Exception e) {
			url = null;
			image = null;
			e.printStackTrace();
		}

		if (image == null) {
			// ?? we just did this server side but client side it failed so whatever
			System.out.print("failed to dl...?");
			return null;
		}

		// get the NativeImage
		NativeImage nativeImage = null;
		try {
			return getFromBuffered(image);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static boolean downloadSprite(String urlPath, Identifier identifier) {
		// get the NativeImage
		NativeImage nativeImage = downloadImage(urlPath);
		if (nativeImage == null) {
			return false;
		}

		NativeImageBackedTexture nativeImageBackedTexture = new NativeImageBackedTexture(nativeImage);

		MinecraftClient client = MinecraftClient.getInstance();
		client.getTextureManager().registerTexture(identifier, nativeImageBackedTexture);
		System.out.println("finished downloading");

		// RESOURCE_PACK.addTexture(identifier, image);
		// JModel model = JModel.model("item/generated").textures(new JTextures().layer0("minecraft:item/diamond"));
		// RESOURCE_PACK.addModel(model, identifier);

		// RRPCallback.AFTER_VANILLA.register(a -> a.add(RESOURCE_PACK));
		
		// createModel(identifier);

		return true;
	}

	public static JsonUnbakedModel createModel(Identifier identifier) {
		System.out.println("making model for " + identifier);

		var json = String.format("""
		{
			\"parent\": \"item/generated\",
			\"textures\": {
				\"layer0\": \"minecraft:item/diamond\"
			}
		}
		""");
		System.out.println(json);
		var model = JsonUnbakedModel.deserialize(json);
		model.id = identifier.getNamespace() + ":" + identifier.getPath();

		// model.bake(loader, textureGetter, rotationContainer, modelId)

		return model;
	}

	public static NativeImage getFromBuffered(BufferedImage image) throws IOException {
		try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
			ImageIO.write(image, "PNG", outputStream);
			return NativeImage.read(new FastByteArrayInputStream(outputStream.array));
		}
	}

	public static String createItemModelJson(String id, String type) {
		if ("generated".equals(type) || "handheld".equals(type)) {
			//The two types of items. "handheld" is used mostly for tools and the like, while "generated" is used for everything else.
			return "{\n" +
					"  \"parent\": \"item/" + type + "\",\n" +
					"  \"textures\": {\n" +
					"    \"layer0\": \"" + id + "\"\n" +
					"  }\n" +
					"}";
		} else if ("block".equals(type)) {
			//However, if the item is a block-item, it will have a different model json than the previous two.
			return "{\n" +
					"  \"parent\": \"" + id + "\"\n" +
					"}";
		}
		else {
			//If the type is invalid, return an empty json string.
			return "";
		}
	}

	public enum CoinValue {
		COPPER,
		GOLD,
		EMERALD,
		DIAMOND,
		NETHERITE
	}
}