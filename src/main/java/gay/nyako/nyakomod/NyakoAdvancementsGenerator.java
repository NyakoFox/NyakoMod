package gay.nyako.nyakomod;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.ConsumeItemCriterion;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class NyakoAdvancementsGenerator implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> consumer) {

        Advancement parentAdvancement = Advancement.Builder.create()
                .display(NyakoItems.EMERALD_COIN_ITEM, Text.literal("Cunkin' it up"),
                        Text.translatable("Get your first coin"),
                        new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.COPPER_COIN_ITEM))
                .build(consumer, "nyakomod/root");

        Advancement firstMilkAdvancement = Advancement.Builder.create()
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
                .build(consumer, "nyakomod/drank_milk");

        Advancement getMilkedAdvancement = Advancement.Builder.create()
                .display(Items.MILK_BUCKET, Text.literal("Get milked bozo"),
                        Text.translatable("Get milked by another player"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(firstMilkAdvancement)
                .criterion("player_milked", PlayerMilkCriterion.Conditions.any())
                .build(consumer, "nyakomod/player_milked");


        Advancement diamondCoinAdvancement = Advancement.Builder.create()
                .display(NyakoItems.DIAMOND_COIN_ITEM, Text.literal("Moving Up"),
                        Text.translatable("Obtain your first Diamond Coin"),
                        null,
                        AdvancementFrame.TASK,
                        true,
                        true,
                        false
                )
                .parent(parentAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.DIAMOND_COIN_ITEM))
                .build(consumer, "nyakomod/diamond_coin");

        Advancement netheriteCoinAdvancement = Advancement.Builder.create()
                .display(NyakoItems.NETHERITE_COIN_ITEM, Text.literal("You're Rich"),
                        Text.translatable("Obtain your first Netherite Coin"),
                        null,
                        AdvancementFrame.GOAL,
                        true,
                        true,
                        false
                )
                .parent(diamondCoinAdvancement)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.NETHERITE_COIN_ITEM))
                .build(consumer, "nyakomod/netherite_coin");
    }
}