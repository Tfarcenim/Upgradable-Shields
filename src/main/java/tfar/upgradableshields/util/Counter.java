package tfar.upgradableshields.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

public class Counter {

    public static int clearOrCountMatchingItems(IInventory inv, Predicate<ItemStack> stackPredicate, int toRemove, boolean keep) {
        int total = 0;
        for(int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack itemstack = inv.getStackInSlot(i);
            int count = clearOrCountMatchingItem(itemstack, stackPredicate, toRemove - total, keep);
            if (count > 0 && !keep && itemstack.isEmpty()) {
                inv.setInventorySlotContents(i, ItemStack.EMPTY);
            }
            total += count;
        }

        return total;
    }

    public static int clearOrCountMatchingItem(ItemStack stack, Predicate<ItemStack> predicate, int maxClear, boolean keep) {
        if (!stack.isEmpty() && predicate.test(stack)) {
            if (keep) {
                return stack.getCount();
            } else {
                int remove = maxClear < 0 ? stack.getCount() : Math.min(maxClear, stack.getCount());
                stack.shrink(remove);
                return remove;
            }
        } else {
            return 0;
        }
    }

}
