package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MatterVortexBlock extends Block {

    public static long USAGE_PRICE = 1000L;

    public MatterVortexBlock(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        var count = NyakoMod.countInventoryCoins(player.getInventory()) + NyakoMod.countInventoryCoins(player.getEnderChestInventory());
        if (count < USAGE_PRICE) return ActionResult.SUCCESS;

        NyakoMod.removeCoins(player, USAGE_PRICE);

        player.sendMessage(Text.of("Yummy meal (thank you) Yum yum"), true);

        player.playSound(SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 1f, 1f);
        player.playSound(SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 1f, 1f);

        return ActionResult.SUCCESS;
    }
}