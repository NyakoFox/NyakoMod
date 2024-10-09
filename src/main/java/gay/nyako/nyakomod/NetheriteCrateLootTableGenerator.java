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

public class NetheriteCrateLootTableGenerator implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(NyakoLoot.NETHERITE_CRATE_LOOT_TABLE,
                LootTable.builder().pool(
                        LootPool.builder()
                                .with(ItemEntry.builder(NyakoItems.CONDENSED_MATTER_CONTAINER).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(NyakoItems.CREEPER).weight(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                                .with(ItemEntry.builder(NyakoItems.WITHER).weight(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                                .with(ItemEntry.builder(NyakoItems.TIME_IN_A_BOTTLE).weight(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                                .with(ItemEntry.builder(NyakoItems.FLINT_AND_STEEL_PLUS).weight(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(16).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(16).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.BOOK).weight(8).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(70.0f)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(NyakoItems.GOLD_COIN).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(30.0f, 60.0f))))
                                .with(ItemEntry.builder(NyakoItems.EMERALD_COIN).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6.0f, 20.0f))))
                                .rolls(ConstantLootNumberProvider.create(6))
                                .bonusRolls(ConstantLootNumberProvider.create(2))
                )
        );
    }
}
