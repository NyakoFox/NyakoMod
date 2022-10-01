package gay.nyako.nyakomod;

import io.wispforest.owo.config.ConfigWrapper;
import io.wispforest.owo.config.Option;
import io.wispforest.owo.util.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NyakoConfig extends ConfigWrapper<gay.nyako.nyakomod.NyakoConfigModel> {

    private final Option<java.lang.String> packURL = this.optionForKey(new Option.Key("packURL"));
    private final Option<java.lang.Integer> webserverPort = this.optionForKey(new Option.Key("webserverPort"));

    private NyakoConfig() {
        super(gay.nyako.nyakomod.NyakoConfigModel.class);
    }

    public static NyakoConfig createAndLoad() {
        var wrapper = new NyakoConfig();
        wrapper.load();
        return wrapper;
    }

    public java.lang.String packURL() {
        return packURL.value();
    }

    public void packURL(java.lang.String value) {
        instance.packURL = value;
        packURL.synchronizeWithBackingField();
    }

    public int webserverPort() {
        return webserverPort.value();
    }

    public void webserverPort(int value) {
        instance.webserverPort = value;
        webserverPort.synchronizeWithBackingField();
    }




}

