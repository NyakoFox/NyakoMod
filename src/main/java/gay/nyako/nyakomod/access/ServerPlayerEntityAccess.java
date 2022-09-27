package gay.nyako.nyakomod.access;

import net.minecraft.util.math.Vec3d;

public interface ServerPlayerEntityAccess {
    void setSafeMode(boolean bool);
    boolean isInSafeMode();
    void setJoinPos(Vec3d pos);
    Vec3d getJoinPos();
}
