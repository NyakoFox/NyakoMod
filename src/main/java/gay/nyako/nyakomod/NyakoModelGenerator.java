package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.Optional;

import static net.minecraft.data.client.BlockStateModelGenerator.createSingletonBlockState;

public class NyakoModelGenerator extends FabricModelProvider {
    public NyakoModelGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // ...

        registerSingleCoinBlocks(NyakoMod.COPPER_SINGLE_COIN_BLOCK, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoMod.GOLD_SINGLE_COIN_BLOCK, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoMod.DIAMOND_SINGLE_COIN_BLOCK, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoMod.EMERALD_SINGLE_COIN_BLOCK, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoMod.NETHERITE_SINGLE_COIN_BLOCK, blockStateModelGenerator);

        registerBlueprintWorkbench(blockStateModelGenerator);

        // Shop block
        blockStateModelGenerator.registerNorthDefaultHorizontalRotated(NyakoMod.MAIN_SHOP_BLOCK, TexturedModel.ORIENTABLE_WITH_BOTTOM);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoMod.PLASTEEL_CASING_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoMod.PLASTEEL_SMOOTH_CASING_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoMod.PLASTEEL_PLATING_BLOCK);
        blockStateModelGenerator.registerAxisRotated(NyakoMod.PLASTEEL_PILLAR_BLOCK, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);
    }

    private void registerBlueprintWorkbench(BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = (new TextureMap())
                .put(TextureKey.PARTICLE, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.DOWN, TextureMap.getId(Blocks.DARK_OAK_PLANKS))
                .put(TextureKey.UP, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_top"))
                .put(TextureKey.NORTH, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.EAST, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.SOUTH, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.WEST, TextureMap.getSubId(NyakoMod.BLUEPRINT_WORKBENCH, "_side"));
        blockStateModelGenerator.blockStateCollector.accept(createSingletonBlockState(NyakoMod.BLUEPRINT_WORKBENCH, Models.CUBE.upload(NyakoMod.BLUEPRINT_WORKBENCH, textureMap, blockStateModelGenerator.modelCollector)));
    }

    public void registerSingleCoinBlocks(Block block, BlockStateModelGenerator blockStateModelGenerator) {
        ArrayList<Pair<String,String>> variants = new ArrayList<>();
        variants.add(new Pair<>("_one",    "single_coin"       ));
        variants.add(new Pair<>("_two",    "two_single_coins"  ));
        variants.add(new Pair<>("_three",  "three_single_coins"));
        variants.add(new Pair<>("_four",   "four_single_coins" ));
        variants.add(new Pair<>("_five",   "five_single_coins" ));
        variants.add(new Pair<>("_six",    "six_single_coins"  ));
        variants.add(new Pair<>("_seven",  "seven_single_coins"));
        variants.add(new Pair<>("_eight",  "eight_single_coins"));
        variants.add(new Pair<>("_nine",   "nine_single_coins" ));
        variants.add(new Pair<>("_ten",    "ten_single_coins"  ));
        variants.add(new Pair<>("_eleven", "eleven_single_coins"));
        variants.add(new Pair<>("_twelve", "twelve_single_coins"));
        variants.add(new Pair<>("_thirteen", "thirteen_single_coins"));
        variants.add(new Pair<>("_fourteen", "fourteen_single_coins"));
        variants.add(new Pair<>("_fifteen", "fifteen_single_coins"));
        variants.add(new Pair<>("_sixteen", "sixteen_single_coins"));
        variants.add(new Pair<>("_seventeen", "seventeen_single_coins"));
        variants.add(new Pair<>("_eighteen", "eighteen_single_coins"));
        variants.add(new Pair<>("_nineteen", "nineteen_single_coins"));
        variants.add(new Pair<>("_twenty", "twenty_single_coins"));
        variants.add(new Pair<>("_twenty_one", "twenty_one_single_coins"));
        variants.add(new Pair<>("_twenty_two", "twenty_two_single_coins"));
        variants.add(new Pair<>("_twenty_three", "twenty_three_single_coins"));
        variants.add(new Pair<>("_twenty_four", "twenty_four_single_coins"));
        variants.add(new Pair<>("_twenty_five", "twenty_five_single_coins"));
        variants.add(new Pair<>("_twenty_six", "twenty_six_single_coins"));
        variants.add(new Pair<>("_twenty_seven", "twenty_seven_single_coins"));
        variants.add(new Pair<>("_twenty_eight", "twenty_eight_single_coins"));
        variants.add(new Pair<>("_twenty_nine", "twenty_nine_single_coins"));
        variants.add(new Pair<>("_thirty", "thirty_single_coins"));
        variants.add(new Pair<>("_thirty_one", "thirty_one_single_coins"));
        variants.add(new Pair<>("_thirty_two", "thirty_two_single_coins"));

        var blockStateVariantMap = BlockStateVariantMap.create(NyakoMod.COINS_PROPERTY);
        var counter = 0;
        for (Pair<String, String> pair : variants) {
            counter++;

            String variant = pair.getLeft();
            String model   = pair.getRight();
            Model TEMPLATE_SINGLE_COIN = block(model, variant, TextureKey.TEXTURE);

            TextureMap textureMap = TextureMap.texture(block);
            Identifier identifier = TEMPLATE_SINGLE_COIN.upload(block, textureMap, blockStateModelGenerator.modelCollector);

            blockStateVariantMap.register(counter, BlockStateVariant.create().put(
                    VariantSettings.MODEL,
                    identifier
            ));
        }
        var variantsBlockStateSupplier = VariantsBlockStateSupplier
                .create(block)
                .coordinate(blockStateVariantMap);

        blockStateModelGenerator.blockStateCollector.accept(variantsBlockStateSupplier);
    }

    private static Model block(String parent, String variant, TextureKey ... requiredTextureKeys) {
        return new Model(Optional.of(new Identifier("nyakomod", "block/" + parent)), Optional.of(variant), requiredTextureKeys);
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        // Coins
        itemModelGenerator.register(NyakoMod.COPPER_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.GOLD_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.EMERALD_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.DIAMOND_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.NETHERITE_COIN_ITEM, Models.GENERATED);

        // Coin bags
        itemModelGenerator.register(NyakoMod.BAG_OF_COINS_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.HUNGRY_BAG_OF_COINS_ITEM, Models.GENERATED);

        // Music discs
        NyakoModDisc.registerModels(itemModelGenerator);

        // Various gacha items
        itemModelGenerator.register(NyakoMod.DISCORD_GACHA_ITEM, Models.GENERATED);

        // Staffs
        itemModelGenerator.register(NyakoMod.STAFF_OF_SMITING_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.STAFF_OF_VORBULATION_ITEM, Models.GENERATED);

        // Misc. items
        itemModelGenerator.register(NyakoMod.DRIP_JACKET, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.PRESENT_ITEM, Models.GENERATED);

        itemModelGenerator.register(NyakoMod.TEST_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.PET_DRAGON_SUMMON_ITEM, Models.GENERATED);
    }
}
