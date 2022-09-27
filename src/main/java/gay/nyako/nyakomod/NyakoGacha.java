package gay.nyako.nyakomod;

import gay.nyako.nyakomod.item.PresentItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class NyakoGacha {
    public static List<GachaEntry> GACHA_ENTRIES = new ArrayList<>();

    public static void register() {
        /* 1 STAR */
        // 1 Gold CunkCoin
        registerGachaItem(Text.of("1 §6Gold CunkCoin™"), new ItemStack(NyakoItems.GOLD_COIN_ITEM), 1);
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
        registerGachaItem(Text.of("a §bMusic Disc"), new ItemStack(NyakoDiscs.WOLVES.item()), 2);
        registerGachaItem(Text.of("16 §bSquishy Diamonds"), (GachaItem) NyakoItems.DIAMOND_GACHA_ITEM, 16);
        registerGachaItem(Text.of("32 §6Cookies"), Items.COOKIE, 32, 2);

        registerGachaItem(Text.of("32 §7Sticks"), Items.STICK, 32, 2);
        registerGachaItem(Text.of("a §7Fishing Rod"), Items.FISHING_ROD, 1, 2);
        registerGachaItem(Text.of("2 §dCow Spawn Eggs"), Items.COW_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dPig Spawn Eggs"), Items.PIG_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dSheep Spawn Eggs"), Items.SHEEP_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dRabbit Spawn Eggs"), Items.RABBIT_SPAWN_EGG, 2, 2);
        registerGachaItem(Text.of("2 §dChicken Spawn Eggs"), Items.CHICKEN_SPAWN_EGG, 2, 2);

        /* 3 STAR */
        registerGachaItem(Text.of("the §9Discord Logo"), (GachaItem) NyakoItems.DISCORD_GACHA_ITEM);
        registerGachaItem(Text.of("a §2Potion of Luck"), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.LUCK), 3);
        registerGachaItem(Text.of("a §2Potion of Unluck"), PotionUtil.setPotion(new ItemStack(Items.POTION), NyakoPotions.UNLUCK), 3);
        registerGachaItem(Text.of("5 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN_ITEM, 5, 3);
        registerGachaItem(Text.of("64 §7Cobblestone"), Items.COBBLESTONE, 64, 3);
        registerGachaItem(Text.of("64 §cTorches"), Items.TORCH, 64, 3);
        registerGachaItem(Text.of("16 §7Iron Ingots"), Items.IRON_INGOT, 16, 3);
        registerGachaItem(Text.of("16 §aExperience Bottles"), Items.EXPERIENCE_BOTTLE, 16, 3);
        registerGachaItem(Text.of("16 §dItem Frames"), Items.ITEM_FRAME, 16, 3);
        registerGachaItem(Text.of("16 §7Arrows"), Items.ARROW, 16, 3);

        // Mario and Luigi
        ItemStack brotherStack = new ItemStack(NyakoItems.PRESENT_ITEM);
        PresentItem.addToPresent(brotherStack, new ItemStack(NyakoItems.MARIO_GACHA_ITEM));
        PresentItem.addToPresent(brotherStack, new ItemStack(NyakoItems.LUIGI_GACHA_ITEM));
        brotherStack.setCustomName(Text.of("Present (Brothers)"));
        registerGachaItem(Text.of("§cThe §4Brothers"), brotherStack, 3);

        /* 4 STAR */
        registerGachaItem(Text.of("10 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN_ITEM, 10, 4);
        registerGachaItem(Text.of("the §5Staff of Vorbulation"), (GachaItem) NyakoItems.STAFF_OF_VORBULATION_ITEM);
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
        registerGachaItem(Text.of("20 §6Gold CunkCoin™"), NyakoItems.GOLD_COIN_ITEM, 20, 5);
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
        GachaEntry gachaEntry = new GachaEntry(
                name, itemStack, rarity, (1d / (rarity * 2d))
        );
        GACHA_ENTRIES.add(gachaEntry);
    }

    public record GachaEntry(
            Text name,
            ItemStack itemStack,
            int rarity,
            double weight
    ) {}
}
