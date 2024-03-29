package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.utils.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.NyakoNetworking;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.*;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CunkShopHandledScreen extends BaseUIModelHandledScreen<FlowLayout, CunkShopScreenHandler> {
    public int selectedEntry = 0;
    public int purchaseAmount = 1;
    public int oldValue = 0;
    public FlowLayout layout;

    public CunkShopHandledScreen(CunkShopScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.asset(new Identifier("nyakomod", "cunk_shop")));
        this.titleY = 69420;
        this.playerInventoryTitleY = 69420;
    }

    public List<ShopEntry> getEntries() {
        return ShopEntries.getShop(handler.shopId).entries;
    }

    @Override
    protected void handledScreenTick() {
        // TODO: not... do this
        if (layout != null) {
            var count = CunkCoinUtils.countInventoryCoins(client.player.getInventory()) + CunkCoinUtils.countInventoryCoins(client.player.getEnderChestInventory());
            if (count != oldValue) {
                oldValue = count;
                updateCurrentAmount(layout);
            }
        }
        super.handledScreenTick();
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        layout = rootComponent;
        var shopEntries = getEntries();

        for (ShopEntry entry : shopEntries) {
            Consumer<ButtonComponent> shopButtonPress = button -> {
                selectEntry(shopEntries.indexOf(entry), rootComponent);
            };

            rootComponent.childById(FlowLayout.class,"button_list").child(
                    Components.button(entry.name(), shopButtonPress)
                            .horizontalSizing(Sizing.fixed(88))
                            .cursorStyle(CursorStyle.POINTER)
            );
        }

        selectEntry(0, rootComponent);

        rootComponent.childById(ButtonComponent.class, "remove_16").onPress(button -> {
            changePurchaseAmount(rootComponent, -16);
        });
        rootComponent.childById(ButtonComponent.class, "remove_1").onPress(button -> {
            changePurchaseAmount(rootComponent, -1);
        });
        rootComponent.childById(ButtonComponent.class, "add_1").onPress(button -> {
            changePurchaseAmount(rootComponent, 1);
        });
        rootComponent.childById(ButtonComponent.class, "add_16").onPress(button -> {
            changePurchaseAmount(rootComponent, 16);
        });

        rootComponent.childById(ButtonComponent.class, "buy").onPress(button -> {
            var entry = shopEntries.get(selectedEntry);
            var amount = entry.price() * purchaseAmount;
            if (amount < 0) {
                return;
            }

            var player = client.player;
            var count = CunkCoinUtils.countInventoryCoins(player.getInventory()) + CunkCoinUtils.countInventoryCoins(player.getEnderChestInventory());

            if (count < amount) {
                return;
            }

            PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
            passedData.writeIdentifier(handler.shopId);
            passedData.writeInt(selectedEntry);
            passedData.writeInt(purchaseAmount);
            ClientPlayNetworking.send(NyakoNetworking.CUNK_SHOP_PURCHASE, passedData);
            player.getInventory().markDirty();

        });
    }

    public void changePurchaseAmount(FlowLayout rootComponent, int amount) {
        var shopEntries = getEntries();
        purchaseAmount = Math.max(Math.min(purchaseAmount + amount, 128), 1);
        ShopEntry shopEntry = shopEntries.get(selectedEntry);
        // Prevent overflows
        while ((shopEntry.price() * purchaseAmount) < 0) {
            purchaseAmount--;
            if (purchaseAmount <= 0) {
                purchaseAmount = 1;
                break;
            }
        }
        updatePurchaseAmount(rootComponent);
    }

    public void selectEntry(int entry, FlowLayout rootComponent) {
        var shopEntries = getEntries();
        purchaseAmount = 1;
        selectedEntry = entry;
        ShopEntry shopEntry = shopEntries.get(entry);

        rootComponent.childById(FlowLayout.class, "result-list").clearChildren();

        for (ItemStack item : shopEntry.stacks()) {
            addItem(item, rootComponent);
        }

        rootComponent.childById(LabelComponent.class, "purchase-header").text(shopEntry.name());

        rootComponent.childById(LabelComponent.class,"entry-description").text(shopEntry.description());
        var gridLayout = rootComponent.childById(GridLayout.class,"stickers");
        List<Component> children = new ArrayList<>(gridLayout.children());
        children.forEach(
                gridLayout::removeChild
        );

        if (shopEntry.pack() != null)
        {
            gridLayout.sizing(Sizing.content(2));
            List<Identifier> stickers = new ArrayList<>();
            MinecraftClient.getInstance().getResourceManager()
                    .findAllResources("textures/sticker/" + shopEntry.pack(), id -> id.getPath().endsWith(".png")).forEach(
                            (resourceID, resource) -> {
                                stickers.add(resourceID);
                            }
                    );
            for (int i = 0; i < stickers.size(); i++)
            {
                int x = i % 4;
                int y = i / 4;
                gridLayout.child(
                        Components.texture(
                                stickers.get(i),
                                0, 0,
                                32, 32,
                                32, 32
                        ),
                        y,
                        x
                );
            }
        }
        else
        {
            gridLayout.verticalSizing(Sizing.fixed(0));
        }

        updatePurchaseAmount(rootComponent);
    }

    public void updatePurchaseAmount(FlowLayout rootComponent) {
        var shopEntries = getEntries();
        var price = rootComponent.childById(FlowLayout.class,"price-display");
        price.clearChildren();

        ShopEntry shopEntry = shopEntries.get(selectedEntry);

        var multipliedPrice = shopEntry.price() * purchaseAmount;
        if (multipliedPrice < 0) {
            price.child(
                    Components.label(Text.of("Max exceeded!"))
                            .color(Color.ofFormatting(Formatting.DARK_GRAY))
            );
            return;
        }
        var split = CunkCoinUtils.valueToSplit(multipliedPrice);
        price.child(
                Components.label(Text.of("Price: " + purchaseAmount + " for "))
                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
        );

        List<CunkCoinUtils.CoinValue> coinValueList = new ArrayList<>();
        coinValueList.add(CunkCoinUtils.CoinValue.NETHERITE);
        coinValueList.add(CunkCoinUtils.CoinValue.DIAMOND);
        coinValueList.add(CunkCoinUtils.CoinValue.EMERALD);
        coinValueList.add(CunkCoinUtils.CoinValue.GOLD);
        coinValueList.add(CunkCoinUtils.CoinValue.COPPER);

        // split is a Map, loop through the keys
        for (var key : coinValueList) {
            if (split.get(key) > 0) {
                price.child(
                        Components.label(Text.of(String.valueOf(split.get(key))))
                                .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                .margins(Insets.of(0, 0, 0, 4))
                );
                var item = NyakoItems.COPPER_COIN;
                switch (key) {
                    case COPPER -> item = NyakoItems.COPPER_COIN;
                    case GOLD -> item = NyakoItems.GOLD_COIN;
                    case EMERALD -> item = NyakoItems.EMERALD_COIN;
                    case DIAMOND -> item = NyakoItems.DIAMOND_COIN;
                    case NETHERITE -> item = NyakoItems.NETHERITE_COIN;
                }
                price.child(
                        Components.item(new ItemStack(item))
                                .margins(Insets.of(0, 0, 0, 8))
                );
            }
        }
        updateCurrentAmount(rootComponent);
    }

    public void updateCurrentAmount(FlowLayout rootComponent) {
        var count = CunkCoinUtils.countInventoryCoins(client.player.getInventory()) + CunkCoinUtils.countInventoryCoins(client.player.getEnderChestInventory());

        var currentMoney = rootComponent.childById(FlowLayout.class,"current_money");
        currentMoney.clearChildren();

        var split = CunkCoinUtils.valueToSplit(count);
        currentMoney.child(
                Components.label(Text.of("Money: "))
                        .color(Color.ofFormatting(Formatting.DARK_GRAY))
        );

        List<CunkCoinUtils.CoinValue> coinValueList = new ArrayList<>();
        coinValueList.add(CunkCoinUtils.CoinValue.NETHERITE);
        coinValueList.add(CunkCoinUtils.CoinValue.DIAMOND);
        coinValueList.add(CunkCoinUtils.CoinValue.EMERALD);
        coinValueList.add(CunkCoinUtils.CoinValue.GOLD);
        coinValueList.add(CunkCoinUtils.CoinValue.COPPER);

        // split is a Map, loop through the keys
        for (var key : coinValueList) {
            if (split.get(key) > 0) {
                currentMoney.child(
                        Components.label(Text.of(String.valueOf(split.get(key))))
                                .color(Color.ofFormatting(Formatting.DARK_GRAY))
                                .margins(Insets.of(0, 0, 0, 4))
                );
                var item = NyakoItems.COPPER_COIN;
                switch (key) {
                    case COPPER -> item = NyakoItems.COPPER_COIN;
                    case GOLD -> item = NyakoItems.GOLD_COIN;
                    case EMERALD -> item = NyakoItems.EMERALD_COIN;
                    case DIAMOND -> item = NyakoItems.DIAMOND_COIN;
                    case NETHERITE -> item = NyakoItems.NETHERITE_COIN;
                }
                currentMoney.child(
                        Components.item(new ItemStack(item))
                                .margins(Insets.of(0, 0, 0, 8))
                );
            }
        }
    }

    public void addItem(ItemStack stack, FlowLayout rootComponent) {
        var tooltip = stack.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.ADVANCED);
        var tooltipComponents = new ArrayList<TooltipComponent>();
        for (var text : tooltip) {
            tooltipComponents.add(TooltipComponent.of(text.asOrderedText()));
        }

        rootComponent.childById(FlowLayout.class, "result-list").child(
                Components.item(stack)
                        .margins(Insets.of(0, 0, 0, 4))
                        .tooltip(tooltipComponents)
        ).child(
                Components.label(Text.literal("x " + stack.getCount()).formatted(Formatting.DARK_GRAY))
                        .margins(Insets.of(0, 0, 0, 4))
        );
    }
}
