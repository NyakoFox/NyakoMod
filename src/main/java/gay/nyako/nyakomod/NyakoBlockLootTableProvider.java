package gay.nyako.nyakomod;

import gay.nyako.nyakomod.block.SingleCoinBlock;
import gay.nyako.nyakomod.block.SproutHeartBlock;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.CaveVines;
import net.minecraft.block.TallPlantBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.Items;
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
        addDrop(inputBlock, (Block block) -> LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .with(ItemEntry.builder(block).apply(valueList, integer -> SetCountLootFunction.builder(ConstantLootNumberProvider.create(integer)).conditionally(BlockStatePropertyLootCondition.builder(block).properties(StatePredicate.Builder.create().exactMatch(SingleCoinBlock.COINS, integer)))))));
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
        this.addDrop(NyakoBlocks.ECHO_TRAPDOOR);
        this.addDrop(NyakoBlocks.ECHO_PRESSURE_PLATE);
        this.addDrop(NyakoBlocks.ECHO_BUTTON);
        this.addDrop(NyakoBlocks.ECHO_SIGN);
        this.addDrop(NyakoBlocks.ECHO_HANGING_SIGN);
        this.addDrop(NyakoBlocks.ECHO_DOOR, this::doorDrops);

        this.addDrop(NyakoBlocks.ECHO_ROOTS);
        this.addDrop(NyakoBlocks.ECHO_BULB);

        this.addDrop(NyakoBlocks.ECHO_LEAVES, (Block block) -> this.leavesDrops(block, NyakoBlocks.ECHO_SAPLING, SAPLING_DROP_CHANCE));

        this.addPottedPlantDrops(NyakoBlocks.POTTED_ECHO_ROOTS);
        this.addDrop(NyakoBlocks.ECHO_SPROUTBULB, (Block block) -> this.dropsWithProperty(block, TallPlantBlock.HALF, DoubleBlockHalf.LOWER));

        this.addDrop(NyakoBlocks.ECHO_SPROUTHEART, (Block block) -> LootTable.builder()
                .pool(LootPool.builder()
                        .with(ItemEntry.builder(NyakoItems.HEART_BERRY))
                        .conditionally(BlockStatePropertyLootCondition.builder(block)
                                .properties(
                                        StatePredicate.Builder.create()
                                                .exactMatch(SproutHeartBlock.HEART, true)
                                                .exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER)
                                )
                        )
                )
                .pool(LootPool.builder()
                        .with(ItemEntry.builder(NyakoItems.ECHO_SPROUTHEART))
                        .conditionally(BlockStatePropertyLootCondition.builder(block)
                                .properties(
                                        StatePredicate.Builder.create()
                                                .exactMatch(TallPlantBlock.HALF, DoubleBlockHalf.LOWER)
                                )
                        )
                ));

        this.addDrop(NyakoBlocks.BENTHIC_SPINE);
        this.addDrop(NyakoBlocks.BENTHIC_SPUR);
        this.addDrop(NyakoBlocks.STRIPPED_BENTHIC_SPINE);
        this.addDrop(NyakoBlocks.STRIPPED_BENTHIC_SPUR);
        this.addDrop(NyakoBlocks.BENTHIC_PLANKS);
        this.addDrop(NyakoBlocks.BENTHIC_SLAB);
        this.addDrop(NyakoBlocks.BENTHIC_STAIRS);
        this.addDrop(NyakoBlocks.BENTHIC_FENCE);
        this.addDrop(NyakoBlocks.BENTHIC_FENCE_GATE);
        this.addDrop(NyakoBlocks.BENTHIC_TRAPDOOR);
        this.addDrop(NyakoBlocks.BENTHIC_PRESSURE_PLATE);
        this.addDrop(NyakoBlocks.BENTHIC_BUTTON);
        this.addDrop(NyakoBlocks.BENTHIC_SIGN);
        this.addDrop(NyakoBlocks.BENTHIC_HANGING_SIGN);
        this.addDrop(NyakoBlocks.BENTHIC_DOOR, this::doorDrops);

        this.addDrop(NyakoBlocks.BENTHIC_LEAVES, (Block block) -> this.leavesDrops(block, NyakoBlocks.BENTHIC_SAPLING, SAPLING_DROP_CHANCE));

        this.addDrop(NyakoBlocks.ECHO_SAPLING);
        this.addDrop(NyakoBlocks.BENTHIC_SAPLING);

        this.addPottedPlantDrops(NyakoBlocks.POTTED_ECHO_SAPLING);
        this.addPottedPlantDrops(NyakoBlocks.POTTED_BENTHIC_SAPLING);

        this.addDrop(NyakoBlocks.ELYTRA_BLOCK);
        this.addDrop(NyakoBlocks.CHARGED_IRON_BLOCK);
        this.addDrop(NyakoBlocks.WOODEN_CRATE);
        this.addDrop(NyakoBlocks.IRON_CRATE);
        this.addDrop(NyakoBlocks.GOLDEN_CRATE);
        this.addDrop(NyakoBlocks.DIAMOND_CRATE);
        this.addDrop(NyakoBlocks.NETHERITE_CRATE);
    }
}
