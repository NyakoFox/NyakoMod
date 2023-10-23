package gay.nyako.nyakomod.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

public class RodOfDiscordItem extends Item {
    public RodOfDiscordItem(FabricItemSettings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        HitResult result = user.raycast(10d, 0.0f, false);
        double x = Math.round(result.getPos().getX()) - 0.5f;
        double y = Math.round(result.getPos().getY());
        double z = Math.round(result.getPos().getZ()) - 0.5f;
        user.requestTeleportAndDismount(x, y, z);
        world.playSound(x, y, z, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f, true);

        user.getItemCooldownManager().set(this, 30);

        return TypedActionResult.success(stack);
    }
}
