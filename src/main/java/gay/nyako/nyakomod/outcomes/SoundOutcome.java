package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.client.sound.Sound;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class SoundOutcome extends PositionedOutcome {
    private final OutcomeField<Identifier> sound;

    public SoundOutcome(OutcomePosition position, OutcomeField<Identifier> sound) {
        super();
        this.setPosition(position);
        this.sound = sound;
    }

    @Override
    public void apply(OutcomeContext context) {
        SoundEvent sound = Registries.SOUND_EVENT.get(this.sound.get(context));

        Vec3d pos = getPosition(context);

        context.world().playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
