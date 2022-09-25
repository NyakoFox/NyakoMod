package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoModNetworking;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.CheckboxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;

import java.util.*;

public class ModelScreen extends BaseUIModelScreen<FlowLayout> {
    public ModelScreen() {
        super(FlowLayout.class, DataSource.asset(new Identifier("nyakomod", "model_input")));
    }

    Map<String, CheckboxComponent> checkboxes;

    String chosenType = "item";

    @Override
    protected void build(FlowLayout rootComponent) {
        var submitButton = rootComponent.childById(ButtonWidget.class, "submit");
        var urlElement = rootComponent.childById(TextFieldWidget.class, "url-box");
        var nameElement = rootComponent.childById(TextFieldWidget.class, "name-box");
        var itemBoxElement = rootComponent.childById(CheckboxComponent.class, "item-checkbox");
        var handheldBoxElement = rootComponent.childById(CheckboxComponent.class, "handheld-checkbox");
        var blockBoxElement = rootComponent.childById(CheckboxComponent.class, "block-checkbox");

        checkboxes = new HashMap<>();
        checkboxes.put("item", itemBoxElement);
        checkboxes.put("handheld", handheldBoxElement);
        checkboxes.put("block", blockBoxElement);

        urlElement.setMaxLength(250);
        nameElement.setMaxLength(25);

        submitButton.onPress(button -> {
            var value = urlElement.getText();
            var name = nameElement.getText();
            if (value.startsWith("https://") && (value.endsWith(".png") || value.endsWith(".jpg") || value.endsWith(".jpeg"))) {
                name = name.toLowerCase().replaceAll("[^a-z_0-9]", "");

                var buf = PacketByteBufs.create();
                buf.writeString(name);
                buf.writeString(chosenType);
                buf.writeString(value);
                ClientPlayNetworking.send(NyakoModNetworking.MODEL_CREATE_PACKET, buf);

                MinecraftClient.getInstance().setScreen(null);
            }
        });

        for (var checkboxEntry : checkboxes.entrySet()) {
            var key = checkboxEntry.getKey();
            checkboxEntry.getValue().onChanged(state -> {
                if (state) {
                    onCheckboxChange(key);
                }
            });
        }
    }

    protected void onCheckboxChange(String type) {
        for (var checkboxEntry : checkboxes.entrySet()) {
            var key = checkboxEntry.getKey();
            var match = key.equals(type);
            var box = checkboxEntry.getValue();

            if (!match && box.isChecked()) {
                box.onPress();
            }
        }

        chosenType = type;
    }
}
