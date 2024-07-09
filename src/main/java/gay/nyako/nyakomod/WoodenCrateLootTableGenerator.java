package gay.nyako.nyakomod;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class WoodenCrateLootTableGenerator implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(NyakoLoot.WOODEN_CRATE_LOOT_TABLE,
                LootTable.builder().pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.COD).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.SALMON).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.TROPICAL_FISH).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.PUFFERFISH).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.BONE).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.NAME_TAG).weight(4))
                                .with(ItemEntry.builder(Items.SADDLE).weight(4))
                                .with(ItemEntry.builder(Items.BOW).weight(4).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f)))).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())
                                .with(ItemEntry.builder(Items.FISHING_ROD).weight(4).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.0f, 0.25f)))).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())
                                .with(ItemEntry.builder(Items.BOOK).weight(4).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(Items.NAUTILUS_SHELL).weight(4))
                                .with(ItemEntry.builder(Items.HEART_OF_THE_SEA).weight(1))
                                .with(ItemEntry.builder(NyakoItems.COPPER_COIN).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.GOLD_COIN).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .rolls(ConstantLootNumberProvider.create(6))
                                .bonusRolls(ConstantLootNumberProvider.create(2))
                )
        );
    }
}
