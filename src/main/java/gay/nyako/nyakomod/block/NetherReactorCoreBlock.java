package gay.nyako.nyakomod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.StructureTemplateManager;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Optional;

public class NetherReactorCoreBlock extends Block {
    public static final IntProperty STAGE = IntProperty.of("stage", 0, 2);

    public NetherReactorCoreBlock(Settings settings) {
        super(settings.luminance((state) -> state.get(STAGE) == 1 ? 15 : 0));
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
        if (world.isClient()) return ActionResult.SUCCESS;

        if (!checkStructure(world, pos)) {
            player.sendMessage(Text.of("Not the correct pattern!"));
            return ActionResult.SUCCESS;
        }

        // Check distance of all players
        for (PlayerEntity p : world.getPlayers()) {
            if (p.getBlockPos().getManhattanDistance(pos) > 10) {
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

        StructureTemplateManager structureTemplateManager = world.getServer().getStructureTemplateManager();

        Optional<StructureTemplate> optional = structureTemplateManager.getTemplate(Identifier.of("nyakomod", "spire"));

        optional.ifPresent(structureTemplate -> this.place(pos, (ServerWorld) world, structureTemplate));

        world.setBlockState(pos, state.with(STAGE, 1));

        return ActionResult.SUCCESS;
    }

    public void place(BlockPos blockPos, ServerWorld world, StructureTemplate template) {
        StructurePlacementData structurePlacementData = new StructurePlacementData().setMirror(BlockMirror.NONE).setRotation(BlockRotation.NONE).setIgnoreEntities(true);
        //if (this.integrity < 1.0f) {
        //    structurePlacementData.clearProcessors().addProcessor(new BlockRotStructureProcessor(MathHelper.clamp(this.integrity, 0.0f, 1.0f))).setRandom(StructureBlockBlockEntity.createRandom(this.seed));
        //}
        BlockPos blockPos2 = blockPos.add(-8, -3, -8);
        template.place(world, blockPos2, blockPos2, structurePlacementData, world.getRandom(), 2);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }
}
