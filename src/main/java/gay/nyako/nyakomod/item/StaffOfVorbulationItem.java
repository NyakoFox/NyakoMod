package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.item.gacha.GachaItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;

public class StaffOfVorbulationItem extends GachaItem {

    public StaffOfVorbulationItem(Settings settings) {
        super(settings, 4, (MutableText) Text.of("Vorbulation has never been easier!"));
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        user.world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.BLOCK_HONEY_BLOCK_BREAK, SoundCategory.PLAYERS, 1f, 0.8f, true);
        entity.setGlowing(!entity.isGlowing());
        return ActionResult.SUCCESS;
    }
}
