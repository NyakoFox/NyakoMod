package gay.nyako.nyakomod;

import gay.nyako.nyakomod.entity.PetDragonEntity;
import gay.nyako.nyakomod.item.*;
import gay.nyako.nyakomod.item.gacha.DiscordGachaItem;
import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;

public class NyakoModItem {
    public static final Item DRIP_JACKET = new ArmorItem(NyakoMod.customArmorMaterial, EquipmentSlot.CHEST, new Item.Settings().group(ItemGroup.COMBAT).fireproof());
    // Staff of Smiting
    public static final Item STAFF_OF_SMITING_ITEM = new StaffOfSmitingItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());
    // Present
    public static final Item PRESENT_ITEM = new PresentItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    // Custom
    public static final Item CUSTOM_ITEM = new CustomItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(69));
    // /dev/null
    public static final Item DEV_NULL_ITEM = new DevNullItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item RETENTIVE_BALL_ITEM = new RetentiveBallItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final BlockItem BLUEPRINT_WORKBENCH_ITEM = new BlockItem(NyakoMod.BLUEPRINT_WORKBENCH, new Item.Settings().group(ItemGroup.MISC));
    public static final Item COPPER_COIN_ITEM            = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoMod.COPPER_SINGLE_COIN_BLOCK, "copper", 1);
    public static final Item GOLD_COIN_ITEM              = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoMod.GOLD_SINGLE_COIN_BLOCK, "gold", 100);
    public static final Item EMERALD_COIN_ITEM           = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoMod.EMERALD_SINGLE_COIN_BLOCK, "emerald", 10000);
    public static final Item DIAMOND_COIN_ITEM           = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100), NyakoMod.DIAMOND_SINGLE_COIN_BLOCK, "diamond", 1000000);
    public static final Item NETHERITE_COIN_ITEM         = new CoinItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(100).fireproof(), NyakoMod.NETHERITE_SINGLE_COIN_BLOCK, "netherite", 100000000);
    // Bag of coins
    public static final Item BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item HUNGRY_BAG_OF_COINS_ITEM = new BagOfCoinsItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    // Time in a bottle
    public static final TimeInABottleItem TIME_IN_A_BOTTLE = new TimeInABottleItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    // Soul jar
    public static final SoulJarItem SOUL_JAR = new SoulJarItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    // Test item
    public static final Item TEST_ITEM = new TestItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));
    public static final Item BLUEPRINT = new BlueprintItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(16));
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
    public static final Item PET_SPRITE_SUMMON_ITEM = new PetSpriteSummonItem(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof());
    public static final Item PET_DRAGON_SUMMON_ITEM = new PetSummonItem<>(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1).fireproof(), NyakoMod.PET_DRAGON, PetDragonEntity::createPet);

    public static final Item PIAMOND_DICKAXE = new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

    public static final Item TWO_TALL = new Item(new FabricItemSettings().group(ItemGroup.MISC).maxCount(1));

    public static final Item NETHER_PORTAL = new BlockItem(Blocks.NETHER_PORTAL, new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item FIRE          = new BlockItem(Blocks.FIRE,          new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item SOUL_FIRE     = new BlockItem(Blocks.SOUL_FIRE,     new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item WATER         = new BlockItem(Blocks.WATER,         new FabricItemSettings().group(ItemGroup.MISC));
    public static final Item LAVA          = new BlockItem(Blocks.LAVA,          new FabricItemSettings().group(ItemGroup.MISC));

}
