package gay.nyako.nyakomod.mixin;

import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityAccess {
    private boolean fromSpawner = false;
    @Override
    public void setFromSpawner(boolean bool) {
        fromSpawner = bool;
    }
    @Override
    public boolean isFromSpawner() {
        return fromSpawner;
    }
}