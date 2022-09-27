package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.CunkCoinUtils;
import gay.nyako.nyakomod.NyakoModItem;
import gay.nyako.nyakomod.NyakoModNetworking;
import io.netty.buffer.Unpooled;
import io.wispforest.owo.ui.base.BaseUIModelHandledScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.CheckboxComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Color;
import io.wispforest.owo.ui.core.CursorStyle;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class NBPHandledScreen extends BaseUIModelHandledScreen<FlowLayout, NBPScreenHandler> {
    public FlowLayout layout;
    PlayerEntity player;

    public NBPHandledScreen(NBPScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title, FlowLayout.class, BaseUIModelScreen.DataSource.asset(new Identifier("nyakomod", "note_block_plus")));
        this.titleY = 69420;
        this.playerInventoryTitleY = 69420;

        player = inventory.player;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        layout = rootComponent;
        var submitButton = rootComponent.childById(ButtonWidget.class, "submit");
        var urlElement = rootComponent.childById(TextFieldWidget.class, "url-box");

        urlElement.setMaxLength(250);





        submitButton.onPress(button -> {
            var value = urlElement.getText();


            if (value.startsWith("https://") && (value.endsWith(".abc"))) {
                var buf = PacketByteBufs.create();
                buf.writeBlockPos(handler.blockPos);
                buf.writeString(value);

                ClientPlayNetworking.send(NyakoModNetworking.NOTE_BLOCK_PLUS_SAVE_PACKET, buf);

                MinecraftClient.getInstance().setScreen(null);
            }
        });

    }
}
