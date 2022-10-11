package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.access.TntEntityAccess;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.*;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class FlintAndSteelPlusItem extends Item {
    public FlintAndSteelPlusItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos;
        PlayerEntity playerEntity = context.getPlayer();
        World world = context.getWorld();

        if (playerEntity instanceof ServerPlayerEntity) {
            ItemStack itemStack = context.getStack();
            itemStack.damage(1, playerEntity, p -> p.sendToolBreakStatus(context.getHand()));
        }

        BlockState blockState = world.getBlockState(blockPos = context.getBlockPos());

        if (!world.isClient) {
            TntEntity tntEntity = new TntEntity(world, (double) blockPos.getX() + 0.5, blockPos.getY(), (double) blockPos.getZ() + 0.5, playerEntity);
            world.spawnEntity(tntEntity);
            world.playSound(null, tntEntity.getX(), tntEntity.getY(), tntEntity.getZ(), SoundEvents.ENTITY_TNT_PRIMED, SoundCategory.BLOCKS, 1.0f, 1.0f);
            world.emitGameEvent(playerEntity, GameEvent.PRIME_FUSE, blockPos);
            ((TntEntityAccess) tntEntity).setCopyBlockState(blockState);
        }

        world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.REDRAW_ON_MAIN_THREAD);


        return ActionResult.success(world.isClient());
    }
}
