package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.SingleCoinBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.data.client.*;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class NyakoBlockLootTableProvider extends FabricBlockLootTableProvider {
    public NyakoBlockLootTableProvider(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    protected void generateBlockLootTables() {
        registerSingleCoinBlocks(NyakoMod.COPPER_SINGLE_COIN_BLOCK);
        registerSingleCoinBlocks(NyakoMod.GOLD_SINGLE_COIN_BLOCK);
        registerSingleCoinBlocks(NyakoMod.DIAMOND_SINGLE_COIN_BLOCK);
        registerSingleCoinBlocks(NyakoMod.EMERALD_SINGLE_COIN_BLOCK);
        registerSingleCoinBlocks(NyakoMod.NETHERITE_SINGLE_COIN_BLOCK);
    }

    public void registerSingleCoinBlocks(Block inputBlock) {
        List<Integer> valueList = IntStream.rangeClosed(2, SingleCoinBlock.MAX_COINS).boxed().toList();
        addDrop(inputBlock, (Block block) -> LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(block).apply(valueList, integer -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(integer.intValue())).conditionally(BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().exactMatch(SingleCoinBlock.COINS, integer.intValue())))))));
    }
}
