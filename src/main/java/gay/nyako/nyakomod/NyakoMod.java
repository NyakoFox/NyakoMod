package gay.nyako.nyakomod;

import com.mojang.brigadier.arguments.StringArgumentType;
import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.DiscordGachaItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.MapColor;
import net.minecraft.block.Material;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import java.util.*;

import gay.nyako.nyakomod.command.BackCommand;
import gay.nyako.nyakomod.command.XpCommand;
import gay.nyako.nyakomod.mixin.ScoreboardCriterionMixin;
import net.minecraft.world.World;

public class NyakoMod implements ModInitializer {
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
	// Staff of Smiting
	public static final Item STAFF_OF_SMITING_ITEM = new StaffOfSmitingItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());
	// Present
	public static final Item PRESENT_ITEM = new PresentItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Music Discs
	public static final Identifier MUSIC_DISC_MASK_SOUND = new Identifier("nyakomod:music_disc.mask");
	public static SoundEvent MUSIC_DISC_MASK_SOUND_EVENT = new SoundEvent(MUSIC_DISC_MASK_SOUND);
	public static final Item MUSIC_DISC_MASK = new CustomDiscItem(1, MUSIC_DISC_MASK_SOUND_EVENT, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE));

	public static final Identifier MUSIC_DISC_WOLVES_SOUND = new Identifier("nyakomod:music_disc.wolves");
	public static SoundEvent MUSIC_DISC_WOLVES_SOUND_EVENT = new SoundEvent(MUSIC_DISC_WOLVES_SOUND);
	public static final Item MUSIC_DISC_WOLVES = new CustomDiscItem(2, MUSIC_DISC_WOLVES_SOUND_EVENT, new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE));


	// Coins
	public static final Item COPPER_COIN_ITEM    = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100));
	public static final Item GOLD_COIN_ITEM      = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), "gold", 100);
	public static final Item EMERALD_COIN_ITEM   = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), "emerald", 10000);
	public static final Item DIAMOND_COIN_ITEM   = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), "diamond", 1000000);
	public static final Item NETHERITE_COIN_ITEM = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof(), "netherite", 100000000);
	public static final Identifier COIN_COLLECT_SOUND = new Identifier("nyakomod:coin_collect");
	public static SoundEvent COIN_COLLECT_SOUND_EVENT = new SoundEvent(COIN_COLLECT_SOUND);

	public static Map<EntityType<?>, Integer> coinMap = new HashMap<>();

	public static final ScoreboardCriterion COIN_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:coins");

	// Bag of coins
	public static final Item BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	public static final Item HUNGRY_BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Time in a bottle
	public static final TimeInABottleItem TIME_IN_A_BOTTLE = new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Soul jar
	public static final SoulJarItem SOUL_JAR = new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Player smite packet
	public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");

	// Bricks
	public static final Block BRICKUS = new Block(AbstractBlock.Settings.of(Material.STONE, MapColor.RED).requiresTool().strength(2.0f, 6.0f));
	public static final Block BRICKUS_STAIRS = new CustomStairsBlock(BRICKUS.getDefaultState(), AbstractBlock.Settings.copy(BRICKUS));
	public static final Block BRICKUS_SLAB = new CustomSlabBlock(AbstractBlock.Settings.copy(BRICKUS));
	public static final Block BRICKUS_WALL = new CustomWallBlock(AbstractBlock.Settings.copy(BRICKUS));

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

	public static final Block MATTER_VORTEX_BLOCK = new MatterVortexBlock(FabricBlockSettings.copy(Blocks.STONE).requiresTool());


	// Gacha items

	public static final Item DIAMOND_GACHA_ITEM = new GachaItem(
			new FabricItemSettings().group(ItemGroup.MISC).food(FoodComponents.GOLDEN_CARROT),
			2,
			Arrays.asList(
				(MutableText) Text.of("You can't make tools out of these,"),
				(MutableText) Text.of("but at least they're healthy!")
			)
	);

	public static final Item MARIO_GACHA_ITEM = new GachaItem(
			new FabricItemSettings().group(ItemGroup.MISC),
			4,
			(MutableText) Text.of("The lovable plumber!")
	);

	public static final Item LUIGI_GACHA_ITEM = new GachaItem(
			new FabricItemSettings().group(ItemGroup.MISC),
			4,
			(MutableText) Text.of("The lovable plumber's brother!")
	);

	public static final Item DISCORD_GACHA_ITEM = new DiscordGachaItem(new FabricItemSettings().group(ItemGroup.MISC));

	// Staff of Vorbulation
	public static final Item STAFF_OF_VORBULATION_ITEM = new StaffOfVorbulationItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());

	public record GachaEntry (
			Text name,
			ItemStack itemStack,
			int rarity,
			double weight
	) {}

	public static List<GachaEntry> gachaEntryList = new ArrayList<>();

	public static final Identifier DISCORD_SOUND = new Identifier("nyakomod:discord");
	public static SoundEvent DISCORD_SOUND_EVENT = new SoundEvent(DISCORD_SOUND);
	public void registerGachaItems() {
		/* REGISTRY */
		// Items
		// Squishy Diamond
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "diamond_gacha"), DIAMOND_GACHA_ITEM);
		// Discord Logo
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "discord_gacha"), DISCORD_GACHA_ITEM);
		// Staff of Vorbulation
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "staff_of_vorbulation"), STAFF_OF_VORBULATION_ITEM);

		// Mario
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "mario_gacha"), MARIO_GACHA_ITEM);
		// Luigi
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "luigi_gacha"), LUIGI_GACHA_ITEM);

		// Sounds
		Registry.register(Registry.SOUND_EVENT, DISCORD_SOUND, DISCORD_SOUND_EVENT);

		/* 1 STAR */
		// 1 Gold CunkCoin
		registerGachaItem(Text.of("1 §6Gold CunkCoin™"), new ItemStack(GOLD_COIN_ITEM), 1);
		registerGachaItem(Text.of("64 §6Dirt"), Items.DIRT, 64, 1);
		registerGachaItem(Text.of("16 §6Oak Logs"), Items.OAK_LOG, 16, 1);
		registerGachaItem(Text.of("16 §6Dark Oak Logs"), Items.DARK_OAK_LOG, 16, 1);
		registerGachaItem(Text.of("16 §6Spruce Logs"), Items.SPRUCE_LOG, 16, 1);
		registerGachaItem(Text.of("16 §6Acacia Logs"), Items.ACACIA_LOG, 16, 1);
		registerGachaItem(Text.of("16 §6Birch Logs"), Items.BIRCH_LOG, 16, 1);
		registerGachaItem(Text.of("16 §6Jungle Logs"), Items.JUNGLE_LOG, 16, 1);
		registerGachaItem(Text.of("16 §cCrimson Stems"), Items.CRIMSON_STEM, 16, 1);
		registerGachaItem(Text.of("16 §bWarped Stems"), Items.WARPED_STEM, 16, 1);

		// Bow
		registerGachaItem(Text.of("a §7Bow"), Items.BOW, 1, 1);

		/* 2 STAR */
		registerGachaItem(Text.of("an §5Uncraftable Potion...?"), new ItemStack(Items.POTION), 2);
		// wolves
		registerGachaItem(Text.of("a §bMusic Disc"), new ItemStack(MUSIC_DISC_WOLVES), 2);
		registerGachaItem(Text.of("16 §bSquishy Diamonds"), (GachaItem) DIAMOND_GACHA_ITEM, 16);
		registerGachaItem(Text.of("32 §6Cookies"), Items.COOKIE, 32, 2);

		registerGachaItem(Text.of("16 §aOak Leaves"), Items.OAK_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §aDark Oak Leaves"), Items.DARK_OAK_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §aSpruce Leaves"), Items.SPRUCE_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §aAcacia Leaves"), Items.ACACIA_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §a§aBirch Leaves"), Items.BIRCH_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §aJungle Leaves"), Items.JUNGLE_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §cNether Wart Blocks"), Items.NETHER_WART_BLOCK, 16, 2);
		registerGachaItem(Text.of("16 §bWarped Wart Blocks"), Items.WARPED_WART_BLOCK, 16, 2);
		registerGachaItem(Text.of("16 §aAzalea Leaves"), Items.AZALEA_LEAVES, 16, 2);
		registerGachaItem(Text.of("16 §aFlowering Azalea Leaves"), Items.FLOWERING_AZALEA_LEAVES, 16, 2);

		registerGachaItem(Text.of("32 §7Sticks"), Items.STICK, 32, 2);
		registerGachaItem(Text.of("a §7Fishing Rod"), Items.FISHING_ROD, 1, 2);
		registerGachaItem(Text.of("2 §dCow Spawn Eggs"), Items.COW_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dPig Spawn Eggs"), Items.PIG_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dSheep Spawn Eggs"), Items.SHEEP_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dRabbit Spawn Eggs"), Items.RABBIT_SPAWN_EGG, 2, 2);
		registerGachaItem(Text.of("2 §dChicken Spawn Eggs"), Items.CHICKEN_SPAWN_EGG, 2, 2);

		/* 3 STAR */
		registerGachaItem(Text.of("the §9Discord Logo"), (GachaItem) DISCORD_GACHA_ITEM);
		registerGachaItem(Text.of("a §2Potion of Luck"), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LUCK), 3);
		registerGachaItem(Text.of("5 §6Gold CunkCoin™"), GOLD_COIN_ITEM, 5, 3);
		registerGachaItem(Text.of("64 §7Cobblestone"), Items.COBBLESTONE, 64, 3);
		registerGachaItem(Text.of("64 §cTorches"), Items.TORCH, 64, 3);
		registerGachaItem(Text.of("16 §7Iron Ingots"), Items.IRON_INGOT, 16, 3);
		registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 3);
		registerGachaItem(Text.of("16 §dItem Frames"), Items.ITEM_FRAME, 16, 3);
		registerGachaItem(Text.of("16 §7Arrows"), Items.ARROW, 16, 3);

		// Mario and Luigi
		ItemStack brotherStack = new ItemStack(PRESENT_ITEM);
		PresentItem.addToPresent(brotherStack, new ItemStack(MARIO_GACHA_ITEM));
		PresentItem.addToPresent(brotherStack, new ItemStack(LUIGI_GACHA_ITEM));
		brotherStack.setCustomName(Text.of("Present (Brothers)"));
		registerGachaItem(Text.of("§cThe §4Brothers"), brotherStack, 3);

		/* 4 STAR */
		registerGachaItem(Text.of("10 §6Gold CunkCoin™"), GOLD_COIN_ITEM, 10, 4);
		registerGachaItem(Text.of("the §5Staff of Vorbulation"), (GachaItem) STAFF_OF_VORBULATION_ITEM);
		registerGachaItem(Text.of("32 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 32, 4);
		registerGachaItem(Text.of("16 §eGlowing Item Frames"), Items.GLOW_ITEM_FRAME, 16, 4);
		registerGachaItem(Text.of("16 §eSpectral Arrows"), Items.SPECTRAL_ARROW, 16, 4);
		registerGachaItem(Text.of("32 §bGlass"), Items.GLASS, 32, 4);
		registerGachaItem(Text.of("1 §dVillager Spawn Egg"), Items.VILLAGER_SPAWN_EGG, 1, 4);
		registerGachaItem(Text.of("4 §dWandering Trader Spawn Eggs"), Items.WANDERING_TRADER_SPAWN_EGG, 4, 4);
		registerGachaItem(Text.of("16 §cTNT"), Items.TNT, 16, 4);
		registerGachaItem(Text.of("16 §5Dragon's Breath"), Items.DRAGON_BREATH, 16, 4);

		/* 5 STAR */
		registerGachaItem(Text.of("a §dDragon §5Egg"), Items.DRAGON_EGG, 1, 5);
		registerGachaItem(Text.of("20 §6Gold CunkCoin™"), GOLD_COIN_ITEM, 20, 5);
		registerGachaItem(Text.of("1 §4Ancient Debris"), Items.ANCIENT_DEBRIS, 1, 5);
		registerGachaItem(Text.of("8 §bDiamonds"), Items.DIAMOND, 8, 5);
		// Diamond tool kit
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
		registerGachaItem(Text.of("a Diamond Armor Kit"), armorStack, 5);
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


	@Override
	public void onInitialize() {
		System.out.println("owo");

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("rename")
				.executes(context -> {
					ServerCommandSource source = context.getSource();
					PlayerEntity player = source.getPlayerOrThrow();
					ItemStack heldStack = player.getMainHandStack();
					if (heldStack.isEmpty()) {
						context.getSource().sendError(Text.literal("You can't rename nothing!").formatted(Formatting.RED));
					} else {
						heldStack.removeCustomName();
						context.getSource().sendFeedback(Text.literal("Your item's name has been cleared."), false);
					}
					return 1;
				})
				.then(CommandManager.argument("name", StringArgumentType.greedyString())
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ItemStack heldStack = player.getMainHandStack();
						String string = context.getArgument("name", String.class);
						Text newName = TextParserUtils.formatText(string);
						if (heldStack.isEmpty()) {
							context.getSource().sendError(Text.literal("You can't rename nothing!").formatted(Formatting.RED));
						} else {
							heldStack.setCustomName(newName);
							context.getSource().sendFeedback(Text.translatable("Your item has been renamed to \"%s\".", newName), false);
						}
						return 1;
					})
				)
			);

			dispatcher.register(CommandManager.literal("lore")
				.then(CommandManager.literal("clear")
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ItemStack heldStack = player.getMainHandStack();
						if (heldStack.isEmpty()) {
							context.getSource().sendError(Text.literal("You can't clear the lore of nothing!").formatted(Formatting.RED));
						} else {
							NbtCompound nbt = heldStack.getOrCreateNbt();
							NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
							NbtList nbtLore = new NbtList();

							nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
							nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
							heldStack.setNbt(nbt);
							context.getSource().sendFeedback(Text.literal("Lore cleared."), false);
						}
						return 1;
					})
				)
				.then(CommandManager.literal("add")
					.then(CommandManager.argument("text", StringArgumentType.greedyString())
						.executes(context -> {
							ServerCommandSource source = context.getSource();
							PlayerEntity player = source.getPlayerOrThrow();
							ItemStack heldStack = player.getMainHandStack();
							Text newText = TextParserUtils.formatText(context.getArgument("text", String.class));
							if (heldStack.isEmpty()) {
								context.getSource().sendError(Text.literal("You can't add lore to nothing!").formatted(Formatting.RED));
							} else {
								NbtCompound nbt = heldStack.getOrCreateNbt();
								NbtCompound nbtDisplay = nbt.getCompound(ItemStack.DISPLAY_KEY);
								NbtList nbtLore = nbtDisplay.getList(ItemStack.LORE_KEY, NbtElement.STRING_TYPE);

								nbtLore.add(NbtString.of(Text.Serializer.toJson(newText)));

								nbtDisplay.put(ItemStack.LORE_KEY, nbtLore);
								nbt.put(ItemStack.DISPLAY_KEY, nbtDisplay);
								heldStack.setNbt(nbt);
								context.getSource().sendFeedback(Text.literal("Lore applied."), false);
							}
							return 1;
						})
					)
				)
			);
		});



		// Killbind
		ServerPlayNetworking.registerGlobalReceiver(KILL_PLAYER_PACKET_ID,
				(server, player, handler, buffer, sender) -> server.execute(() -> {
					player.damage(DamageSource.MAGIC, 3.4028235E38F);
					MinecraftClient.getInstance().player.addVelocity(0D, 5D, 0D);
				}));

		// Spunch block
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "spunch_block"), SPUNCH_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "spunch_block"), new BlockItem(SPUNCH_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.SOUND_EVENT, SPUNCH_BLOCK_SOUND, SPUNCH_BLOCK_SOUND_EVENT);

		// Drip
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "drip_jacket"), DRIP_JACKET);

		// Launcher
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "launcher"), LAUNCHER_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "launcher"), new BlockItem(LAUNCHER_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

		// Brickus
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "brickus"), BRICKUS);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "brickus_stairs"), BRICKUS_STAIRS);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "brickus_slab"), BRICKUS_SLAB);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "brickus_wall"), BRICKUS_WALL);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "brickus"), new BlockItem(BRICKUS, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "brickus_stairs"), new BlockItem(BRICKUS_STAIRS, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "brickus_slab"), new BlockItem(BRICKUS_SLAB, new FabricItemSettings().group(ItemGroup.MISC)));
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "brickus_wall"), new BlockItem(BRICKUS_WALL, new FabricItemSettings().group(ItemGroup.MISC)));

		// Staff of Smiting
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "staff_of_smiting"), STAFF_OF_SMITING_ITEM);

		// Present
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "present"), PRESENT_ITEM);

		// Coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "copper_coin"),    COPPER_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "gold_coin"),      GOLD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "emerald_coin"),   EMERALD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "diamond_coin"),   DIAMOND_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "netherite_coin"), NETHERITE_COIN_ITEM);
		Registry.register(Registry.SOUND_EVENT, COIN_COLLECT_SOUND, COIN_COLLECT_SOUND_EVENT);

		// Bag of coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "bag_of_coins"), BAG_OF_COINS_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "hungry_bag_of_coins"), HUNGRY_BAG_OF_COINS_ITEM);

		// TIAB
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "time_in_a_bottle"), TIME_IN_A_BOTTLE);

		// Soul jar
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "soul_jar"), SOUL_JAR);

		// Discs
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "music_disc_mask"), MUSIC_DISC_MASK);
		Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_MASK_SOUND, MUSIC_DISC_MASK_SOUND_EVENT);

		Registry.register(Registry.ITEM, new Identifier("nyakomod", "music_disc_wolves"), MUSIC_DISC_WOLVES);
		Registry.register(Registry.SOUND_EVENT, MUSIC_DISC_WOLVES_SOUND, MUSIC_DISC_WOLVES_SOUND_EVENT);

		Registry.register(Registry.SOUND_EVENT, WOLVES_SOUND, WOLVES_SOUND_EVENT);

		// Gacha-related
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "matter_vortex"), MATTER_VORTEX_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "matter_vortex"), new BlockItem(MATTER_VORTEX_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

		registerGachaItems();

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

	}

	public static void registerCommands() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			BackCommand.register(dispatcher);
			XpCommand.register(dispatcher);
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
		registerCoinAmount(EntityType.ELDER_GUARDIAN, 0, 50, 0,  0,0);
		// She spawns coins herself, so don't register
		//registerCoinAmount(EntityType.ENDER_DRAGON,   0, 0,  4, 0,0);

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
		splitMap.put(CoinValue.GOLD,      (total / 100) % 100);
		splitMap.put(CoinValue.EMERALD,   (total / (int) Math.pow(100, 2)) % 100);
		splitMap.put(CoinValue.DIAMOND,   (total / (int) Math.pow(100, 3)) % 100);
		splitMap.put(CoinValue.NETHERITE, (total / (int) Math.pow(100, 4)));
		return splitMap;
	}

	public static void giveCoins(PlayerEntity player, long amount) {
		giveCoins(player.getInventory(), amount);
	}

	public static void giveCoins(Inventory inventory, long amount) {
		System.out.println("Giving " + amount + " coins");
		Map<NyakoMod.CoinValue, Integer> map = NyakoMod.valueToSplit((int) amount);

		Integer copper = map.get(NyakoMod.CoinValue.COPPER);
		Integer gold = map.get(NyakoMod.CoinValue.GOLD);
		Integer emerald = map.get(NyakoMod.CoinValue.EMERALD);
		Integer diamond = map.get(NyakoMod.CoinValue.DIAMOND);
		Integer netherite = map.get(NyakoMod.CoinValue.NETHERITE);

		if (copper > 0) {
			ItemStack stack = new ItemStack(NyakoMod.COPPER_COIN_ITEM);
			stack.setCount(copper);
			System.out.println("Giving " + copper + " copper");
			if (inventory instanceof SimpleInventory) {
				((SimpleInventory) inventory).addStack(stack);
			} else if (inventory instanceof PlayerInventory) {
				((PlayerInventory) inventory).insertStack(stack);
			}
		}
		if (gold > 0) {
			ItemStack stack = new ItemStack(NyakoMod.GOLD_COIN_ITEM);
			stack.setCount(gold);
			System.out.println("Giving " + gold + " gold");
			if (inventory instanceof SimpleInventory) {
				((SimpleInventory) inventory).addStack(stack);
			} else if (inventory instanceof PlayerInventory) {
				((PlayerInventory) inventory).insertStack(stack);
			}
		}
		if (emerald > 0) {
			ItemStack stack = new ItemStack(NyakoMod.EMERALD_COIN_ITEM);
			stack.setCount(emerald);
			System.out.println("Giving " + emerald + " emerald");
			if (inventory instanceof SimpleInventory) {
				((SimpleInventory) inventory).addStack(stack);
			} else if (inventory instanceof PlayerInventory) {
				((PlayerInventory) inventory).insertStack(stack);
			}
		}
		if (diamond > 0) {
			ItemStack stack = new ItemStack(NyakoMod.DIAMOND_COIN_ITEM);
			stack.setCount(diamond);
			System.out.println("Giving " + diamond + " diamond");
			if (inventory instanceof SimpleInventory) {
				((SimpleInventory) inventory).addStack(stack);
			} else if (inventory instanceof PlayerInventory) {
				((PlayerInventory) inventory).insertStack(stack);
			}
		}
		if (netherite > 0) {
			ItemStack stack = new ItemStack(NyakoMod.NETHERITE_COIN_ITEM);
			stack.setCount(netherite);
			System.out.println("Giving " + netherite + " netherite");
			if (inventory instanceof SimpleInventory) {
				((SimpleInventory) inventory).addStack(stack);
			} else if (inventory instanceof PlayerInventory) {
				((PlayerInventory) inventory).insertStack(stack);
			}
		}
	}

	public static void removeCoins(PlayerEntity player, long amount) {
		long removed = 0;

		removed += removeCoinsFromInventory(player.getInventory(), amount, removed);
		removed += removeCoinsFromInventory(player.getEnderChestInventory(), amount, removed);
	}

	public static long removeCoinsFromInventory(Inventory inventory, long amount, long removed) {
		for (int i = 0; i < inventory.size(); ++i) {
			var stack = inventory.getStack(i);
			var item = stack.getItem();

			if (item instanceof CoinItem) {
				while (removed < amount) {
					removed += ((CoinItem) item).getCoinValue();
					stack.decrement(1);
					if (stack.getCount() == 0) break;
				}
			} else if (item instanceof BagOfCoinsItem) {
				NbtCompound tag = stack.getOrCreateNbt();
				long bagAmount = 0;
				bagAmount += tag.getInt("copper");
				bagAmount += tag.getInt("gold") * 100L;
				bagAmount += tag.getInt("emerald") * 10000L;
				bagAmount += tag.getInt("diamond") * 1000000L;
				bagAmount += tag.getInt("netherite") * 100000000L;

				long toRemove = amount - removed;
				if ((bagAmount - toRemove) < 0) {
					removed += bagAmount;
					bagAmount = 0;
				} else {
					removed += toRemove;
					bagAmount -= toRemove;
				}

				Map<NyakoMod.CoinValue, Integer> map = NyakoMod.valueToSplit((int) bagAmount);

				tag.putInt("copper",    map.get(NyakoMod.CoinValue.COPPER));
				tag.putInt("gold",      map.get(NyakoMod.CoinValue.GOLD));
				tag.putInt("emerald",   map.get(NyakoMod.CoinValue.EMERALD));
				tag.putInt("diamond",   map.get(NyakoMod.CoinValue.DIAMOND));
				tag.putInt("netherite", map.get(NyakoMod.CoinValue.NETHERITE));

				stack.setNbt(tag);
			}

			if (removed > amount) {
				giveCoins(inventory, removed - amount);
				removed = amount;
			}
			if (removed >= amount) {
				return removed;
			}
		}
		return removed;
	}

	public static int countInventoryCoins(Inventory inventory) {
		int total = 0;
		for (int i = 0; i < inventory.size(); ++i) {
			var stack = inventory.getStack(i);
			var item = stack.getItem();

			if (item instanceof CoinItem) {
				total += stack.getCount() * ((CoinItem) item).getCoinValue();
			} else if (item instanceof BagOfCoinsItem) {
				NbtCompound tag = stack.getOrCreateNbt();
				total += tag.getInt("copper");
				total += tag.getInt("gold") * 100;
				total += tag.getInt("emerald") * 10000;
				total += tag.getInt("diamond") * 1000000;
				total += tag.getInt("netherite") * 100000000;
			}
		}

		return total;
	}

	public enum CoinValue {
		COPPER,
		GOLD,
		EMERALD,
		DIAMOND,
		NETHERITE
	}
}