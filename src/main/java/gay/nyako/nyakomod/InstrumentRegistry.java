package gay.nyako.nyakomod;

public class InstrumentRegistry {
    public static final InstrumentRegister HARP = new InstrumentRegister("harp");


    public static void register() {
        HARP.register();
    }
}
