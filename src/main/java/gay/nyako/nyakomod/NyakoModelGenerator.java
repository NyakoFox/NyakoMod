package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.*;
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

        registerSingleCoinBlocks(NyakoBlocks.COPPER_SINGLE_COIN, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoBlocks.GOLD_SINGLE_COIN, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoBlocks.DIAMOND_SINGLE_COIN, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoBlocks.EMERALD_SINGLE_COIN, blockStateModelGenerator);
        registerSingleCoinBlocks(NyakoBlocks.NETHERITE_SINGLE_COIN, blockStateModelGenerator);

        registerBlueprintWorkbench(blockStateModelGenerator);

        // Shop block
        blockStateModelGenerator.registerNorthDefaultHorizontalRotated(NyakoBlocks.MAIN_SHOP, TexturedModel.ORIENTABLE_WITH_BOTTOM);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_CASING);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_SMOOTH_CASING);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_PLATING);
        blockStateModelGenerator.registerAxisRotated(NyakoBlocks.PLASTEEL_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.FIREBLU);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.TRUE_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.NOTE_BLOCK_PLUS);
    }

    private void registerBlueprintWorkbench(BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = (new TextureMap())
                .put(TextureKey.PARTICLE, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.DOWN, TextureMap.getId(Blocks.SPRUCE_PLANKS))
                .put(TextureKey.UP, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_top"))
                .put(TextureKey.NORTH, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.EAST, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.SOUTH, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_side"))
                .put(TextureKey.WEST, TextureMap.getSubId(NyakoBlocks.BLUEPRINT_WORKBENCH, "_side"));
        blockStateModelGenerator.blockStateCollector.accept(createSingletonBlockState(NyakoBlocks.BLUEPRINT_WORKBENCH, Models.CUBE.upload(NyakoBlocks.BLUEPRINT_WORKBENCH, textureMap, blockStateModelGenerator.modelCollector)));
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
        variants.add(new Pair<>("_thirty_three", "thirty_three_single_coins"));
        variants.add(new Pair<>("_thirty_four", "thirty_four_single_coins"));
        variants.add(new Pair<>("_thirty_five", "thirty_five_single_coins"));
        variants.add(new Pair<>("_thirty_six", "thirty_six_single_coins"));
        variants.add(new Pair<>("_thirty_seven", "thirty_seven_single_coins"));
        variants.add(new Pair<>("_thirty_eight", "thirty_eight_single_coins"));
        variants.add(new Pair<>("_thirty_nine", "thirty_nine_single_coins"));
        variants.add(new Pair<>("_forty", "forty_single_coins"));
        variants.add(new Pair<>("_forty_one", "forty_one_single_coins"));
        variants.add(new Pair<>("_forty_two", "forty_two_single_coins"));
        variants.add(new Pair<>("_forty_three", "forty_three_single_coins"));
        variants.add(new Pair<>("_forty_four", "forty_four_single_coins"));
        variants.add(new Pair<>("_forty_five", "forty_five_single_coins"));
        variants.add(new Pair<>("_forty_six", "forty_six_single_coins"));
        variants.add(new Pair<>("_forty_seven", "forty_seven_single_coins"));
        variants.add(new Pair<>("_forty_eight", "forty_eight_single_coins"));
        variants.add(new Pair<>("_forty_nine", "forty_nine_single_coins"));
        variants.add(new Pair<>("_fifty", "fifty_single_coins"));
        variants.add(new Pair<>("_fifty_one", "fifty_one_single_coins"));
        variants.add(new Pair<>("_fifty_two", "fifty_two_single_coins"));
        variants.add(new Pair<>("_fifty_three", "fifty_three_single_coins"));
        variants.add(new Pair<>("_fifty_four", "fifty_four_single_coins"));
        variants.add(new Pair<>("_fifty_five", "fifty_five_single_coins"));
        variants.add(new Pair<>("_fifty_six", "fifty_six_single_coins"));
        variants.add(new Pair<>("_fifty_seven", "fifty_seven_single_coins"));
        variants.add(new Pair<>("_fifty_eight", "fifty_eight_single_coins"));
        variants.add(new Pair<>("_fifty_nine", "fifty_nine_single_coins"));
        variants.add(new Pair<>("_sixty", "sixty_single_coins"));
        variants.add(new Pair<>("_sixty_one", "sixty_one_single_coins"));
        variants.add(new Pair<>("_sixty_two", "sixty_two_single_coins"));
        variants.add(new Pair<>("_sixty_three", "sixty_three_single_coins"));
        variants.add(new Pair<>("_sixty_four", "sixty_four_single_coins"));

        var blockStateVariantMap = BlockStateVariantMap.create(NyakoMod.COINS_PROPERTY);
        var counter = 0;
        for (Pair<String, String> pair : variants) {
            counter++;

            String variant = pair.getLeft();
            String model   = "coins/" + pair.getRight();
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
        itemModelGenerator.register(NyakoItems.COPPER_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.GOLD_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.EMERALD_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.DIAMOND_COIN_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.NETHERITE_COIN_ITEM, Models.GENERATED);

        // Coin bags
        itemModelGenerator.register(NyakoItems.BAG_OF_COINS_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.HUNGRY_BAG_OF_COINS_ITEM, Models.GENERATED);

        // Music discs
        NyakoDiscs.registerModels(itemModelGenerator);

        // Various gacha items
        itemModelGenerator.register(NyakoItems.DISCORD_GACHA_ITEM, Models.GENERATED);

        // Staffs
        itemModelGenerator.register(NyakoItems.STAFF_OF_SMITING_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.STAFF_OF_VORBULATION_ITEM, Models.GENERATED);

        // Misc. items
        itemModelGenerator.register(NyakoItems.DRIP_JACKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.PRESENT_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.RETENTIVE_BALL_ITEM, Models.GENERATED);

        itemModelGenerator.register(NyakoItems.TEST_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.PET_DRAGON_SUMMON_ITEM, Models.GENERATED);

        itemModelGenerator.register(NyakoItems.PIAMOND_DICKAXE, Models.HANDHELD);

        // Minecraft items lol
        registerMinecraftBlockItem(itemModelGenerator, "minecraft:block/nether_portal", Blocks.NETHER_PORTAL);
        registerMinecraftBlockItem(itemModelGenerator, "minecraft:block/fire_0", Blocks.FIRE);
        registerMinecraftBlockItem(itemModelGenerator, "minecraft:block/soul_fire_0", Blocks.SOUL_FIRE);
        registerMinecraftBlockItem(itemModelGenerator, "minecraft:block/water_still", Blocks.WATER);
        registerMinecraftBlockItem(itemModelGenerator, "minecraft:block/lava_still", Blocks.LAVA);
    }

    public final void registerMinecraftBlockItem(ItemModelGenerator itemModelGenerator, String texture, Block block) {
        TextureMap layer0 = new TextureMap().put(TextureKey.LAYER0, Identifier.tryParse(texture));
        Models.GENERATED.upload(ModelIds.getItemModelId(block.asItem()), layer0, itemModelGenerator.writer);
    }
}
