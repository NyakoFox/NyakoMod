package gay.nyako.nyakomod.block;

import gay.nyako.nyakomod.NyakoEntities;
import gay.nyako.nyakomod.SyncingBlockEntity;
import gay.nyako.nyakomod.screens.NBPScreenHandler;
import gay.nyako.nyakomod.SongPlayer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NoteBlockPlusBlockEntity extends SyncingBlockEntity implements NamedScreenHandlerFactory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(9, ItemStack.EMPTY);

    private final SongPlayer songPlayer;

    public String songContents;

    public NoteBlockPlusBlockEntity(BlockPos pos, BlockState state) {
        super(NyakoEntities.NOTE_BLOCK_PLUS_ENTITY, pos, state);

        songPlayer = new SongPlayer(this);
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        //We provide *this* to the screenHandler as our class Implements Inventory
        //Only the Server has the Inventory at the start, this will be synced to the client in the ScreenHandler
        return new NBPScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, player.getBlockPos()));
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable(getCachedState().getBlock().getTranslationKey());
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, this.inventory);
        songContents = nbt.getString("songContents");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, this.inventory);
        if (songContents != null) {
            nbt.putString("songContents", songContents);
        }
    }

    public SongPlayer getSongPlayer() {
        return songPlayer;
    }

    public static void tick(World world, BlockPos pos, BlockState state, NoteBlockPlusBlockEntity blockEntity) {
        blockEntity.getSongPlayer().tick();
    }
}