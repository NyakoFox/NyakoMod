package gay.nyako.nyakomod.item.gacha;

import gay.nyako.nyakomod.NyakoModSoundEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DiscordGachaItem extends GachaItem {

    public DiscordGachaItem(Settings settings) {
        super(settings, 3, (MutableText) Text.of("Better check your notifications!"));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        world.playSoundFromEntity(null, user, NyakoModSoundEvents.DISCORD, SoundCategory.PLAYERS, 1f, 1f);
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}
