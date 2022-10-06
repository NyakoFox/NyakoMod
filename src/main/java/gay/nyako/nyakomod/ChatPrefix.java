package gay.nyako.nyakomod;

import eu.pb4.placeholders.api.TextParserUtils;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public class ChatPrefix {
    public String formatted;
    public String prefix;

    public ChatPrefix(String formatted, String prefix) {
        this.formatted = formatted;
        this.prefix = prefix;
    }

    public String getMarkdown() {
        return prefix + " **>>** ";
    }

    public Text getFormatted() {
        return TextParserUtils.formatText(formatted + " <white><bold>>></bold></white> ");
    }

    public Text apply(Text text) {
        MutableText mutableText = (MutableText) getFormatted();
        return mutableText.append(text);
    }

    public Text apply(String string) {
        return apply(TextParserUtils.formatText(string));
    }

    public String applyString(String string) {
        return getMarkdown() + string;
    }

    public String applyString(Text text) {
        return applyString(text.getString());
    }
}
