package gay.nyako.nyakomod;

import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.*;

public class StickerPackCollection extends ArrayList<String> {

    public NbtElement toNbt() {
        var list = new NbtList();
        for (var sticker : this) {
            list.add(NbtString.of(sticker));
        }
        return list;
    }

    public void fromNbt(NbtElement nbt) {
        if (nbt instanceof NbtList list) {
            for (var element : list) {
                if (element instanceof NbtString) {
                    this.add(element.asString());
                }
            }
        }
    }

    public boolean hasStickerPack(String sticker) {
        return this.contains(sticker);
    }

    public void addStickerPack(String sticker) {
        this.add(sticker);
    }

    public void removeStickerPack(String sticker) {
        this.remove(sticker);
    }
}
