package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

public class EchoPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = Properties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_SHAPE = Block.createCuboidShape(0.0, 0.0, 6.0, 16.0, 16.0, 10.0);
    protected static final VoxelShape Z_SHAPE = Block.createCuboidShape(6.0, 0.0, 0.0, 10.0, 16.0, 16.0);

    public EchoPortalBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState((this.stateManager.getDefaultState()).with(AXIS, Direction.Axis.X));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        if (Objects.requireNonNull(state.get(AXIS)) == Direction.Axis.Z) {
            return Z_SHAPE;
        }
        return X_SHAPE;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        //boolean bl;
        //Direction.Axis axis = direction.getAxis();
        //Direction.Axis axis2 = state.get(AXIS);
        //boolean bl2 = bl = axis2 != axis && axis.isHorizontal();
        //if (bl || neighborState.isOf(this)) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        //}
        //return Blocks.AIR.getDefaultState();
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayerEntity)
        {
            ServerWorld currentWorld = (ServerWorld)world;
            ServerWorld echolandsWorld = currentWorld.getServer().getWorld(NyakoMod.ECHOLANDS_KEY);
            BlockPos spawnPosition = echolandsWorld.getSpawnPos();
            serverPlayerEntity.teleport(echolandsWorld, spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ(), 0.0f, 0);

            // Place the portal!
            StructureTemplateManager structureTemplateManager = echolandsWorld.getServer().getStructureTemplateManager();
            Optional<StructureTemplate> optional = structureTemplateManager.getTemplate(Identifier.of("nyakomod", "echo_portal"));

            StructureTemplate template = optional.get();

            StructurePlacementData structurePlacementData = new StructurePlacementData().setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true);

            BlockPos blockPos2 = spawnPosition.add(-11, -1, -2);
            template.place(echolandsWorld, blockPos2, blockPos2, structurePlacementData, echolandsWorld.getRandom(), 2);

            // Place a spawn pad underneath the player
            for (int x = -2; x <= 1; x++)
            {
                for (int z = -2; z <= 1; z++)
                {
                    BlockPos spawnPadPos = spawnPosition.add(x, -1, z);
                    echolandsWorld.setBlockState(spawnPadPos, Blocks.REINFORCED_DEEPSLATE.getDefaultState());
                }
            }
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound((double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5f, random.nextFloat() * 0.4f + 0.8f, false);
        }
        for (int i = 0; i < 4; ++i) {
            double d = (double)pos.getX() + random.nextDouble();
            double e = (double)pos.getY() + random.nextDouble();
            double f = (double)pos.getZ() + random.nextDouble();
            double g = ((double)random.nextFloat() - 0.5) * 0.5;
            double h = ((double)random.nextFloat() - 0.5) * 0.5;
            double j = ((double)random.nextFloat() - 0.5) * 0.5;
            int k = random.nextInt(2) * 2 - 1;
            if (world.getBlockState(pos.west()).isOf(this) || world.getBlockState(pos.east()).isOf(this)) {
                f = (double)pos.getZ() + 0.5 + 0.25 * (double)k;
                j = random.nextFloat() * 2.0f * (float)k;
            } else {
                d = (double)pos.getX() + 0.5 + 0.25 * (double)k;
                g = random.nextFloat() * 2.0f * (float)k;
            }
            //world.addParticle(NyakoParticleTypes.ECHO_PORTAL, d, e, f, g, h, j);
        }
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        switch (rotation) {
            case COUNTERCLOCKWISE_90, CLOCKWISE_90 -> {
                switch (state.get(AXIS)) {
                    case X -> {
                        return state.with(AXIS, Direction.Axis.Z);
                    }
                    case Z -> {
                        return state.with(AXIS, Direction.Axis.X);
                    }
                }
                return state;
            }
        }
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction.Axis finalAxis = Direction.Axis.X;
        switch (ctx.getHorizontalPlayerFacing().getAxis()) {
            case X: finalAxis = Direction.Axis.Z; break;
            case Z: finalAxis = Direction.Axis.X; break;
        }
        return this.getDefaultState().with(Properties.HORIZONTAL_AXIS, finalAxis);
    }
}
