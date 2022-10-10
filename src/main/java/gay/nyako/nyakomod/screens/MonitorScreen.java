package gay.nyako.nyakomod.screens;

import gay.nyako.nyakomod.NyakoNetworking;
import gay.nyako.nyakomod.entity.MonitorEntity;
import io.wispforest.owo.ui.base.BaseUIModelScreen;
import io.wispforest.owo.ui.container.FlowLayout;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.Identifier;

public class MonitorScreen extends BaseUIModelScreen<FlowLayout> {
    MonitorEntity monitorEntity;

    public MonitorScreen(MonitorEntity monitorEntity) {
        super(FlowLayout.class, DataSource.asset(new Identifier("nyakomod", "monitor_input")));

        this.monitorEntity = monitorEntity;
    }


    @Override
    protected void build(FlowLayout rootComponent) {
        var submitButton = rootComponent.childById(ButtonWidget.class, "submit");
        var textElement = rootComponent.childById(TextFieldWidget.class, "url-box");
        assert textElement != null;
        textElement.setMaxLength(250);
        textElement.setText(monitorEntity.getURL());

        assert submitButton != null;
        submitButton.onPress(button -> {
            var value = textElement.getText();
            if (value.startsWith("https://") && value.endsWith(".png")) {
                monitorEntity.setURL(textElement.getText());
                var buf = PacketByteBufs.create();
                buf.writeString(textElement.getText());
                buf.writeUuid(monitorEntity.getUuid());

                ClientPlayNetworking.send(NyakoNetworking.MONITOR_SET_URL, buf);

                MinecraftClient.getInstance().setScreen(null);
            }
        });
    }
}
