package gay.nyako.nyakomod;

import net.minecraft.entity.damage.DamageSource;

public class NyakoDamageSources {
    public static final DamageSource TOTEM_OF_DYING = new DamageSource("totemOfDying").setBypassesArmor().setOutOfWorld();
    public static final DamageSource KILLBIND = new DamageSource("killbind").setBypassesArmor().setOutOfWorld();
    public static final DamageSource EAT_PICKAXE = new DamageSource("eatPickaxe").setBypassesArmor().setUnblockable();
}
