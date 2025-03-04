package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ItemOutcome extends PositionedOutcome {
    private final OutcomeField<Identifier> item;
    private final OutcomeField<Integer> count;
    private final NbtCompound nbt;

    public ItemOutcome(OutcomePosition position, OutcomeField<Identifier> item, OutcomeField<Integer> count) {
        super();
        this.setPosition(position);
        this.item = item;
        this.count = count;
        this.nbt = null;
    }

    public ItemOutcome(OutcomePosition position, OutcomeField<Identifier> item, OutcomeField<Integer> count, NbtCompound nbt) {
        super();
        this.setPosition(position);
        this.item = item;
        this.count = count;
        this.nbt = nbt;
    }

    @Override
    public void apply(OutcomeContext context) {
        ItemStack stack = new ItemStack(Registries.ITEM.get(item.get(context)), count.get(context));
        if (nbt != null)
        {
            stack.setNbt(nbt);
        }

        Vec3d pos = getPosition(context);

        double d = (double)EntityType.ITEM.getHeight() / 2.0;
        double e = pos.getX() + MathHelper.nextDouble(context.world().random, -0.25, 0.25);
        double f = pos.getY() + MathHelper.nextDouble(context.world().random, -0.25, 0.25) - d;
        double g = pos.getZ() + MathHelper.nextDouble(context.world().random, -0.25, 0.25);

        ItemEntity itemEntity = new ItemEntity(context.world(), e, f, g, stack);
        itemEntity.setToDefaultPickupDelay();
        context.world().spawnEntity(itemEntity);
    }
}
