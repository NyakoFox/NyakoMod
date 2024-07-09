package gay.nyako.nyakomod;

import net.minecraft.data.DataOutput;
import net.minecraft.data.server.loottable.LootTableProvider;
import net.minecraft.loot.context.LootContextTypes;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NyakoLootTableProvider extends LootTableProvider {
    public NyakoLootTableProvider(DataOutput output) {
        super(output,
                new HashSet<>(Arrays.asList(
                        NyakoLoot.CRATE_LOOT_TABLE,
                        NyakoLoot.WOODEN_CRATE_LOOT_TABLE,
                        NyakoLoot.IRON_CRATE_LOOT_TABLE,
                        NyakoLoot.GOLDEN_CRATE_LOOT_TABLE,
                        NyakoLoot.DIAMOND_CRATE_LOOT_TABLE,
                        NyakoLoot.NETHERITE_CRATE_LOOT_TABLE
                )),
                List.of(
                        new LootTableProvider.LootTypeGenerator(FishingCrateLootTableGenerator::new, LootContextTypes.FISHING),
                        new LootTableProvider.LootTypeGenerator(WoodenCrateLootTableGenerator::new, LootContextTypes.CHEST),
                        new LootTableProvider.LootTypeGenerator(IronCrateLootTableGenerator::new, LootContextTypes.ENTITY),
                        new LootTableProvider.LootTypeGenerator(GoldenCrateLootTableGenerator::new, LootContextTypes.BLOCK),
                        new LootTableProvider.LootTypeGenerator(DiamondCrateLootTableGenerator::new, LootContextTypes.BLOCK),
                        new LootTableProvider.LootTypeGenerator(NetheriteCrateLootTableGenerator::new, LootContextTypes.BLOCK)
                )
        );
    }
}
