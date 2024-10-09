package gay.nyako.nyakomod;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetDamageLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class DiamondCrateLootTableGenerator implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(NyakoLoot.DIAMOND_CRATE_LOOT_TABLE,
                LootTable.builder().pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(Items.DIAMOND).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.COD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.SALMON).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 6.0f))))
                                .with(ItemEntry.builder(Items.TROPICAL_FISH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.PUFFERFISH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.BOW).weight(3).apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.25f, 0.50f)))).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0f)).allowTreasureEnchantments())
                                .with(ItemEntry.builder(Items.BOOK).weight(12).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(70.0f)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(NyakoItems.COPPER_COIN).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.GOLD_COIN).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.EMERALD_COIN).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .rolls(ConstantLootNumberProvider.create(6))
                                .bonusRolls(ConstantLootNumberProvider.create(2))
                )
        );
    }
}
