package gay.nyako.nyakomod.struct;

import net.minecraft.server.network.ServerPlayerEntity;

public class PlayerTeleportPayload {
  public ServerPlayerEntity player;
  public double x;
  public double y;
  public double z;
  public float yaw;
  public float pitch;
}
