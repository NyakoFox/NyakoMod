package gay.nyako.nyakomod;

import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.nyakomod.item.BagOfCoinsItem;
import gay.nyako.nyakomod.item.CoinItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashMap;
import java.util.Map;

public class CunkCoinUtils {

    public static Map<EntityType<?>, Integer> coinMap = new HashMap<>();

    public static int getCoinValue(EntityType<?> entity) {
        Integer value = coinMap.get(entity);
        if (value == null) {
            return 0;
        }

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
        registerCoinAmount(EntityType.HOGLIN,       0, 2, 0, 0,0);
        registerCoinAmount(EntityType.ZOGLIN,       0, 2, 0, 0,0);
        registerCoinAmount(EntityType.GHAST,        0, 3, 0, 0,0);
        registerCoinAmount(EntityType.GUARDIAN,     0, 1, 0, 0,0);
        registerCoinAmount(EntityType.MAGMA_CUBE,   0, 1, 0, 0,0);
        registerCoinAmount(EntityType.PHANTOM,      0, 1, 0, 0,0);
        registerCoinAmount(EntityType.PIGLIN,       0, 1, 0, 0,0);
        registerCoinAmount(EntityType.PIGLIN_BRUTE, 0, 6, 0, 0,0);
        registerCoinAmount(EntityType.PILLAGER,     0, 1, 0, 0,0);
        registerCoinAmount(EntityType.RAVAGER, 	    0, 6, 0, 0,0);
        registerCoinAmount(EntityType.SLIME,        0, 1, 0, 0,0);
        registerCoinAmount(EntityType.VINDICATOR,   0, 2, 0, 0,0);
        registerCoinAmount(EntityType.VEX,          50, 0, 0, 0,0);
        registerCoinAmount(EntityType.SHULKER,      0, 1, 0, 0,0);
        registerCoinAmount(EntityType.SILVERFISH,   20, 0, 0, 0,0);

        registerCoinAmount(EntityType.ILLUSIONER, 0, 2, 0, 0,0);
        registerCoinAmount(EntityType.EVOKER,     0, 2, 0, 0,0);

        // Minibosses
        registerCoinAmount(EntityType.IRON_GOLEM, 0, 50, 0, 0, 0);

        // Bosses
        registerCoinAmount(EntityType.WITHER,         0, 0,  4,  0,0);
        registerCoinAmount(EntityType.ELDER_GUARDIAN, 0, 50, 0,  0,0);
        // She spawns coins herself, so don't register
        //registerCoinAmount(EntityType.ENDER_DRAGON,   0, 0,  4, 0,0);

        // Passive mobs you'd normally kill
        registerCoinAmount(EntityType.CHICKEN,   20, 0, 0, 0,0);
        registerCoinAmount(EntityType.PIG,       30, 0, 0, 0,0);
        registerCoinAmount(EntityType.SHEEP,     30, 0, 0, 0,0);
        registerCoinAmount(EntityType.RABBIT,    40, 0, 0, 0,0);
        registerCoinAmount(EntityType.COW,       40, 0, 0, 0,0);
        registerCoinAmount(EntityType.MOOSHROOM, 40, 0, 0, 0,0);

        registerCoinAmount(EntityType.SALMON,        5, 0, 0, 0,0);
        registerCoinAmount(EntityType.COD,           5, 0, 0, 0,0);
        registerCoinAmount(EntityType.TROPICAL_FISH, 5, 0, 0, 0,0);
        registerCoinAmount(EntityType.PUFFERFISH,    5, 0, 0, 0,0);

        registerCoinAmount(EntityType.SQUID, 20, 0, 0, 0,0);
        registerCoinAmount(EntityType.GLOW_SQUID, 40, 0, 0, 0,0);
        registerCoinAmount(EntityType.GIANT, 0, 50, 0, 0,0);

        // Horses and horse-likes
        registerCoinAmount(EntityType.HORSE,   20, 0, 0, 0,0);
        registerCoinAmount(EntityType.DONKEY,  20, 0, 0, 0,0);
        registerCoinAmount(EntityType.MULE,    20, 0, 0, 0,0);
        registerCoinAmount(EntityType.SKELETON_HORSE, 0, 2, 0, 0,0);
        registerCoinAmount(EntityType.ZOMBIE_HORSE,   0, 2, 0, 0,0);
        registerCoinAmount(EntityType.LLAMA,        20, 0, 0, 0,0);
        registerCoinAmount(EntityType.TRADER_LLAMA, 20, 0, 0, 0,0);


        // Passive mobs you wouldn't normally kill
        registerCoinAmount(EntityType.AXOLOTL, 25, 0, 0, 0,0);
        registerCoinAmount(EntityType.BAT,     0,  4, 0, 0,0);
        registerCoinAmount(EntityType.BEE,     50, 0, 0, 0,0);
        registerCoinAmount(EntityType.CAT,     25, 0, 0, 0,0);
        registerCoinAmount(EntityType.DOLPHIN, 50, 0, 0, 0,0);
        registerCoinAmount(EntityType.FOX,     25, 0, 0, 0,0);
        registerCoinAmount(EntityType.GOAT,    25, 0, 0, 0,0);
        registerCoinAmount(EntityType.OCELOT,  25, 0, 0, 0,0);
        registerCoinAmount(EntityType.PANDA,   25, 0, 0, 0,0);
        registerCoinAmount(EntityType.PARROT,  25, 0, 0, 0,0);
        registerCoinAmount(EntityType.POLAR_BEAR, 50, 1, 0, 0,0);
        registerCoinAmount(EntityType.SNOW_GOLEM, 25, 0, 0, 0,0);

        registerCoinAmount(EntityType.STRIDER, 60, 0, 0, 0,0);

        registerCoinAmount(EntityType.TURTLE, 40, 0, 0, 0,0);
        registerCoinAmount(EntityType.VILLAGER, 60, 0, 0, 0,0);
        registerCoinAmount(EntityType.WANDERING_TRADER, 60, 0, 0, 0,0);
        registerCoinAmount(EntityType.WOLF, 25, 0, 0, 0,0);

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
        Map<CoinValue, Integer> map = valueToSplit((int) amount);

        Integer copper = map.get(CoinValue.COPPER);
        Integer gold = map.get(CoinValue.GOLD);
        Integer emerald = map.get(CoinValue.EMERALD);
        Integer diamond = map.get(CoinValue.DIAMOND);
        Integer netherite = map.get(CoinValue.NETHERITE);

        if (copper > 0) {
            ItemStack stack = new ItemStack(NyakoMod.COPPER_COIN_ITEM);
            stack.setCount(copper);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (gold > 0) {
            ItemStack stack = new ItemStack(NyakoMod.GOLD_COIN_ITEM);
            stack.setCount(gold);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (emerald > 0) {
            ItemStack stack = new ItemStack(NyakoMod.EMERALD_COIN_ITEM);
            stack.setCount(emerald);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (diamond > 0) {
            ItemStack stack = new ItemStack(NyakoMod.DIAMOND_COIN_ITEM);
            stack.setCount(diamond);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (netherite > 0) {
            ItemStack stack = new ItemStack(NyakoMod.NETHERITE_COIN_ITEM);
            stack.setCount(netherite);
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

                Map<CoinValue, Integer> map = valueToSplit((int) bagAmount);

                tag.putInt("copper",    map.get(CoinValue.COPPER));
                tag.putInt("gold",      map.get(CoinValue.GOLD));
                tag.putInt("emerald",   map.get(CoinValue.EMERALD));
                tag.putInt("diamond",   map.get(CoinValue.DIAMOND));
                tag.putInt("netherite", map.get(CoinValue.NETHERITE));

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

    public static ItemStack getHungryBag(PlayerEntity player) {
        var inventory = player.getInventory();
        for (int i = 0; i < inventory.size(); ++i) {
            var stack = inventory.getStack(i);
            if (stack.isOf(NyakoMod.HUNGRY_BAG_OF_COINS_ITEM)) {
                NbtCompound tag = stack.getOrCreateNbt();
                if (!tag.getBoolean("using")) {
                    return stack;
                }
            }
        }

        var optional_component = TrinketsApi.getTrinketComponent(player);
        if (optional_component.isPresent()) {
            var component = optional_component.get();
            var bags = component.getEquipped(NyakoMod.HUNGRY_BAG_OF_COINS_ITEM);
            for (int i = 0; i < bags.size(); ++i) {
                var stack = bags.get(i).getRight();
                if (stack.isOf(NyakoMod.HUNGRY_BAG_OF_COINS_ITEM)) {
                    NbtCompound tag = stack.getOrCreateNbt();
                    if (!tag.getBoolean("using")) {
                        return stack;
                    }
                }
            }
        }

        return null;
    }
}
