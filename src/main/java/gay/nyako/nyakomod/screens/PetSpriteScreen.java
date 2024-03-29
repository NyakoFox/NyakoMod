package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoNetworking;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class PetSpriteScreen extends BaseUIModelScreen<FlowLayout> {
    ItemStack item;

    public PetSpriteScreen(ItemStack item) {
        super(FlowLayout.class, DataSource.asset(new Identifier("nyakomod", "pet_input")));

        this.item = item;
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        var submitButton = rootComponent.childById(ButtonComponent.class, "submit");
        var textElement = rootComponent.childById(TextFieldWidget.class, "url-box");
        var petSizeElement = rootComponent.childById(TextFieldWidget.class, "pet-size-box");
        var checkboxWidget = rootComponent.childById(CheckboxWidget.class, "still-box");
        textElement.setMaxLength(250);

        var nbt = item.getOrCreateNbt();
        if (nbt.contains("custom_sprite")) {
            textElement.setText(nbt.getString("custom_sprite"));
            textElement.setCursorToStart(false);
        }
        if (nbt.contains("pet_size")) {
            petSizeElement.setText(String.valueOf(nbt.getDouble("pet_size")));
        } else {
            petSizeElement.setText("2.0");
        }
        petSizeElement.setCursorToStart(false);

        if (nbt.contains("still")) {
            checkboxWidget.checked = nbt.getBoolean("still");
        }

        submitButton.onPress(button -> {
            var value = textElement.getText();
            if (value.startsWith("https://") && value.endsWith(".png")) {
                var buf = PacketByteBufs.create();
                buf.writeString(textElement.getText());

                try {
                    var size = Double.parseDouble(petSizeElement.getText());
                    buf.writeDouble(size);
                } catch (Exception e) {
                    buf.writeDouble(2);
                }

                buf.writeBoolean(checkboxWidget.isChecked());

                ClientPlayNetworking.send(NyakoNetworking.PET_SPRITE_SET_URL, buf);

                MinecraftClient.getInstance().setScreen(null);
            }
        });
    }
}
