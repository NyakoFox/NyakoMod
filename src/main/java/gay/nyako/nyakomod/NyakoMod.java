package gay.nyako.nyakomod;

import net.fabricmc.api.ModInitializer;
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
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import net.minecraft.item.Items;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;

import java.util.HashMap;
import java.util.Map;

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


	@Override
	public void onInitialize() {
		System.out.println("owo");

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

		registerCoinLootTables();

	}
	public void registerCoinLootTables() {

		Map<net.minecraft.util.Identifier, Integer> map = new HashMap<>();
		registerCoinAmount(map, EntityType.ZOMBIE, 60, 0, 0, 0,0);
		registerCoinAmount(map, EntityType.SKELETON, 99, 0, 0, 0,0);
		registerCoinAmount(map, EntityType.CREEPER, 50, 1, 0, 0,0);
		registerCoinAmount(map, EntityType.ENDERMAN, 50, 2, 0, 0,0);


		LootTableLoadingCallback.EVENT.register((resourceManager, manager, id, supplier, setter) -> {

			Integer value = map.get(id);
			if (value == null) return;

			int copper =    value % 1000;
			int gold =      value / 1000;
			int emerald =   value / (int) Math.pow(1000, 2);
			int diamond =   value / (int) Math.pow(1000, 3);
			int netherite = value / (int) Math.pow(1000, 4);

			LootPool lootCopper = createCoinPool(copper, NyakoMod.COPPER_COIN_ITEM);
			LootPool lootGold = createCoinPool(gold, NyakoMod.GOLD_COIN_ITEM);
			LootPool lootEmerald = createCoinPool(emerald, NyakoMod.EMERALD_COIN_ITEM);
			LootPool lootDiamond = createCoinPool(diamond, NyakoMod.DIAMOND_COIN_ITEM);
			LootPool lootNetherite = createCoinPool(netherite, NyakoMod.NETHERITE_COIN_ITEM);

			supplier.withPool(lootCopper)
					.withPool(lootGold)
					.withPool(lootEmerald)
					.withPool(lootDiamond)
					.withPool(lootNetherite);
		});
	}

	public LootPool createCoinPool(int amount, Item item) {
		return FabricLootPoolBuilder.builder()
				.with(ItemEntry.builder(item))
				.rolls(UniformLootNumberProvider.create(amount / 2, amount)).build();
	};

	public void registerCoinAmount(Map map, EntityType type, int copper, int gold, int emerald, int diamond, int netherite) {
		int total = copper
				+ (gold * 1000)
				+ (int) (emerald * Math.pow(1000, 2))
				+ (int) (diamond * Math.pow(1000, 3))
				+ (int) (netherite * Math.pow(1000, 4));
		map.put(type.getLootTableId(), total);
	}
}