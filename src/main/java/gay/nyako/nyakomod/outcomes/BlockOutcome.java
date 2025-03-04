package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import gay.nyako.nyakomod.outcomes.positions.OutcomePosition;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public class BlockOutcome extends PositionedOutcome {
    private final OutcomeField<Identifier> block;
    private final NbtCompound nbt;
    private final HashMap<String, String> properties;

    public BlockOutcome(OutcomePosition position, OutcomeField<Identifier> block) {
        super();
        this.setPosition(position);
        this.block = block;
        this.nbt = null;
        this.properties = new HashMap<>();
    }

    public BlockOutcome(OutcomePosition position, OutcomeField<Identifier> block, HashMap<String, String> properties) {
        super();
        this.setPosition(position);
        this.block = block;
        this.nbt = null;
        this.properties = properties;
    }

    public BlockOutcome(OutcomePosition position, OutcomeField<Identifier> block, HashMap<String, String> properties, NbtCompound nbt) {
        super();
        this.setPosition(position);
        this.block = block;
        this.nbt = nbt;
        this.properties = properties;
    }

    // Generic helper method to avoid wildcard issues
    private static <T extends Comparable<T>> BlockState setBlockState(BlockState blockState, Property<T> property, String value) {
        var parsed = property.parse(value);
        if (parsed.isPresent()) {
            blockState = blockState.with(property, parsed.get());
        }
        return blockState;
    }

    @Override
    public void apply(OutcomeContext context) {
        Block block = Registries.BLOCK.get(this.block.get(context));

        var stateManager = block.getStateManager();
        var blockState = block.getDefaultState();

        for (var entry : properties.entrySet())
        {
            var property = stateManager.getProperty(entry.getKey());
            if (property != null)
            {
                blockState = setBlockState(blockState, property, entry.getValue());
            }
        }

        Vec3d pos = getPosition(context);
        var newBlockPos = new BlockPos((int) Math.floor(pos.getX()), (int) Math.floor(pos.getY()), (int) Math.floor(pos.getZ()));

        var couldSetBlock = context.world().setBlockState(newBlockPos, blockState, Block.NOTIFY_LISTENERS);

        if (couldSetBlock)
        {
            if (nbt != null)
            {
                var blockEntity = context.world().getBlockEntity(newBlockPos);
                if (blockEntity != null)
                {
                    blockEntity.readNbt(nbt);
                }
            }
            context.world().updateNeighbors(newBlockPos, block);
        }
    }
}
