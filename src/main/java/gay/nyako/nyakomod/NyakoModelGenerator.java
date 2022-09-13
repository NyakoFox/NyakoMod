package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class NyakoModelGenerator extends FabricModelProvider {
    public NyakoModelGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // ...
        /*
        registerSingleCoinBlock(NyakoMod.COPPER_SINGLE_COIN_BLOCK);
        registerSingleCoinBlock(NyakoMod.GOLD_SINGLE_COIN_BLOCK);
        registerSingleCoinBlock(NyakoMod.DIAMOND_SINGLE_COIN_BLOCK);
        registerSingleCoinBlock(NyakoMod.EMERALD_SINGLE_COIN_BLOCK);
        registerSingleCoinBlock(NyakoMod.NETHERITE_SINGLE_COIN_BLOCK);*/
    }

    public void registerSingleCoinBlock(Block block) {
        Model TEMPLATE_SINGLE_COIN = block("single_coin", "_top", TextureKey.TEXTURE);

        TextureMap textureMap = TextureMap.texture(block);
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
        itemModelGenerator.register(NyakoMod.MUSIC_DISC_MASK, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.MUSIC_DISC_WOLVES, Models.GENERATED);

        // Various gacha items
        itemModelGenerator.register(NyakoMod.DISCORD_GACHA_ITEM, Models.GENERATED);

        // Staffs
        itemModelGenerator.register(NyakoMod.STAFF_OF_SMITING_ITEM, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.STAFF_OF_VORBULATION_ITEM, Models.GENERATED);

        // Misc. items
        itemModelGenerator.register(NyakoMod.DRIP_JACKET, Models.GENERATED);
        itemModelGenerator.register(NyakoMod.PRESENT_ITEM, Models.GENERATED);
    }
}
