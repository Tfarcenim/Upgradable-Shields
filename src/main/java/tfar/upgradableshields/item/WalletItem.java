package tfar.upgradableshields.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import tfar.upgradableshields.ModItems;
import tfar.upgradableshields.util.Counter;

import javax.annotation.Nullable;
import java.util.List;

public class WalletItem extends Item {
    public WalletItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        int toDeposit = Counter.clearOrCountMatchingItems(playerIn.inventory,stack -> stack.getItem() == ModItems.COIN,0,true);

        int prior = itemstack.getOrCreateTag().getInt("coins");

        Counter.clearOrCountMatchingItems(playerIn.inventory,stack -> stack.getItem() == ModItems.COIN,toDeposit,false);

        itemstack.getTag().putInt("coins",prior + toDeposit);

        return ActionResult.resultConsume(itemstack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTag()) {
            int coins = stack.getTag().getInt("coins");
            tooltip.add(new StringTextComponent("Coins: "+coins));
        }
    }
}
