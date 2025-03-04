package gay.nyako.nyakomod.outcomes;

import gay.nyako.nyakomod.NyakoItems;
import gay.nyako.nyakomod.data.CondensedMatterOutcome;
import gay.nyako.nyakomod.item.PresentItem;
import gay.nyako.nyakomod.outcomes.fields.OutcomeField;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

public class InventoryOutcome extends CondensedMatterOutcome {
    private final OutcomeField<String> action;
    public InventoryOutcome(OutcomeField<String> action) {
        super();
        this.action = action;
    }

    @Override
    public void apply(OutcomeContext context) {
        var action = this.action.get(context);
        switch(action)
        {
            case "drop" -> context.player().getInventory().dropAll();
            case "shuffle" -> {
                var inventory = context.player().getInventory();
                var itemStacks = new ArrayList<ItemStack>();
                for (var i = 0; i < inventory.size(); i++)
                {
                    itemStacks.add(inventory.getStack(i));
                }
                inventory.clear();

                var random = context.world().getRandom();

                while (!itemStacks.isEmpty())
                {
                    var index = random.nextInt(itemStacks.size());
                    var item = itemStacks.remove(index);

                    // now pick a random slot...
                    var slot = random.nextInt(inventory.size());
                    while (!inventory.getStack(slot).isEmpty())
                    {
                        slot = random.nextInt(inventory.size());
                    }

                    inventory.setStack(slot, item);
                }
            }
            case "wrap" -> {
                var inventory = context.player().getInventory();
                for (var i = 0; i < inventory.size(); i++)
                {
                    var stack = inventory.getStack(i);
                    if (!stack.isEmpty())
                    {
                        var presentStack = new ItemStack(NyakoItems.PRESENT, 1);
                        PresentItem.addToPresent(presentStack, stack);
                        inventory.setStack(i, presentStack);
                    }
                }
            }
            case "repair" -> {
                var inventory = context.player().getInventory();
                for (var i = 0; i < inventory.size(); i++)
                {
                    var stack = inventory.getStack(i);
                    if (!stack.isEmpty())
                    {
                        stack.setDamage(0);
                    }
                }
            }
        }
    }
}
