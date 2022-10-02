package gay.nyako.nyakomod;

import eu.pb4.placeholders.api.TextParserUtils;
import gay.nyako.nyakomod.access.EntityAccess;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.PersistentState;

public class SlimeSkyManager extends PersistentState {

    public enum SlimeSkyState {
        INACTIVE,
        ACTIVE
    }

    public ServerWorld world;
    public long stateLength = 7 * 24000;
    public SlimeSkyState state = SlimeSkyState.INACTIVE;

    public SlimeSkyManager() {}

    public SlimeSkyManager(NbtCompound nbt) {
        stateLength = nbt.getLong("stateLength");
        state = SlimeSkyState.valueOf(nbt.getString("state"));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        nbt.putLong("stateLength", stateLength);
        nbt.putString("state", state.name());
        return nbt;
    }

    public void tick() {
        if (world.getServer().getPlayerManager().getCurrentPlayerCount() == 0) {
            return;
        }

        stateLength--;

        if (stateLength % (20 * 60 * 10) == 0) {
            // Save every 10 minutes
            markDirty();
        }

        switch (state) {
            case INACTIVE:
                // Make the slime rain active
                if (stateLength <= 0) {
                    state = SlimeSkyState.ACTIVE;
                    stateLength = (world.getRandom().nextBetween(9, 15) * 60L) * 20;
                    var prefix = (MutableText) TextParserUtils.formatText("<aqua>[i]</aqua> <bold>>></bold> ");
                    world.getServer().getPlayerManager().broadcast(prefix.append(Text.literal("Slime is falling from the sky!").setStyle(Style.EMPTY.withColor(0x32FF82))), false);
                    markDirty();
                }
                break;
            case ACTIVE:
                // Loop through all players

                if (stateLength % 20 == 0) {
                    var randomPlayer = world.getRandomAlivePlayer();
                    if (randomPlayer != null) {
                        var pos = randomPlayer.getBlockPos();
                        // Pick a random position within 128 blocks of the player
                        var randomPos = pos.add(world.getRandom().nextBetween(-64, 64), world.getRandom().nextBetween(64, 96), world.getRandom().nextBetween(-64, 64));

                        // Spawn a slime
                        var slime = new SlimeEntity(EntityType.SLIME, world);
                        slime.setPosition(randomPos.getX(), randomPos.getY(), randomPos.getZ());
                        slime.setSize(world.getRandom().nextBetween(1, 5), true);
                        ((EntityAccess)slime).setFromSpawner(true);
                        slime.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 20 * 15, 0, false, false, false));

                        world.spawnEntity(slime);
                    }
                }

                // We're done slime rain
                if (stateLength <= 0) {
                    state = SlimeSkyState.INACTIVE;
                    stateLength = world.getRandom().nextBetween((40 * 24000), (80 * 24000)); // 40 - 80 days
                    var prefix = (MutableText) TextParserUtils.formatText("<aqua>[i]</aqua> <bold>>></bold> ");
                    world.getServer().getPlayerManager().broadcast(prefix.append(Text.literal("Slime has stopped falling from the sky.").setStyle(Style.EMPTY.withColor(0x32FF82))), false);
                    markDirty();
                }
                break;
        }
    }

    public static SlimeSkyManager forWorld(ServerWorld world) {
        var slimeSkyManager = world.getPersistentStateManager().getOrCreate(SlimeSkyManager::new, SlimeSkyManager::new, "slime_sky_manager");
        slimeSkyManager.world = world;
        return slimeSkyManager;
    }
}
