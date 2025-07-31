package gay.nyako.nyakomod;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NyakoPaintingVariants {
    public static final PaintingVariant BOBOMB = register("bobomb", new PaintingVariant(64, 64));
    public static final PaintingVariant CREATION = register("creation", new PaintingVariant(64, 32));
    public static final PaintingVariant MARISAD = register("marisad", new PaintingVariant(64, 64));
    public static final PaintingVariant CHIHUAHUA = register("chihuahua", new PaintingVariant(16, 32));
    public static final PaintingVariant JOHN_PORK = register("john_pork", new PaintingVariant(32, 64));
    public static final PaintingVariant FUNNY_FISH = register("funny_fish", new PaintingVariant(64, 64));
    public static final PaintingVariant SOGGY_CAT = register("soggy_cat", new PaintingVariant(64, 64));
    public static final PaintingVariant EYE_OF_HORUS = register("eye_of_horus", new PaintingVariant(64, 64));
    public static final PaintingVariant TWO_HANDS = register("two_hands", new PaintingVariant(64, 64));
    public static final PaintingVariant HEDGEHOG_ECHIDNA = register("hedgehog_echidna", new PaintingVariant(64, 64));
    public static final PaintingVariant PACK = register("pack", new PaintingVariant(16, 16));
    public static final PaintingVariant GEOMETRY = register("geometry", new PaintingVariant(16, 16));
    public static final PaintingVariant KNOWLEDGE = register("knowledge", new PaintingVariant(16, 32));
    public static final PaintingVariant ROOTS = register("roots", new PaintingVariant(16, 16));

    private static PaintingVariant register(String id, PaintingVariant variant) {
        return Registry.register(Registries.PAINTING_VARIANT, NyakoMod.id(id), variant);
    }

    public static void register() {};
}
