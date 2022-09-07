package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoMod;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CoinItem extends Item {
    protected int coinValue = 1;
    protected String coinKey = "copper";

    public CoinItem(Settings settings) {
        super(settings);
    }

    public CoinItem(Settings settings, String key, int value) {
        super(settings);

        this.coinKey = key;
        this.coinValue = value;
    }

    public String getCoinKey() {
      return coinKey;
    }

    public int getCoinValue() {
      return coinValue;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        var hungryBag = CunkCoinUtils.getHungryBag(user);
        if (hungryBag == null) {
            return super.use(world, user, hand);
        }
        NbtCompound tag = hungryBag.getOrCreateNbt();
        var coin = (CoinItem) stack.getItem();
        tag.putInt(coin.getCoinKey(), tag.getInt(coin.getCoinKey()) + stack.getCount());
        ((BagOfCoinsItem) hungryBag.getItem()).rebalance(hungryBag);
        stack.setCount(0);
        user.playSound(NyakoMod.COIN_COLLECT_SOUND_EVENT, SoundCategory.MASTER, 0.7f, 1f);
        return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, user.getStackInHand(hand));
    }
}
