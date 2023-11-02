package gay.nyako.nyakomod.access;

import gay.nyako.nyakomod.StickerPackCollection;

public interface PlayerEntityAccess {
    int getMilk();
    void setMilk(int milk);
    void addMilk(int milk);
    int getMilkSaturation();
    void setMilkSaturation(int milk);
    void addMilkSaturation(int milk);
    void setMilkTimer(int time);
    int getMilkTimer();

    StickerPackCollection getStickerPackCollection();
    void setStickerPackCollection(StickerPackCollection stickerPackCollection);
}
