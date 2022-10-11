package gay.nyako.nyakomod.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SoundBlock extends Block {
    public static final BooleanProperty POWERED = Properties.POWERED;
    private final SoundEvent sound;

    public SoundBlock(Settings settings, SoundEvent sound) {
        super(settings);

        this.sound = sound;
        this.setDefaultState(this.stateManager.getDefaultState().with(POWERED, false));
    }

    @Override
    public void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        boolean bl = world.isReceivingRedstonePower(pos);
        if (bl != state.get(POWERED)) {
            if (bl) {
                world.playSound(null, pos, sound, SoundCategory.BLOCKS, 1.0F, 1.0F);
            }
            world.setBlockState(pos, state.with(POWERED, bl), Block.NOTIFY_ALL);
        }
    }

    /*@Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), NyakoMod.SPUNCH_BLOCK_SOUND_EVENT, SoundCategory.BLOCKS, 1.0F, 1.0F, true);
        if (world.isClient) {
            return ActionResult.SUCCESS;
        }
        return ActionResult.CONSUME;
    }*/

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(POWERED);
    }
}
