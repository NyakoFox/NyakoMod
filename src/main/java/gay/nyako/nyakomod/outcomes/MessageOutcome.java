package gay.nyako.nyakomod.outcomes;

import com.google.gson.JsonElement;
import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class MessageOutcome extends CondensedMatterOutcome {
    private final JsonElement message;

    public MessageOutcome(JsonElement message) {
        super();
        this.message = message;
    }

    @Override
    public void apply(OutcomeContext context) {
        context.player().sendMessage(Text.Serializer.fromJson(message));
    }
}
