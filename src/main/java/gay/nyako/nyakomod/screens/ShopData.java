package gay.nyako.nyakomod.screens;

import java.util.ArrayList;
import java.util.List;

public class ShopData {
    public List<ShopEntry> entries;
    public String id;

    public ShopData(String id) {
        this.entries = new ArrayList<>();
        this.id = id;
    }

    public void add(ShopEntry entry) {
        this.entries.add(entry);
    }
}
