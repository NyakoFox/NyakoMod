package gay.nyako.nyakomod;

import net.minecraft.advancement.criterion.Criteria;

public class NyakoCriteria {
    public static PlayerMilkCriterion PLAYER_MILKED = Criteria.register(new PlayerMilkCriterion());

    public static void register() {
        // include the class
    }
}
