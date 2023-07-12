package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.ZombifiedPiglinEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class NetherReactorCoreBlockEntity extends BlockEntity {
    private int stage = 0;
    private int timer = 0;

    public NetherReactorCoreBlockEntity(BlockPos pos, BlockState state) {
        super(NyakoEntities.NETHER_REACTOR_ENTITY, pos, state);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        nbt.putInt("stage", stage);
        nbt.putInt("timer", timer);
        super.writeNbt(nbt);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        stage = nbt.getInt("stage");
        timer = nbt.getInt("timer");
        super.readNbt(nbt);
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getStage() {
        return stage;
    }

    public static void tick(World world, BlockPos pos, BlockState state, NetherReactorCoreBlockEntity blockEntity) {
        if (world.isClient) return;

        if (blockEntity.stage == 1) {
            blockEntity.timer++;
            switch (blockEntity.timer)
            {
                case 20:
                    world.setBlockState(pos.add(0, -1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(1, -1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(0, -1, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(0, -1, -1), Blocks.GLOWSTONE.getDefaultState());
                    break;
                case 40:
                    world.setBlockState(pos.add(1, 0, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, 0, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(1, 0, -1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, 0, -1), Blocks.GLOWSTONE.getDefaultState());
                    break;
                case 60:
                    world.setBlockState(pos.add(0, 1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(1, 1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, 1, 0), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(0, 1, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(0, 1, -1), Blocks.GLOWSTONE.getDefaultState());
                    break;
                case 100:
                    world.setBlockState(pos.add(1, -1, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, 1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(1, -1, -1), Blocks.GLOWSTONE.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, -1), Blocks.GLOWSTONE.getDefaultState());
                    break;
                case 140:
                case 180:
                case 220:
                case 260:
                case 300:
                case 340:
                case 380:
                case 420:
                case 460:
                case 500:
                case 540:
                case 580:
                case 620:
                case 660:
                case 700:
                case 740:
                    for (int i = 0; i < 8; i++) {
                        double x = pos.getX() + world.getRandom().nextBetween(-6, 7) - 0.5;
                        double y = pos.getY() - 1;
                        double z = pos.getZ() + world.getRandom().nextBetween(-6, 7) - 0.5;
                        if (world.getRandom().nextBetween(0, 100) < 10) {
                            // Spawn a zombie pigmen
                            ZombifiedPiglinEntity piglin = EntityType.ZOMBIFIED_PIGLIN.create(world);
                            piglin.refreshPositionAndAngles(x, y, z, 0, 0);
                            world.spawnEntity(piglin);
                        }
                        else
                        {
                            // Spawn a random item
                            List<Item> items = new ArrayList<>();
                            items.add(Items.GLOWSTONE_DUST);
                            items.add(Items.QUARTZ);
                            items.add(Items.CACTUS);
                            items.add(Items.SUGAR_CANE);
                            items.add(Items.BROWN_MUSHROOM);
                            items.add(Items.RED_MUSHROOM);
                            items.add(Items.BOW);
                            items.add(Items.BOWL);
                            items.add(Items.BOOK);
                            items.add(Items.OAK_DOOR);
                            items.add(Items.RED_BED);
                            items.add(Items.PUMPKIN_SEEDS);
                            items.add(Items.MELON_SEEDS);
                            items.add(Items.PAINTING);
                            items.add(Items.BONE);

                            ItemEntity entity = new ItemEntity(world, x, y, z,
                                    new ItemStack(items.get(world.getRandom().nextInt(items.size())))
                            );
                            entity.refreshPositionAndAngles(x, y, z, 0, 0);
                            world.spawnEntity(entity);
                        }
                    }
                    break;
                case 780:
                    world.setBlockState(pos.add(0, 1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, 1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, 1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, 1, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, 1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, 1, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 1, -1), Blocks.OBSIDIAN.getDefaultState());
                    break;
                case 820:
                    world.setBlockState(pos.add(1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 0, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, 0, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, 0, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, 0, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 0, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, 0, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, 0, -1), Blocks.OBSIDIAN.getDefaultState());
                    break;
                case 860:
                    world.setBlockState(pos.add(0, -1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, -1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, 0), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, -1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(0, -1, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, -1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, 1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(1, -1, -1), Blocks.OBSIDIAN.getDefaultState());
                    world.setBlockState(pos.add(-1, -1, -1), Blocks.OBSIDIAN.getDefaultState());
                    break;
                case 900:
                    // End reaction
                    blockEntity.setStage(2);
                    world.setBlockState(pos, state.with(NetherReactorCoreBlock.STAGE, 2));
                    for (int x = -8; x <= 8; x++) {
                        for (int z = -8; z <= 8; z++) {
                            for (int y = -1; y <= 35; y++) {
                                if (world.getBlockState(pos.add(x, y, z)).getBlock() == Blocks.NETHERRACK) {
                                    if (world.getRandom().nextBetween(0, 100) < 25) {
                                        world.setBlockState(pos.add(x, y, z), Blocks.AIR.getDefaultState());
                                    }
                                }
                            }
                        }
                    }
                    break;
            }
        }
    }
}
