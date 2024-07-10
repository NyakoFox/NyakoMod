package gay.nyako.nyakomod;

import gay.nyako.nyakomod.item.PetChangeSummonItem;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.BlockFace;
import net.minecraft.data.client.*;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;

import java.util.*;

import static net.minecraft.data.client.BlockStateModelGenerator.createSingletonBlockState;

public class NyakoModelGenerator extends FabricModelProvider {
    public NyakoModelGenerator(FabricDataOutput generator) {
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
        registerNoteBlockPlus(blockStateModelGenerator);
        registerPresentWrapper(blockStateModelGenerator);

        // Shop block
        blockStateModelGenerator.registerNorthDefaultHorizontalRotated(NyakoBlocks.MAIN_SHOP, TexturedModel.ORIENTABLE_WITH_BOTTOM);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_CASING);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_SMOOTH_CASING);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.PLASTEEL_PLATING);
        blockStateModelGenerator.registerAxisRotated(NyakoBlocks.PLASTEEL_PILLAR, TexturedModel.END_FOR_TOP_CUBE_COLUMN, TexturedModel.END_FOR_TOP_CUBE_COLUMN_HORIZONTAL);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.FIREBLU);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.TRUE_BLOCK);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.TITANSTONE);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.GLOWING_OBSIDIAN);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.ECHO_DIRT);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.ECHO_STONE);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.ECHO_SLATE);

        blockStateModelGenerator.registerCubeAllModelTexturePool(NyakoBlockFamilies.ECHO.getBaseBlock()).family(NyakoBlockFamilies.ECHO);

        blockStateModelGenerator.registerLog(NyakoBlocks.ECHO_SPINE).stem(NyakoBlocks.ECHO_SPINE).wood(NyakoBlocks.ECHO_SPUR);
        blockStateModelGenerator.registerLog(NyakoBlocks.STRIPPED_ECHO_SPINE).stem(NyakoBlocks.STRIPPED_ECHO_SPINE).wood(NyakoBlocks.STRIPPED_ECHO_SPUR);
        blockStateModelGenerator.registerHangingSign(NyakoBlocks.STRIPPED_ECHO_SPINE, NyakoBlocks.ECHO_HANGING_SIGN, NyakoBlocks.ECHO_WALL_HANGING_SIGN);

        blockStateModelGenerator.registerRoots(NyakoBlocks.ECHO_ROOTS, NyakoBlocks.POTTED_ECHO_ROOTS);
        blockStateModelGenerator.registerSingleton(NyakoBlocks.ECHO_LEAVES, TexturedModel.LEAVES);
        blockStateModelGenerator.registerDoubleBlock(NyakoBlocks.ECHO_SPROUTBULB, BlockStateModelGenerator.TintType.NOT_TINTED);

        blockStateModelGenerator.registerCubeAllModelTexturePool(NyakoBlockFamilies.BENTHIC.getBaseBlock()).family(NyakoBlockFamilies.BENTHIC);

        blockStateModelGenerator.registerLog(NyakoBlocks.BENTHIC_SPINE).stem(NyakoBlocks.BENTHIC_SPINE).wood(NyakoBlocks.BENTHIC_SPUR);
        blockStateModelGenerator.registerLog(NyakoBlocks.STRIPPED_BENTHIC_SPINE).stem(NyakoBlocks.STRIPPED_BENTHIC_SPINE).wood(NyakoBlocks.STRIPPED_BENTHIC_SPUR);
        blockStateModelGenerator.registerHangingSign(NyakoBlocks.STRIPPED_BENTHIC_SPINE, NyakoBlocks.BENTHIC_HANGING_SIGN, NyakoBlocks.BENTHIC_WALL_HANGING_SIGN);

        blockStateModelGenerator.registerSingleton(NyakoBlocks.BENTHIC_LEAVES, TexturedModel.LEAVES);

        blockStateModelGenerator.registerFlowerPotPlant(NyakoBlocks.ECHO_SAPLING, NyakoBlocks.POTTED_ECHO_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);
        blockStateModelGenerator.registerFlowerPotPlant(NyakoBlocks.BENTHIC_SAPLING, NyakoBlocks.POTTED_BENTHIC_SAPLING, BlockStateModelGenerator.TintType.NOT_TINTED);

        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.ELYTRA_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.CHARGED_IRON_BLOCK);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.WOODEN_CRATE);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.IRON_CRATE);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.GOLDEN_CRATE);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.DIAMOND_CRATE);
        blockStateModelGenerator.registerSimpleCubeAll(NyakoBlocks.NETHERITE_CRATE);

        registerAllybox(blockStateModelGenerator);

        registerVent(blockStateModelGenerator);
        registerFan(blockStateModelGenerator);
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

    private void registerNoteBlockPlus(BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = (new TextureMap())
                .put(TextureKey.PARTICLE, TextureMap.getId(Blocks.NOTE_BLOCK))
                .put(TextureKey.DOWN, TextureMap.getSubId(NyakoBlocks.NOTE_BLOCK_PLUS, "_bottom"))
                .put(TextureKey.UP, TextureMap.getSubId(NyakoBlocks.NOTE_BLOCK_PLUS, "_top"))
                .put(TextureKey.NORTH, TextureMap.getId(Blocks.NOTE_BLOCK))
                .put(TextureKey.EAST, TextureMap.getId(Blocks.NOTE_BLOCK))
                .put(TextureKey.SOUTH, TextureMap.getSubId(NyakoBlocks.NOTE_BLOCK_PLUS, "_front"))
                .put(TextureKey.WEST, TextureMap.getSubId(NyakoBlocks.NOTE_BLOCK_PLUS, "_west"));
        blockStateModelGenerator.blockStateCollector.accept(createSingletonBlockState(NyakoBlocks.NOTE_BLOCK_PLUS, Models.CUBE.upload(NyakoBlocks.NOTE_BLOCK_PLUS, textureMap, blockStateModelGenerator.modelCollector)));
    }

    private void registerPresentWrapper(BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = (new TextureMap())
                .put(TextureKey.PARTICLE, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"))
                .put(TextureKey.DOWN, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"))
                .put(TextureKey.UP, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_top"))
                .put(TextureKey.NORTH, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"))
                .put(TextureKey.EAST, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"))
                .put(TextureKey.SOUTH, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"))
                .put(TextureKey.WEST, TextureMap.getSubId(NyakoBlocks.PRESENT_WRAPPER, "_side"));
        blockStateModelGenerator.blockStateCollector.accept(createSingletonBlockState(NyakoBlocks.PRESENT_WRAPPER, Models.CUBE.upload(NyakoBlocks.PRESENT_WRAPPER, textureMap, blockStateModelGenerator.modelCollector)));
    }

    private void registerAllybox(BlockStateModelGenerator blockStateModelGenerator) {
        TextureMap textureMap = (new TextureMap())
                .put(TextureKey.PARTICLE, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_top"))
                .put(TextureKey.DOWN, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_bottom"))
                .put(TextureKey.UP, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_top"))
                .put(TextureKey.NORTH, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_north"))
                .put(TextureKey.EAST, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_east"))
                .put(TextureKey.SOUTH, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_south"))
                .put(TextureKey.WEST, TextureMap.getSubId(NyakoBlocks.ALLYBOX, "_west"));
        blockStateModelGenerator.blockStateCollector.accept(createSingletonBlockState(NyakoBlocks.ALLYBOX, Models.CUBE.upload(NyakoBlocks.ALLYBOX, textureMap, blockStateModelGenerator.modelCollector)));
    }

    private void registerVent(BlockStateModelGenerator blockStateModelGenerator) {
        // uses HORIZONTAL_FACING

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(
                        NyakoBlocks.VENT,
                        BlockStateVariant.create().put(
                                VariantSettings.MODEL,
                                ModelIds.getBlockModelId(NyakoBlocks.VENT)
                        )
                ).coordinate(
                        BlockStateVariantMap.create(
                                Properties.HORIZONTAL_FACING
                        ).register(
                                Direction.NORTH, BlockStateVariant.create().put(
                                        VariantSettings.Y, VariantSettings.Rotation.R0
                                )
                        ).register(
                                Direction.EAST, BlockStateVariant.create().put(
                                        VariantSettings.Y, VariantSettings.Rotation.R90
                                )
                        ).register(
                                Direction.SOUTH, BlockStateVariant.create().put(
                                        VariantSettings.Y, VariantSettings.Rotation.R180
                                )
                        ).register(
                                Direction.WEST, BlockStateVariant.create().put(
                                        VariantSettings.Y, VariantSettings.Rotation.R270
                                )
                        )
                )
        );
    }

    private void registerFan(BlockStateModelGenerator blockStateModelGenerator)
    {
        TextureMap textureMap = new TextureMap()
                .put(TextureKey.TOP, TextureMap.getSubId(NyakoBlocks.FAN, "_side"))
                .put(TextureKey.SIDE, TextureMap.getSubId(NyakoBlocks.FAN, "_side"))
                .put(TextureKey.FRONT, TextureMap.getSubId(NyakoBlocks.FAN, "_front"));

        Identifier identifier = Models.ORIENTABLE.upload(NyakoBlocks.FAN, textureMap, blockStateModelGenerator.modelCollector);

        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(NyakoBlocks.FAN, identifier)
                        .coordinate(BlockStateModelGenerator.createNorthDefaultRotationStates())
        );
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
        itemModelGenerator.register(NyakoItems.COPPER_COIN, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.GOLD_COIN, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.EMERALD_COIN, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.DIAMOND_COIN, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.NETHERITE_COIN, Models.GENERATED);

        // Coin bags
        itemModelGenerator.register(NyakoItems.BAG_OF_COINS, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.HUNGRY_BAG_OF_COINS, Models.GENERATED);

        // Music discs
        NyakoDiscs.registerModels(itemModelGenerator);

        // Various gacha items
        itemModelGenerator.register(NyakoItems.DISCORD_GACHA, Models.GENERATED);

        // Staffs
        itemModelGenerator.register(NyakoItems.STAFF_OF_SMITING, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.STAFF_OF_VORBULATION, Models.GENERATED);

        // Misc. items
        itemModelGenerator.register(NyakoItems.DRIP_JACKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.JEAN_JACKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.JEAN_HAT, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.JEANS, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.JEAN_SOCKS, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.PRESENT, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.RETENTIVE_BALL, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.TEST_ITEM, Models.GENERATED);
        registerPetChangeSummoner(itemModelGenerator, (PetChangeSummonItem<?>) NyakoItems.PET_DRAGON_SUMMON);
        itemModelGenerator.register(NyakoItems.PIAMOND_DICKAXE, Models.HANDHELD);
        itemModelGenerator.register(NyakoItems.NETHER_PORTAL_BUCKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.LEFT_DIAMOND, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.RIGHT_DIAMOND, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.DRAGON_MILK_BUCKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.FLINT_AND_STEEL_PLUS, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.HORSE_MILK_BUCKET, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.ENCUMBERING_STONE, String.format(Locale.ROOT, "_%s", "unlocked"), Models.GENERATED);
        itemModelGenerator.register(NyakoItems.SUPER_ENCUMBERING_STONE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.DRAGON_SCALE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.TOTEM_OF_DYING, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.FOAM_ZOMBIE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.SMITHING_HAMMER, Models.HANDHELD);
        itemModelGenerator.register(NyakoItems.ROD_OF_DISCORD, Models.HANDHELD);
        itemModelGenerator.register(NyakoItems.DIAMOND_APPLE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.EMERALD_APPLE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.GREEN_APPLE, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.SPECULAR_FISH, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.RECALL_POTION, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.ENDER_GEM, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.ECHO_PEARL, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.HEART_BERRY, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.HEART_CANISTER, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.BURNING_SUPER_DEATH_SWORD, Models.HANDHELD);
        itemModelGenerator.register(NyakoItems.STICKER_PACK, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.CONDENSED_MATTER_CONTAINER, Models.GENERATED);
        itemModelGenerator.register(NyakoItems.OBSIDIAN_ARROW, Models.GENERATED);

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

    public final void registerPetChangeSummoner(ItemModelGenerator generator, PetChangeSummonItem<?> item) {
        for (var i = 1; i < item.variations.size(); i++) {
            generator.register(item, String.format(Locale.ROOT, "_%02d", i), Models.GENERATED);
        }
    }
}
