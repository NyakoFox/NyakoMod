package gay.nyako.nyakomod.struct;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

public class PlayerTeleportPayload {
  public ServerPlayerEntity player;
  public ServerWorld world;
  public double x;
  public double y;
  public double z;
  public float yaw;
  public float pitch;
}
