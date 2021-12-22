package gay.nyako.nyakomod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.scoreboard.ScoreboardCriterion;

@Mixin(ScoreboardCriterion.class)
public interface ScoreboardCriterionMixin {
  @Invoker("create")
  static ScoreboardCriterion create(String name) {
    throw new AssertionError();
  }
}
