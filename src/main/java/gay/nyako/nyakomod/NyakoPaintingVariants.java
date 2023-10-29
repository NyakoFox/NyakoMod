package gay.nyako.nyakomod;

import net.minecraft.entity.decoration.painting.PaintingVariant;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class NyakoPaintingVariants {
    public static final PaintingVariant BOBOMB = register("bobomb", new PaintingVariant(64, 64));
    public static final PaintingVariant LAWNMOWER = register("lawnmower", new PaintingVariant(64, 64));
    public static final PaintingVariant CREATION = register("creation", new PaintingVariant(64, 32));
    public static final PaintingVariant MARISAD = register("marisad", new PaintingVariant(64, 64));
    public static final PaintingVariant CHIHUAHUA = register("chihuahua", new PaintingVariant(16, 32));
    public static final PaintingVariant OREO = register("oreo", new PaintingVariant(64, 64));
    public static final PaintingVariant JOHN_PORK = register("john_pork", new PaintingVariant(32, 64));

    private static PaintingVariant register(String id, PaintingVariant variant) {
        return Registry.register(Registries.PAINTING_VARIANT, new Identifier("nyakomod", id), variant);
    }

    public static void register() {};
}
