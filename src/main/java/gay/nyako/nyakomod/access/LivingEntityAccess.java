package gay.nyako.nyakomod.access;

public interface LivingEntityAccess {
    void setFromSpawner(boolean bool);
    boolean isFromSpawner();
    void setCoinMultiplier(float multiplier);
    float getCoinMultiplier();
    void setBossBar(boolean bool);
    boolean hasBossBar();
}
