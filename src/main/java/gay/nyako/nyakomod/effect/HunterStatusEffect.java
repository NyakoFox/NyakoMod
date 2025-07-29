package gay.nyako.nyakomod.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class HunterStatusEffect extends StatusEffect {
    public HunterStatusEffect() {
        super(StatusEffectCategory.BENEFICIAL, 0xE6620A);
    }

    // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // In our case, we just make it return true so that it applies the status effect every tick.
        return true;
    }

    /*
    public HashMap<LivingEntity, Boolean> glowingEntities = new HashMap<LivingEntity, Boolean>();

    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof PlayerEntity && entity.world.isClient()) {
            for (var entry : glowingEntities.entrySet()) {
                entry.getKey().setGlowing(entry.getValue());
                glowingEntities.remove(entry.getKey());
            }

            List<LivingEntity> entities = entity.world.getNonSpectatingEntities(LivingEntity.class,
                    new Box(entity.getBlockPos().down(32).east(32).north(32),
                            entity.getBlockPos().up(32).west(32).south(32))
            );

            NyakoMod.LOGGER.info("Found " + entities.size() + " mobs to glow.");

            for (var e : entities) {
                glowingEntities.put(e, e.isGlowing());
                e.setGlowing(true);
            }
        }
    }
     */
}
