package gay.nyako.nyakomod;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class NyakoAdvancementsGenerator implements Consumer<Consumer<AdvancementEntry>> {

    @Override
    public void accept(Consumer<AdvancementEntry> consumer) {

        AdvancementEntry parentAdvancement = Advancement.Builder.create()
                .display(NyakoItems.EMERALD_COIN, Text.literal("Cunkin' it up"),
                        Text.translatable("Get your first coin"),
                        new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.COPPER_COIN))
                .build(consumer, "nyakomod:nyakomod/root");

        AdvancementEntry firstMilkAdvancement = Advancement.Builder.create()
                .display(Items.BONE, Text.literal("Bone Generator"),
                        Text.translatable("Drink a refreshing bucket of milk"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(parentAdvancement)
                .criterion("consume_item", ConsumeItemCriterion.Conditions.item(Items.MILK_BUCKET))
                .build(consumer, "nyakomod:nyakomod/drank_milk");

        AdvancementEntry getMilkedAdvancement = Advancement.Builder.create()
                .display(Items.MILK_BUCKET, Text.literal("Get milked bozo"),
                        Text.translatable("Get milked by another player"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(firstMilkAdvancement)
                .criterion("nyakomod:player_milked", PlayerMilkCriterion.Conditions.any())
                .build(consumer, "nyakomod:nyakomod/player_milked");

        AdvancementEntry milkHorseAdvancement = Advancement.Builder.create()
                .display(NyakoItems.HORSE_MILK_BUCKET, Text.literal("BRO?????"),
                        Text.translatable("Milk a horse??? What the fuck?????"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        true
                )
                .parent(getMilkedAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.HORSE_MILK_BUCKET))
                .build(consumer, "nyakomod:nyakomod/horse_milked");

        AdvancementEntry milkDragonAdvancement = Advancement.Builder.create()
                .display(NyakoItems.DRAGON_MILK_BUCKET, Text.literal("Mommy Milked"),
                        Text.translatable("Milk the Jender Jragon"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        true
                )
                .parent(milkHorseAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.DRAGON_MILK_BUCKET))
                .build(consumer, "nyakomod:nyakomod/dragon_milked");

        AdvancementEntry diamondCoinAdvancement = Advancement.Builder.create()
                .display(NyakoItems.DIAMOND_COIN, Text.literal("Moving Up"),
                        Text.translatable("Obtain your first Diamond Coin"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(parentAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.DIAMOND_COIN))
                .build(consumer, "nyakomod:nyakomod/diamond_coin");

        AdvancementEntry netheriteCoinAdvancement = Advancement.Builder.create()
                .display(NyakoItems.NETHERITE_COIN, Text.literal("You're Rich"),
                        Text.translatable("Obtain your first Netherite Coin"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(diamondCoinAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.NETHERITE_COIN))
                .build(consumer, "nyakomod:nyakomod/netherite_coin");

        AdvancementEntry enderGemAdvancement = Advancement.Builder.create()
                .display(NyakoItems.ENDER_GEM, Text.literal("From The Ancients"),
                        Text.translatable("Find an Ender Gem."),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(parentAdvancement)
                .criterion("consume_item", ConsumeItemCriterion.Conditions.item(NyakoItems.ENDER_GEM))
                .build(consumer, "nyakomod:nyakomod/ender_gem");
    }
}