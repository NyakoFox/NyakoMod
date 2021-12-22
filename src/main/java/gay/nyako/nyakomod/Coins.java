package gay.nyako.nyakomod;

import java.util.HashMap;
import java.util.Map;

import gay.nyako.nyakomod.NyakoMod.CoinValue;
import net.minecraft.entity.EntityType;

public class Coins {
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
		NyakoMod.coinMap.put(type, total);
	}

  public static int getCoinValue(EntityType<?> entity) {
		Integer value = NyakoMod.coinMap.get(entity);
		if (value == null) {
			System.out.println("Not in map, returning 0");
			return 0;
		}

		System.out.println("In map, returning " + value);
		return value;
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
}
