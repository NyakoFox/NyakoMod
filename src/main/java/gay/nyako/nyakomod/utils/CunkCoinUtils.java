package gay.nyako.nyakomod.utils;

import dev.emi.trinkets.api.TrinketsApi;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.data.CunkCoinData;
import gay.nyako.nyakomod.data.CunkCoinDataValues;
import gay.nyako.nyakomod.item.BagOfCoinsItem;
import gay.nyako.nyakomod.item.CoinItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CunkCoinUtils {
    public static CunkCoinData getCoinData(EntityType<?> entity) {
        Identifier id = Registries.ENTITY_TYPE.getId(entity);
        return CunkCoinDataValues.get(id);
    }

    public static Map<CoinValue, Integer> valueToSplit(long total) {
        Map<CoinValue, Integer> splitMap = new HashMap<>();
        splitMap.put(CoinValue.COPPER,    Math.toIntExact(total % 100));
        splitMap.put(CoinValue.GOLD,      Math.toIntExact((total / 100) % 100));
        splitMap.put(CoinValue.EMERALD,   Math.toIntExact((total / (int) Math.pow(100, 2)) % 100));
        splitMap.put(CoinValue.DIAMOND,   Math.toIntExact((total / (int) Math.pow(100, 3)) % 100));
        splitMap.put(CoinValue.NETHERITE, Math.toIntExact((total / (int) Math.pow(100, 4))));
        return splitMap;
    }

    public static void giveCoins(PlayerEntity player, long amount) {
        giveCoins(player.getInventory(), amount);
    }

    public static void giveCoins(Inventory inventory, long amount) {
        Map<CoinValue, Integer> map = valueToSplit(amount);

        Integer copper = map.get(CoinValue.COPPER);
        Integer gold = map.get(CoinValue.GOLD);
        Integer emerald = map.get(CoinValue.EMERALD);
        Integer diamond = map.get(CoinValue.DIAMOND);
        Integer netherite = map.get(CoinValue.NETHERITE);

        if (copper > 0) {
            ItemStack stack = new ItemStack(NyakoItems.COPPER_COIN);
            stack.setCount(copper);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (gold > 0) {
            ItemStack stack = new ItemStack(NyakoItems.GOLD_COIN);
            stack.setCount(gold);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (emerald > 0) {
            ItemStack stack = new ItemStack(NyakoItems.EMERALD_COIN);
            stack.setCount(emerald);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (diamond > 0) {
            ItemStack stack = new ItemStack(NyakoItems.DIAMOND_COIN);
            stack.setCount(diamond);
            if (inventory instanceof SimpleInventory) {
                ((SimpleInventory) inventory).addStack(stack);
            } else if (inventory instanceof PlayerInventory) {
                ((PlayerInventory) inventory).insertStack(stack);
            }
        }
        if (netherite > 0) {
            ItemStack stack = new ItemStack(NyakoItems.NETHERITE_COIN);
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
        removed  = removeCoinsFromTrinketBag(player, amount, removed);
        removed += removeCoinsFromInventory(player.getEnderChestInventory(), amount, removed);
    }

    public static long removeCoinsFromTrinketBag(PlayerEntity player, long amount, long removed) {
        var trinketBag = getTrinketCoinBag(player);
        if (trinketBag != null) {
            removed = removeCoinsFromBag(trinketBag, amount, removed);
        }
        return removed;
    }

    public static long removeCoinsFromBag(ItemStack stack, long amount, long removed) {
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
        return removed;
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
                removed = removeCoinsFromBag(stack, amount, removed);
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

    public static long countInventoryCoins(Inventory inventory) {
        long total = 0;
        for (int i = 0; i < inventory.size(); ++i) {
            var stack = inventory.getStack(i);
            var item = stack.getItem();

            if (item instanceof CoinItem) {
                total += (long) stack.getCount() * ((CoinItem) item).getCoinValue();
            } else if (item instanceof BagOfCoinsItem) {
                NbtCompound tag = stack.getOrCreateNbt();
                total += tag.getInt("copper");
                total += tag.getInt("gold") * 100L;
                total += tag.getInt("emerald") * 10000L;
                total += tag.getInt("diamond") * 1000000L;
                total += tag.getInt("netherite") * 100000000L;
            }
        }

        if (inventory instanceof PlayerInventory) {
            var trinketBag = getTrinketCoinBag(((PlayerInventory) inventory).player);
            if (trinketBag != null) {
                NbtCompound tag = trinketBag.getOrCreateNbt();
                total += tag.getInt("copper");
                total += tag.getInt("gold") * 100L;
                total += tag.getInt("emerald") * 10000L;
                total += tag.getInt("diamond") * 1000000L;
                total += tag.getInt("netherite") * 100000000L;
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
            if (stack.isOf(NyakoItems.HUNGRY_BAG_OF_COINS)) {
                NbtCompound tag = stack.getOrCreateNbt();
                if (!tag.getBoolean("using")) {
                    return stack;
                }
            }
        }

        var trinketBag = getTrinketCoinBag(player);
        if (trinketBag != null && trinketBag.isOf(NyakoItems.HUNGRY_BAG_OF_COINS)) {
            NbtCompound tag = trinketBag.getOrCreateNbt();
            if (!tag.getBoolean("using")) {
                return trinketBag;
            }
        }

        return null;
    }

    public static ItemStack getTrinketCoinBag(PlayerEntity player) {
        var optionalComponent = TrinketsApi.getTrinketComponent(player);
        if (optionalComponent.isPresent()) {
            var component = optionalComponent.get();
            var bags1 = component.getEquipped(NyakoItems.BAG_OF_COINS);
            var bags2 = component.getEquipped(NyakoItems.HUNGRY_BAG_OF_COINS);
            var bags = Stream.concat(bags1.stream(), bags2.stream()).toList();

            for (int i = 0; i < bags.size(); ++i) {
                var stack = bags.get(i).getRight();
                if (stack.isOf(NyakoItems.HUNGRY_BAG_OF_COINS) || stack.isOf(NyakoItems.BAG_OF_COINS)) {
                    return stack;
                }
            }
        }
        return null;
    }
}

