package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.SingleCoinBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.predicate.StatePredicate;

import java.util.List;
import java.util.stream.IntStream;

public class NyakoBlockLootTableProvider extends FabricBlockLootTableProvider {
    public NyakoBlockLootTableProvider(FabricDataOutput generator) {
        super(generator);
    }

    public void registerSingleCoinBlocks(Block inputBlock) {
        List<Integer> valueList = IntStream.rangeClosed(2, SingleCoinBlock.MAX_COINS).boxed().toList();
        addDrop(inputBlock, (Block block) -> LootTable.builder().pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0f)).with(ItemEntry.builder(block).apply(valueList, integer -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(integer.intValue())).conditionally(BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().exactMatch(SingleCoinBlock.COINS, integer.intValue())))))));
    }

    @Override
    public void generate() {
        registerSingleCoinBlocks(NyakoBlocks.COPPER_SINGLE_COIN);
        registerSingleCoinBlocks(NyakoBlocks.GOLD_SINGLE_COIN);
        registerSingleCoinBlocks(NyakoBlocks.DIAMOND_SINGLE_COIN);
        registerSingleCoinBlocks(NyakoBlocks.EMERALD_SINGLE_COIN);
        registerSingleCoinBlocks(NyakoBlocks.NETHERITE_SINGLE_COIN);

        this.addDrop(NyakoBlocks.ECHO_DIRT);
        this.addDrop(NyakoBlocks.ECHO_STONE);
        this.addDrop(NyakoBlocks.ECHO_GROWTH, (Block block) -> this.drops(block, NyakoBlocks.ECHO_DIRT));
        this.addDrop(NyakoBlocks.ECHO_SLATE);

        this.addDrop(NyakoBlocks.ECHO_SPINE);
        this.addDrop(NyakoBlocks.ECHO_SPUR);
        this.addDrop(NyakoBlocks.STRIPPED_ECHO_SPINE);
        this.addDrop(NyakoBlocks.STRIPPED_ECHO_SPUR);
        this.addDrop(NyakoBlocks.ECHO_PLANKS);
        this.addDrop(NyakoBlocks.ECHO_SLAB);
        this.addDrop(NyakoBlocks.ECHO_STAIRS);
        this.addDrop(NyakoBlocks.ECHO_FENCE);
        this.addDrop(NyakoBlocks.ECHO_FENCE_GATE);
        this.addDrop(NyakoBlocks.ECHO_DOOR);
        this.addDrop(NyakoBlocks.ECHO_TRAPDOOR);
        this.addDrop(NyakoBlocks.ECHO_PRESSURE_PLATE);
        this.addDrop(NyakoBlocks.ECHO_BUTTON);
        this.addDrop(NyakoBlocks.ECHO_SIGN);
        this.addDrop(NyakoBlocks.ECHO_HANGING_SIGN);
    }
}
