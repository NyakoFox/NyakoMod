package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.BombEntity;
import gay.nyako.nyakomod.entity.GrenadeEntity;
import gay.nyako.nyakomod.entity.SafetyBombEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

public class SafetyBombItem extends BombItem {
    public SafetyBombItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    protected BombEntity getBombEntity(World world, PlayerEntity user) {
        return new SafetyBombEntity(world, user);
    }
}
