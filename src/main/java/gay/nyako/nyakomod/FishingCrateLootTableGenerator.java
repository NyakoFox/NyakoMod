package gay.nyako.nyakomod;

import net.minecraft.data.server.loottable.LootTableGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class FishingCrateLootTableGenerator implements LootTableGenerator {
    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        exporter.accept(NyakoLoot.CRATE_LOOT_TABLE,
                LootTable.builder()
                        .pool(LootPool.builder()
                                .with(ItemEntry.builder(NyakoItems.WOODEN_CRATE).weight(12))
                                .with(ItemEntry.builder(NyakoItems.IRON_CRATE).weight(8))
                                .with(ItemEntry.builder(NyakoItems.GOLDEN_CRATE).weight(6))
                                .with(ItemEntry.builder(NyakoItems.DIAMOND_CRATE).weight(4))
                                .with(ItemEntry.builder(NyakoItems.NETHERITE_CRATE).weight(1))
                        )
        );
    }
}
