package gay.nyako.nyakomod.item;

import gay.nyako.nyakomod.entity.BombEntity;
import gay.nyako.nyakomod.entity.GrenadeEntity;
import gay.nyako.nyakomod.entity.MossBombEntity;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

public class MossBombItem extends BombItem {
    public MossBombItem(FabricItemSettings fabricItemSettings) {
        super(fabricItemSettings);
    }

    @Override
    protected BombEntity getBombEntity(World world, PlayerEntity user) {
        return new MossBombEntity(world, user);
    }
}
