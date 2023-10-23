package gay.nyako.nyakomod.screens;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class ShopData {
    public List<ShopEntry> entries;
    public Identifier id;

    public ShopData(Identifier id) {
        this.entries = new ArrayList<>();
        this.id = id;
    }

    public void add(ShopEntry entry) {
        this.entries.add(entry);
    }
}
