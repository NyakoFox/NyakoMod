package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.BombEntity;
import gay.nyako.nyakomod.entity.GrenadeEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class GrenadeItem extends BombItem {
    public GrenadeItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    protected BombEntity getBombEntity(World world, PlayerEntity user) {
        return new GrenadeEntity(world, user);
    }
}
