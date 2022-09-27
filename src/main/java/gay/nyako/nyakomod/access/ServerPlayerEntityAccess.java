package gay.nyako.nyakomod.access;

import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameMode;

public interface ServerPlayerEntityAccess {
    void setSafeMode(boolean bool);
    boolean isInSafeMode();
    void setJoinPos(Vec3d pos);
    Vec3d getJoinPos();
    void setJoinPreviousGameMode(GameMode gameMode);
    GameMode getJoinPreviousGameMode();
    void setJoinGameMode(GameMode gameMode);
    GameMode getJoinGameMode();
}
