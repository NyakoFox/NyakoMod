package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.BobmEntity;
import gay.nyako.nyakomod.entity.BombEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class BobmItem extends BombItem {
    public BobmItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    protected BombEntity getBombEntity(World world, PlayerEntity user) {
        return new BobmEntity(world, user);
    }
}
