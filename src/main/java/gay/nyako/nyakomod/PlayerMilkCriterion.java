package gay.nyako.nyakomod;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.block.Block;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class PlayerMilkCriterion extends AbstractCriterion<PlayerMilkCriterion.Conditions> {
    public void trigger(ServerPlayerEntity player) {
        trigger(player, conditions -> true);
    }

    @Override
    public Codec<Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<ItemPredicate> item) implements AbstractCriterion.Conditions {
        public static final Codec<Conditions> CODEC = RecordCodecBuilder.
                create(
                        instance -> instance.group(
                                Codecs.createStrictOptionalFieldCodec(
                                        LootContextPredicate.CODEC, "player"
                                ).forGetter(
                                        Conditions::player
                                ),
                                Codecs.createStrictOptionalFieldCodec(
                                        ItemPredicate.CODEC, "item"
                                ).forGetter(
                                        Conditions::item
                                )
                        ).apply(
                                instance,
                                Conditions::new
                        )
                );

        public static AdvancementCriterion<Conditions> any() {
            return NyakoCriteria.PLAYER_MILKED.create(new Conditions(Optional.empty(), Optional.empty()));
        }
    }
}
