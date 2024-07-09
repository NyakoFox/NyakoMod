package gay.nyako.nyakomod.item;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FishingCrateBlockItem extends BlockItem {
    private final Identifier lootTable;

    public FishingCrateBlockItem(Block block, Settings settings, Identifier lootTable) {
        super(block, settings);
        this.lootTable = lootTable;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient()) return TypedActionResult.pass(itemStack);
        user.playSound(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.MASTER, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);

        // give the loot table

        LootContextParameterSet.Builder builder = new LootContextParameterSet.Builder((ServerWorld)world).add(LootContextParameters.TOOL, itemStack).add(LootContextParameters.ORIGIN, Vec3d.ofCenter(user.getBlockPos()));
        builder.luck(user.getLuck()).add(LootContextParameters.THIS_ENTITY, user);


        for (ItemStack itemStack2 : world.getServer().getLootManager().getLootTable(this.lootTable).generateLoot(builder.build(LootContextTypes.FISHING))) {
            if (user.giveItemStack(itemStack2)) {
                user.getWorld().playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2f, ((user.getRandom().nextFloat() - user.getRandom().nextFloat()) * 0.7f + 1.0f) * 2.0f);
                continue;
            }
            ItemEntity itemEntity = user.dropItem(itemStack2, false);
            if (itemEntity == null) continue;
            itemEntity.resetPickupDelay();
            itemEntity.setOwner(user.getUuid());
        }


        itemStack.decrement(1);
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.setStackInHand(hand, itemStack);

        return TypedActionResult.success(itemStack, true);
    }
}
