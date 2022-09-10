package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoMod;
import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.loader.impl.util.log.Log;
import net.fabricmc.loader.impl.util.log.LogCategory;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class PetSpriteScreen extends BaseUIModelScreen<FlowLayout> {
    public PetSpriteScreen() {
        super(FlowLayout.class, DataSource.asset(new Identifier("nyakomod", "url_input")));
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        var submitButton = rootComponent.childById(ButtonWidget.class, "submit");
        var textElement = rootComponent.childById(TextFieldWidget.class, "url-box");
        textElement.setMaxLength(250);

        submitButton.onPress(button -> {
            var value = textElement.getText();
            if (value.startsWith("https://") && value.endsWith(".png")) {
                var buf = PacketByteBufs.create();
                buf.writeString(textElement.getText());
                ClientPlayNetworking.send(NyakoMod.PET_SPRITE_SET_URL, buf);
            }
        });
    }
}
