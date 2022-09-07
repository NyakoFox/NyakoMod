package gay.nyako.nyakomod;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class NyakoModelGenerator extends FabricModelProvider {
    public NyakoModelGenerator(FabricDataGenerator generator) {
        super(generator);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        // ...
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
