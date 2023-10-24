package gay.nyako.nyakomod;

import net.minecraft.client.render.DimensionEffects;
import net.minecraft.util.math.Vec3d;

public class EchoLandsDimensionEffects extends DimensionEffects {

    public EchoLandsDimensionEffects() {
        super(Float.NaN, false, DimensionEffects.SkyType.NONE, false, true);
    }

    @Override
    public float[] getFogColorOverride(float skyAngle, float tickDelta) {
        return null;
    }

    @Override
    public Vec3d adjustFogColor(Vec3d color, float sunHeight) {
        return color;
    }

    @Override
    public boolean useThickFog(int camX, int camY) {
        return true;
    }
}
