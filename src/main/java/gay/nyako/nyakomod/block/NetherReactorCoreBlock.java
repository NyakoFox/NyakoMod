package gay.nyako.nyakomod.block;

import com.mojang.serialization.MapCodec;
import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class NetherReactorCoreBlock extends BlockWithEntity {
    public static final MapCodec<NetherReactorCoreBlock> CODEC = NetherReactorCoreBlock.createCodec(NetherReactorCoreBlock::new);
    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);

    public NetherReactorCoreBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(STAGE, 0));
    }

    public boolean checkStructure(World world, BlockPos pos) {
        // First layer
        if (!world.getBlockState(pos.add(0, -1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(1, -1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(0, -1, 1)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(-1, -1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(0, -1, -1)).isOf(Blocks.COBBLESTONE)) return false;

        // Check the corners of the first layer
        if (!world.getBlockState(pos.add(1, -1, 1)).isOf(Blocks.GOLD_BLOCK)) return false;
        if (!world.getBlockState(pos.add(-1, -1, 1)).isOf(Blocks.GOLD_BLOCK)) return false;
        if (!world.getBlockState(pos.add(-1, -1, -1)).isOf(Blocks.GOLD_BLOCK)) return false;
        if (!world.getBlockState(pos.add(1, -1, -1)).isOf(Blocks.GOLD_BLOCK)) return false;

        // Second layer
        if (!world.getBlockState(pos.add(1, 0, 1)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(-1, 0, 1)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(-1, 0, -1)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(1, 0, -1)).isOf(Blocks.COBBLESTONE)) return false;

        // Third layer
        if (!world.getBlockState(pos.add(0, 1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(1, 1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(0, 1, 1)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(-1, 1, 0)).isOf(Blocks.COBBLESTONE)) return false;
        if (!world.getBlockState(pos.add(0, 1, -1)).isOf(Blocks.COBBLESTONE)) return false;

        return true;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (state.get(STAGE) != 0) {
            return ActionResult.PASS;
        }

        if (world.isClient()) return ActionResult.SUCCESS;

        if (!checkStructure(world, pos)) {
            player.sendMessage(Text.of("Not the correct pattern!"));
            return ActionResult.SUCCESS;
        }

        // Check distance of all players
        for (PlayerEntity p : world.getPlayers()) {
            if ((p.getBlockPos().getManhattanDistance(pos) > 6) || (p.getBlockPos().getY() != pos.getY() - 1)) {
                player.sendMessage(Text.of("All players need to be close to the reactor."));
                return ActionResult.SUCCESS;
            }
        }

        // Check if Y is between 4 and 328. Note that in pocket edition, it's 96.
        if (pos.getY() < 4 || pos.getY() > 328) {
            player.sendMessage(Text.of("The nether reactor needs to be built lower down."));
            return ActionResult.SUCCESS;
        }

        player.sendMessage(Text.of("Active!"));

        place(pos, (ServerWorld) world);

        world.setBlockState(pos, state.with(STAGE, 1));
        // Get block entity
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof NetherReactorCoreBlockEntity) {
            NetherReactorCoreBlockEntity netherReactorCoreBlockEntity = (NetherReactorCoreBlockEntity) blockEntity;
            netherReactorCoreBlockEntity.setStage(1);
        }

        return ActionResult.SUCCESS;
    }

    public static void place(BlockPos blockPos, ServerWorld world) {
        StructureTemplateManager structureTemplateManager = world.getServer().getStructureTemplateManager();
        Optional<StructureTemplate> optional = structureTemplateManager.getTemplate(Identifier.of("nyakomod", "spire"));

        StructureTemplate template = optional.get();

        StructurePlacementData structurePlacementData = new StructurePlacementData().setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true);

        BlockPos blockPos2 = blockPos.add(-8, -3, -8);
        template.place(world, blockPos2, blockPos2, structurePlacementData, world.getRandom(), 2);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new NetherReactorCoreBlockEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        // With inheriting from BlockWithEntity this defaults to INVISIBLE, so we need to change that!
        return BlockRenderType.MODEL;
    }
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, NyakoEntities.NETHER_REACTOR_ENTITY, (world1, pos, state1, be) -> NetherReactorCoreBlockEntity.tick(world1, pos, state1, be));
    }
}
