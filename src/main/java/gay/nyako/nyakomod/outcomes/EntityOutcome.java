package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class EntityOutcome extends PositionedOutcome {
    private final OutcomeField<Identifier> entity;
    private final NbtCompound nbt;

    public EntityOutcome(OutcomePosition position, OutcomeField<Identifier> entity) {
        super();
        this.setPosition(position);
        this.entity = entity;
        this.nbt = null;
    }

    public EntityOutcome(OutcomePosition position, OutcomeField<Identifier> entity, NbtCompound nbt) {
        super();
        this.setPosition(position);
        this.entity = entity;
        this.nbt = nbt;
    }

    @Override
    public void apply(OutcomeContext context) {

        Entity entity = Registries.ENTITY_TYPE.get(this.entity.get(context)).create(context.world());
        if (entity == null)
        {
            return;
        }

        if (nbt != null)
        {
            entity.readNbt(nbt);
        }

        Vec3d pos = getPosition(context);

        entity.refreshPositionAndAngles(pos.getX(), pos.getY(), pos.getZ(), context.world().random.nextFloat() * 360.0F, 0.0F);
        context.world().spawnEntity(entity);
    }
}
