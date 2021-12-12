package gay.nyako.nyakomod;

import com.mojang.brigadier.arguments.StringArgumentType;
import eu.pb4.placeholders.TextParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v1.FabricLootPoolBuilder;
import net.fabricmc.fabric.api.loot.v1.event.LootTableLoadingCallback;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
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
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.minecraft.item.Items;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gay.nyako.nyakomod.command.BackCommand;

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

	public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");

	@Override
	public void onInitialize() {
		System.out.println("owo");

		// Register commands
		CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
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
		ServerSidePacketRegistry.INSTANCE.register(KILL_PLAYER_PACKET_ID, (packetContext, attachedData) -> {
			packetContext.getTaskQueue().execute(() -> {
				PlayerEntity player = packetContext.getPlayer();
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

		registerCoinAmounts();
		registerCommands();
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

	public enum CoinValue {
		COPPER,
		GOLD,
		EMERALD,
		DIAMOND,
		NETHERITE
	}
}