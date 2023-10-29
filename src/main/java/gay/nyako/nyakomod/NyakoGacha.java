package gay.nyako.nyakomod;

import gay.nyako.nyakomod.item.PresentItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public class NyakoGacha {
    public static List<GachaEntry> GACHA_ENTRIES = new ArrayList<>();

    public static void register() {
        /* 1-STAR */
        // 1 Gold CunkCoin
        registerGachaItem(Text.of("1 §6Gold CunkCoin™"), new ItemStack(NyakoItems.GOLD_COIN), 1);
        registerGachaItem(Text.of("32 §6Dirt"), Items.DIRT, 32, 1);
        registerGachaItem(Text.of("16 §6Cobblestone"), Items.COBBLESTONE, 16, 1);
        registerGachaItem(Text.of("16 §6Andesite"), Items.ANDESITE, 16, 1);
        registerGachaItem(Text.of("16 §6Diorite"), Items.DIORITE, 16, 1);
        registerGachaItem(Text.of("16 §6Granite"), Items.GRANITE, 16, 1);
        registerGachaItem(Text.of("16 §6Grass Blocks"), Items.GRASS_BLOCK, 16, 1);
        registerGachaItem(Text.of("32 §bGlass"), Items.GLASS, 32, 1);

        /* 2-STAR */
        registerGachaItem(Text.of("16 §bSquishy Diamonds"), (GachaItem) NyakoItems.DIAMOND_GACHA, 16);
        registerGachaItem(Text.of("32 §6Cookies"), Items.COOKIE, 32, 2);

        registerGachaItem(Text.of("2 §dCow Spawn Eggs"), Items.COW_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dPig Spawn Eggs"), Items.PIG_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dSheep Spawn Eggs"), Items.SHEEP_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dRabbit Spawn Eggs"), Items.RABBIT_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dChicken Spawn Eggs"), Items.CHICKEN_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("a §7Bow"), new ItemStack(Items.BOW), 2, true);
        registerGachaItem(Text.of("a §7Fishing Rod"), new ItemStack(Items.FISHING_ROD), 2, true);

        /* 3-STAR */
        registerGachaItem(Text.of("the §9Discord Logo"), (GachaItem) NyakoItems.DISCORD_GACHA);
        registerGachaItem(Text.of("a §2Potion of Luck"), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LUCK), 3);
        registerGachaItem(Text.of("a §2Potion of Unluck"), PotionUtil.setPotion(new ItemStack(Items.POTION), NyakoPotions.UNLUCK), 3);
        registerGachaItem(Text.of("5 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN, 5, 3);
        registerGachaItem(Text.of("16 §7Iron Ingots"), Items.IRON_INGOT, 16, 3);
        registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 3);
        registerGachaItem(Text.of("a §6Magnet"), NyakoItems.MAGNET, 1, 3);
        registerGachaItem(Text.of("an §6Iron Axe"), new ItemStack(Items.IRON_AXE), 3, true);
        registerGachaItem(Text.of("an §6Iron Pickaxe"), new ItemStack(Items.IRON_PICKAXE), 3, true);
        registerGachaItem(Text.of("an §6Iron Shovel"), new ItemStack(Items.IRON_SHOVEL), 3, true);

        // Mario and Luigi
        ItemStack brotherStack = new ItemStack(NyakoItems.PRESENT);
        PresentItem.addToPresent(brotherStack, new ItemStack(NyakoItems.MARIO_GACHA));
        PresentItem.addToPresent(brotherStack, new ItemStack(NyakoItems.LUIGI_GACHA));
        brotherStack.setCustomName(Text.of("Present (Brothers)"));
        registerGachaItem(Text.of("§cThe §4Brothers"), brotherStack, 3);

        /* 4-STAR */
        registerGachaItem(Text.of("10 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN, 10, 4);
        registerGachaItem(Text.of("the §5Staff of Vorbulation"), (GachaItem) NyakoItems.STAFF_OF_VORBULATION);
        registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 4);
        registerGachaItem(Text.of("16 §eGlowing Item Frames"), Items.GLOW_ITEM_FRAME, 16, 4);
        registerGachaItem(Text.of("32 §eSpectral Arrows"), Items.SPECTRAL_ARROW, 32, 4);
        registerGachaItem(Text.of("1 §dVillager Spawn Egg"), Items.VILLAGER_SPAWN_EGG, 1, 4);
        registerGachaItem(Text.of("4 §dWandering Trader Spawn Eggs"), Items.WANDERING_TRADER_SPAWN_EGG, 4, 4);
        registerGachaItem(Text.of("16 §cTNT"), Items.TNT, 16, 4);
        registerGachaItem(Text.of("16 §5Dragon's Breath"), Items.DRAGON_BREATH, 16, 4);
        registerGachaItem(Text.of("4 §6Sponges"), Items.SPONGE, 4, 4);
        registerGachaItem(Text.of("an §6Iron Sword"), new ItemStack(Items.IRON_SWORD), 4, true);
        registerGachaItem(Text.of("an §6Iron Helmet"), new ItemStack(Items.IRON_HELMET), 4, true);
        registerGachaItem(Text.of("an §6Iron Chestplate"), new ItemStack(Items.IRON_CHESTPLATE), 4, true);
        registerGachaItem(Text.of("§6Iron Leggings"), new ItemStack(Items.IRON_LEGGINGS), 4, true);
        registerGachaItem(Text.of("§6Iron Boots"), new ItemStack(Items.IRON_BOOTS), 4, true);

        /* 5-STAR */
        registerGachaItem(Text.of("a §dDragon §5Egg"), Items.DRAGON_EGG, 1, 5);
        registerGachaItem(Text.of("20 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN, 20, 5);
        registerGachaItem(Text.of("1 §4Ancient Debris"), Items.ANCIENT_DEBRIS, 1, 5);
        registerGachaItem(Text.of("8 §bDiamonds"), Items.DIAMOND, 8, 5);
        registerGachaItem(Text.of("a §bSoul Jar"), NyakoItems.SOUL_JAR, 1, 5);
        registerGachaItem(Text.of("a §bRetentive Ball"), NyakoItems.RETENTIVE_BALL, 1, 5);
        registerGachaItem(Text.of("a §bTime In A Bottle"), NyakoItems.TIME_IN_A_BOTTLE, 1, 5);
        registerGachaItem(Text.of("a §6Diamond Axe"), new ItemStack(Items.DIAMOND_AXE), 5, true);
        registerGachaItem(Text.of("a §6Diamond Pickaxe"), new ItemStack(Items.DIAMOND_PICKAXE), 5, true);
        registerGachaItem(Text.of("a §6Diamond Shovel"), new ItemStack(Items.DIAMOND_SHOVEL), 5, true);
        registerGachaItem(Text.of("a Burning Super Death Sword"), NyakoItems.BURNING_SUPER_DEATH_SWORD, 1, 5);
        registerGachaItem(Text.of("a §6Launcher"), NyakoItems.LAUNCHER, 1, 5);
    }

    public static void registerGachaItem(Text name, GachaItem item) {
        registerGachaItem(name, item, 1);
    }

    public static void registerGachaItem(Text name, GachaItem item, int amount) {
        int rarity = item.getRarity();
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCount(amount);
        registerGachaItem(name, itemStack, rarity);
    }

    public static void registerGachaItem(Text name, Item item, int amount, int rarity) {
        ItemStack itemStack = new ItemStack(item);
        itemStack.setCount(amount);
        registerGachaItem(name, itemStack, rarity);
    }

    public static void registerGachaItem(Text name, ItemStack itemStack, int rarity) {
        registerGachaItem(name, itemStack, rarity, false);
    }

    public static void registerGachaItem(Text name, ItemStack itemStack, int rarity, boolean enchantable) {
        GachaEntry gachaEntry = new GachaEntry(
                name, itemStack, rarity, (5 - rarity) + 1, enchantable
        );
        GACHA_ENTRIES.add(gachaEntry);
    }

    public record GachaEntry(
            Text name,
            ItemStack itemStack,
            int rarity,
            double weight,
            boolean enchantable
    ) {}
}
