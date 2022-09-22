package gay.nyako.nyakomod;

import com.google.gson.*;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import gay.nyako.nyakomod.block.*;
import gay.nyako.nyakomod.command.*;
import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.entity.PetSpriteEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.DiscordGachaItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import gay.nyako.nyakomod.mixin.ScoreboardCriterionMixin;
import gay.nyako.nyakomod.screens.CunkShopScreenHandler;
import gay.nyako.nyakomod.screens.ShopData;
import gay.nyako.nyakomod.screens.ShopEntries;
import gay.nyako.nyakomod.screens.ShopEntry;
import gay.nyako.nyakomod.NyakoModPotion;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.models.JModel;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Rarity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static net.devtech.arrp.json.models.JModel.textures;

public class NyakoMod implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger("nyakomod");

	public static final gay.nyako.nyakomod.NyakoConfig CONFIG = gay.nyako.nyakomod.NyakoConfig.createAndLoad();

	public static final IntProperty COINS_PROPERTY = IntProperty.of("coins", 1, SingleCoinBlock.MAX_COINS);

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
	// Custom
	public static final Item CUSTOM_ITEM = new CustomItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(69));

	// /dev/null
	public static final Item DEV_NULL_ITEM = new DevNullItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Coins
	public static final Block GOLD_SINGLE_COIN_BLOCK      = new SingleCoinBlock(FabricBlockSettings.of(Material.METAL).strength(0.3f));
	public static final Block COPPER_SINGLE_COIN_BLOCK    = new SingleCoinBlock(FabricBlockSettings.of(Material.METAL).strength(0.3f));
	public static final Block EMERALD_SINGLE_COIN_BLOCK   = new SingleCoinBlock(FabricBlockSettings.of(Material.METAL).strength(0.3f));
	public static final Block DIAMOND_SINGLE_COIN_BLOCK   = new SingleCoinBlock(FabricBlockSettings.of(Material.METAL).strength(0.3f));
	public static final Block NETHERITE_SINGLE_COIN_BLOCK = new SingleCoinBlock(FabricBlockSettings.of(Material.METAL).strength(0.3f));

	public static final Item COPPER_COIN_ITEM            = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), COPPER_SINGLE_COIN_BLOCK, "copper", 1);
	public static final Item GOLD_COIN_ITEM              = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), GOLD_SINGLE_COIN_BLOCK, "gold", 100);
	public static final Item EMERALD_COIN_ITEM           = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), EMERALD_SINGLE_COIN_BLOCK, "emerald", 10000);
	public static final Item DIAMOND_COIN_ITEM           = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), DIAMOND_SINGLE_COIN_BLOCK, "diamond", 1000000);
	public static final Item NETHERITE_COIN_ITEM         = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof(), NETHERITE_SINGLE_COIN_BLOCK, "netherite", 100000000);
	public static final Identifier COIN_COLLECT_SOUND = new Identifier("nyakomod:coin_collect");
	public static SoundEvent COIN_COLLECT_SOUND_EVENT = new SoundEvent(COIN_COLLECT_SOUND);

	public static final ScoreboardCriterion COIN_CRITERIA = ScoreboardCriterionMixin.create("nyakomod:coins");

	public static final Identifier CUNK_SHOP_PURCHASE_PACKET_ID = new Identifier("nyakomod", "purchase");

	// Bag of coins
	public static final Item BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	public static final Item HUNGRY_BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Time in a bottle
	public static final TimeInABottleItem TIME_IN_A_BOTTLE = new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
	// Soul jar
	public static final SoulJarItem SOUL_JAR = new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Test item
	public static final Item TEST_ITEM = new TestItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

	// Player smite packet
	public static final Identifier PLAYER_SMITE_PACKET_ID = new Identifier("nyakomod", "player_smite");
	public static final Identifier PET_SPRITE_SET_URL = new Identifier("nyakomod", "set_pet_sprite_custom_sprite");
	public static final Identifier MODEL_CREATE_PACKET = new Identifier("nyakomod", "create_model");

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

	public record GachaEntry(
			Text name,
			ItemStack itemStack,
			int rarity,
			double weight
	) {
	}

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

	public static final Item PET_SPRITE_SUMMON_ITEM = new PetSpriteSummonItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());
	public static final Item PET_DRAGON_SUMMON_ITEM = new PetSummonItem<>(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof(), NyakoMod.PET_DRAGON, PetDragonEntity::createPet);


	public static List<GachaEntry> gachaEntryList = new ArrayList<>();

	public static final Identifier DISCORD_SOUND = new Identifier("nyakomod:discord");
	public static SoundEvent DISCORD_SOUND_EVENT = new SoundEvent(DISCORD_SOUND);

	@Environment(EnvType.SERVER)
	public static CachedResourcePack CACHED_RESOURCE_PACK = new CachedResourcePack();

	@Environment(EnvType.SERVER)
	public static ModelManager MODEL_MANAGER = new ModelManager();

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

		Registry.register(Registry.ITEM, new Identifier("nyakomod", "pet_sprite_summon"), PET_SPRITE_SUMMON_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "pet_dragon_summon"), PET_DRAGON_SUMMON_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "dev_null"), DEV_NULL_ITEM);

		// Sounds
		Registry.register(Registry.SOUND_EVENT, DISCORD_SOUND, DISCORD_SOUND_EVENT);

		/* 1 STAR */
		// 1 Gold CunkCoin
		registerGachaItem(Text.of("1 §6Gold CunkCoin™"), new ItemStack(GOLD_COIN_ITEM), 1);
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
		registerGachaItem(Text.of("16 §bSquishy Diamonds"), (GachaItem) DIAMOND_GACHA_ITEM, 16);
		registerGachaItem(Text.of("32 §6Cookies"), Items.COOKIE, 32, 2);

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
		registerGachaItem(Text.of("a §2Potion of Unluck"), PotionUtil.setPotion(new ItemStack(Items.POTION), NyakoModPotion.UNLUCK), 3);
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
		registerGachaItem(Text.of("20 §6Gold CunkCoin™"), GOLD_COIN_ITEM, 20, 5);
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


	private LootPool.Builder addLootTableCoins(LootPool.Builder lootPoolBuilder, Item coinItem, int min, int max, int weight) {
		return lootPoolBuilder.with(ItemEntry.builder(coinItem)
				.weight(weight)
				.quality(0)
				.apply(SetCountLootFunction.builder(
						UniformLootNumberProvider.create(min, max)
				))
		);
	}

	@Override
	public void onInitialize() {

		List<Identifier> coinLootTables = new ArrayList<>();
		coinLootTables.add(new Identifier("minecraft", "chests/simple_dungeon"));
		coinLootTables.add(new Identifier("minecraft", "chests/abandoned_mineshaft"));
		coinLootTables.add(new Identifier("minecraft", "chests/stronghold_corridor"));
		coinLootTables.add(new Identifier("minecraft", "chests/stronghold_crossing"));
		coinLootTables.add(new Identifier("minecraft", "chests/stronghold_library"));
		coinLootTables.add(new Identifier("minecraft", "chests/woodland_mansion"));
		coinLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_small"));
		coinLootTables.add(new Identifier("minecraft", "chests/underwater_ruin_big"));
		coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_map"));
		coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_supply"));
		coinLootTables.add(new Identifier("minecraft", "chests/nether_bridge"));
		coinLootTables.add(new Identifier("minecraft", "chests/shipwreck_treasure"));
		coinLootTables.add(new Identifier("minecraft", "chests/buried_treasure"));
		coinLootTables.add(new Identifier("minecraft", "chests/end_city_treasure"));
		coinLootTables.add(new Identifier("minecraft", "chests/pillager_outpost"));
		coinLootTables.add(new Identifier("minecraft", "chests/jungle_temple"));
		coinLootTables.add(new Identifier("minecraft", "chests/desert_pyramid"));
		coinLootTables.add(new Identifier("minecraft", "chests/igloo_chest"));
		coinLootTables.add(new Identifier("minecraft", "chests/ruined_portal"));
		coinLootTables.add(new Identifier("minecraft", "chests/village/village_weaponsmith"));

		LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
			// Check if the loot table is one of the coin loot tables
			if (source.isBuiltin() && coinLootTables.contains(id)) {
				// Add a random amount of copper coins to the loot table
				var copperMin = 50;
				var copperMax = 95;
				var copperWeight = 1;

				var lootPoolBuilder = LootPool.builder()
						.rolls(ConstantLootNumberProvider.create(1));

				switch (id.toString()) {
					// treasure
					case "minecraft:chests/end_city_treasure":
						lootPoolBuilder = addLootTableCoins(lootPoolBuilder, EMERALD_COIN_ITEM, 1, 2, 5);
					case "minecraft:chests/buried_treasure":
					case "minecraft:chests/shipwreck_treasure":
						copperMin = 0;
						copperMax = 100;
						lootPoolBuilder = addLootTableCoins(lootPoolBuilder, GOLD_COIN_ITEM, 40, 80, 10);
						break;
				}

				lootPoolBuilder = addLootTableCoins(lootPoolBuilder, COPPER_COIN_ITEM, copperMin, copperMax, copperWeight);

				tableBuilder.pool(lootPoolBuilder);
			}
		});

		ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
			@Override
			public Identifier getFabricId() {
				return new Identifier("nyakomod", "shops");
			}

			@Override
			public void reload(ResourceManager manager) {
				ShopEntries.shops.clear();

				Gson gson = new Gson();

				manager.findResources("shops", identifier -> identifier.getPath().endsWith(".json")).forEach((resourceId, resource) -> {
					try {
						var shopId = new Identifier(
								resourceId.getNamespace(),
								resourceId.getPath().substring(6, resourceId.getPath().length() - 5)
						);

						var shopData = new ShopData(shopId);

						// Use GSon to parse the JSON file into a JsonObject
						JsonObject shopJson = JsonParser.parseReader(new InputStreamReader(resource.getInputStream())).getAsJsonObject();
						shopJson.getAsJsonArray("entries").forEach(entry -> {
							JsonObject entryJson = entry.getAsJsonObject();
							var stacks = new ArrayList<ItemStack>();
							entryJson.getAsJsonArray("items").forEach(item -> {
								var jsonObject = item.getAsJsonObject();
								if ((jsonObject.get("tag") != null) &&
									(jsonObject.get("tag").getAsJsonObject().get("display") != null) &&
									(jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject().get("Name") != null)) {
									var display = jsonObject.get("tag").getAsJsonObject().get("display").getAsJsonObject();
									var string = gson.toJson(display.get("Name").getAsJsonObject());
									display.addProperty("Name",  string);
								}
								NbtCompound converted = (NbtCompound) Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, jsonObject);
								stacks.add(ItemStack.fromNbt(converted));
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

						/*
						shopJson.getJSONArray("items").forEach(item -> {
							var itemJson = (JSONObject) item;
							Dynamic.convert(JsonOps.INSTANCE, NbtOps.INSTANCE, itemJson).result().ifPresent(nbt -> {
								var itemStack = ItemStack.fromNbt(nbt);
								shopData.addItem(itemStack);
							});
							var itemStack = new ItemStack(Registry.ITEM.get(new Identifier(itemJson.getString("item"))));
							itemStack.setCount(itemJson.optInt("count", 1));
							var chance = itemJson.getDouble("chance");
							var gachaEntry = new GachaEntry(
									Text.of(itemJson.getString("name")),
									itemStack,
									rarity,
									chance
							);
							shopData.gachaEntryList.add(gachaEntry);
						});*/


						ShopEntries.register(shopData);
					} catch (Exception e) {
						NyakoMod.LOGGER.error("Error occurred while loading resource json " + resourceId, e);
					}
				});
			}
		});

		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("smite")
					.executes(context -> {
						ServerPlayerEntity player = context.getSource().getPlayer();
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
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ServerWorld world = source.getWorld();

						player.openHandledScreen(new NamedScreenHandlerFactory() {
							@Override
							public Text getDisplayName() {
								return Text.literal("Icon Selector");
							}

							@Override
							public @NotNull ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
								return new IconScreenHandler(syncId, inv, ScreenHandlerContext.create(world, player.getBlockPos()));
							}
						});

						return 0;
					})
			);
			dispatcher.register(CommandManager.literal("shop")
					.executes(context -> {
						ServerCommandSource source = context.getSource();
						PlayerEntity player = source.getPlayerOrThrow();
						ServerWorld world = source.getWorld();

						openShop(player, world, ShopEntries.MAIN);

						return 0;
					})
			);
		});

		// Killbind
		ServerPlayNetworking.registerGlobalReceiver(KILL_PLAYER_PACKET_ID,
				(server, player, handler, buffer, sender) -> server.execute(() -> {
					player.damage(DamageSource.MAGIC, 3.4028235E38F);
				}));

		// Custom Sprite Setting
		ServerPlayNetworking.registerGlobalReceiver(PET_SPRITE_SET_URL,
				(server, player, handler, buffer, sender) -> {
					var string = buffer.readString();
					var size = buffer.readDouble();

					server.execute(() -> {
						var stack = player.getMainHandStack();
						if (!stack.isOf(PET_SPRITE_SUMMON_ITEM)) {
							stack = player.getOffHandStack();
							if (!stack.isOf(PET_SPRITE_SUMMON_ITEM)) {
								return;
							}
						}

						var nbt = stack.getOrCreateNbt();
						nbt.putString("custom_sprite", string);
						nbt.putDouble("pet_size", size);
						stack.setNbt(nbt);
					});
				}
		);


		ServerPlayNetworking.registerGlobalReceiver(MODEL_CREATE_PACKET,
				(server, player, handler, buffer, sender) -> {
					var name = buffer.readString();
					var type = buffer.readString();
					var url = buffer.readString();

					server.execute(() -> {
						MODEL_MANAGER.addModel(player, name, type, url);
					});
				}
		);

		// Cunk shop purchasing
		ServerPlayNetworking.registerGlobalReceiver(CUNK_SHOP_PURCHASE_PACKET_ID,
				(server, player, handler, buffer, sender) -> {
					var shopId = buffer.readIdentifier();
					var entry = buffer.readInt();
					var amount = buffer.readInt();
					server.execute(() -> {
						var shop = ShopEntries.getShop(shopId);
						var currentEntry = shop.entries.get(entry);
						var cost = currentEntry.price() * amount;

						var count = CunkCoinUtils.countInventoryCoins(player.getInventory()) + CunkCoinUtils.countInventoryCoins(player.getEnderChestInventory());
						if (count < cost) {
							return;
						}

						CunkCoinUtils.removeCoins(player, cost);
						player.getInventory().markDirty();
						player.getInventory().updateItems();

						if (player.playerScreenHandler != null) {
							player.playerScreenHandler.sendContentUpdates();
							player.playerScreenHandler.syncState();
							player.playerScreenHandler.updateToClient();
						}
						if (player.currentScreenHandler != null) {
							player.currentScreenHandler.sendContentUpdates();
							player.currentScreenHandler.syncState();
							player.currentScreenHandler.updateToClient();
						}

						for (ItemStack stack : currentEntry.stacks()) {
							for (int i = 0; i < amount; i++) {
								player.getInventory().offerOrDrop(stack.copy());
							}
						}
					});
			});

		NyakoModDisc.registerAll();

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


		// Custom
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "custom"), CUSTOM_ITEM);

		// Single coin block
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "copper_coin"), COPPER_SINGLE_COIN_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "gold_coin"), GOLD_SINGLE_COIN_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "emerald_coin"), EMERALD_SINGLE_COIN_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "diamond_coin"), DIAMOND_SINGLE_COIN_BLOCK);
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "netherite_coin"), NETHERITE_SINGLE_COIN_BLOCK);

		// Coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "copper_coin"), COPPER_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "gold_coin"), GOLD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "emerald_coin"), EMERALD_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "diamond_coin"), DIAMOND_COIN_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "netherite_coin"), NETHERITE_COIN_ITEM);
		Registry.register(Registry.SOUND_EVENT, COIN_COLLECT_SOUND, COIN_COLLECT_SOUND_EVENT);

		// Bag of coins
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "bag_of_coins"), BAG_OF_COINS_ITEM);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "hungry_bag_of_coins"), HUNGRY_BAG_OF_COINS_ITEM);

		// TIAB
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "time_in_a_bottle"), TIME_IN_A_BOTTLE);

		// Soul jar
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "soul_jar"), SOUL_JAR);

		Registry.register(Registry.SOUND_EVENT, WOLVES_SOUND, WOLVES_SOUND_EVENT);

		// Test item
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "test_item"), TEST_ITEM);

		// Gacha-related
		Registry.register(Registry.BLOCK, new Identifier("nyakomod", "matter_vortex"), MATTER_VORTEX_BLOCK);
		Registry.register(Registry.ITEM, new Identifier("nyakomod", "matter_vortex"), new BlockItem(MATTER_VORTEX_BLOCK, new FabricItemSettings().group(ItemGroup.MISC)));

		registerGachaItems();

		FabricDefaultAttributeRegistry.register(PET_SPRITE, PetSpriteEntity.createPetAttributes());
		FabricDefaultAttributeRegistry.register(PET_DRAGON, PetDragonEntity.createPetAttributes());


		DispenserBlock.registerBehavior(SOUL_JAR, new ItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos pos = pointer.getPos();
				World world = pointer.getWorld();
				if (!stack.getOrCreateNbt().contains("entity")) {
					BlockPos blockPos = pointer.getPos().offset(direction);
					List<Entity> list = pointer.getWorld().getEntitiesByClass(
							Entity.class,
							new Box(blockPos), (Entityx) -> Entityx.isAlive()
					);

					Iterator var5 = list.iterator();

					Entity entity;
					ItemStack newStack;
					do {
						if (!var5.hasNext()) {
							return super.dispenseSilently(pointer, stack);
						}

						entity = (Entity) var5.next();
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

				dispenseCoins(nbt, "copper", COPPER_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "gold", GOLD_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "emerald", EMERALD_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "diamond", DIAMOND_COIN_ITEM, direction, pos, world);
				dispenseCoins(nbt, "netherite", NETHERITE_COIN_ITEM, direction, pos, world);

				stack.setNbt(nbt);

				return stack;
			}
		};

		DispenserBlock.registerBehavior(BAG_OF_COINS_ITEM, BagDispenseBehavior);
		DispenserBlock.registerBehavior(HUNGRY_BAG_OF_COINS_ITEM, BagDispenseBehavior);

		CunkCoinUtils.registerCoinAmounts();
		registerCommands();

		ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
			CachedResourcePack.setPlayerResourcePack(handler.player);
		}));
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
		});
	}
}