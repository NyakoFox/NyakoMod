package gay.nyako.nyakomod;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class NyakoPaintingVariants {
    public static final PaintingVariant BOBOMB = register("bobomb", new PaintingVariant(64, 64));
    public static final PaintingVariant LAWNMOWER = register("lawnmower", new PaintingVariant(64, 64));
    public static final PaintingVariant CREATION = register("creation", new PaintingVariant(64, 32));

    private static PaintingVariant register(String id, PaintingVariant variant) {
        return Registry.register(Registry.PAINTING_VARIANT, new Identifier("nyakomod", id), variant);
    }

    public static void register() {};
}
