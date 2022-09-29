package gay.nyako.nyakomod;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementFrame;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class NyakoAdvancementsGenerator implements Consumer<Consumer<Advancement>> {

    @Override
    public void accept(Consumer<Advancement> consumer) {
        // This is our advancement
        // The difference:

        Advancement parentAdvancement = Advancement.Builder.create()
                .display(NyakoItems.DIAMOND_COIN_ITEM, Text.literal("Cunkin' it up"),
                        Text.translatable("Get your first coin"),
                        new Identifier("textures/gui/advancements/backgrounds/husbandry.png"),
                        AdvancementFrame.TASK,
                        // whether to show a toast that appears on the top-right corner
                        true,
                        // whether to announce to chat
                        true,
                        // whether its hidden in the advancement tab
                        false)
                .criterion("inventory_changed", InventoryChangedCriterion.Conditions.items(NyakoItems.COPPER_COIN_ITEM))
                .build(consumer, "nyakomod/root");

        Advancement exampleBlockAdvancement = Advancement.Builder.create()
                .display(Items.MILK_BUCKET, Text.literal("Get milked bozo"),
                        Text.translatable("Get milked by another player"),
                        null,
                        AdvancementFrame.TASK,
                        // whether to show a toast that appears on the top-right corner
                        true,
                        // whether to announce to chat
                        true,
                        // whether its hidden in the advancement tab
                        false
                )
                .parent(parentAdvancement)
                .criterion("player_milked", PlayerMilkCriterion.Conditions.any())
                .build(consumer, "nyakomod/player_milked");
    }
}