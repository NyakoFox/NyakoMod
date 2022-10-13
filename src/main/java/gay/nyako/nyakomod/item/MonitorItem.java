package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.MonitorEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class MonitorItem extends Item {
    public MonitorItem(Settings settings) {
        super(settings);
    }

    public ActionResult useOnBlock(ItemUsageContext context) {
        BlockPos blockPos = context.getBlockPos();
        Direction direction = context.getSide();
        BlockPos blockPos2 = blockPos.offset(direction);
        PlayerEntity playerEntity = context.getPlayer();
        ItemStack itemStack = context.getStack();

        if (playerEntity != null && !this.canPlaceOn(playerEntity, direction, itemStack, blockPos2)) {
            return ActionResult.FAIL;
        } else {
            World world = context.getWorld();
            var nbt = itemStack.getNbt();
            MonitorEntity monitorEntity = new MonitorEntity(world, blockPos2, direction);
            if (nbt != null && nbt.contains("monitor")) {
                var monitor = nbt.getCompound("monitor");
                monitorEntity.setMonitorWidth(monitor.getInt("width"));
                monitorEntity.setMonitorHeight(monitor.getInt("height"));
                monitorEntity.setURL(monitor.getString("url"));
            }

            if (monitorEntity.canStayAttached()) {
                if (!world.isClient) {
                    monitorEntity.onPlace();
                    world.spawnEntity(monitorEntity);
                }

                itemStack.decrement(1);
                return ActionResult.success(world.isClient);
            } else {
                return ActionResult.CONSUME;
            }
        }
    }

    protected boolean canPlaceOn(PlayerEntity player, Direction side, ItemStack stack, BlockPos pos) {
        return !player.world.isOutOfHeightLimit(pos) && player.canPlaceOn(pos, side, stack);
    }
}