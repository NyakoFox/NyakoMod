package gay.nyako.nyakomod;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
}
