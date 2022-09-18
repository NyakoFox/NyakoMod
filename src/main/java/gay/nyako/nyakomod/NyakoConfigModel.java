package gay.nyako.nyakomod;

import io.wispforest.owo.config.annotation.Config;
import io.wispforest.owo.config.annotation.Modmenu;

@Modmenu(modId = "nyakomod")
@Config(name = "nyako-config", wrapperName= "NyakoConfig")
public class NyakoConfigModel {
    public String packURL = "http://localhost:8080/pack.zip";
    public int webserverPort = 8080;
}
