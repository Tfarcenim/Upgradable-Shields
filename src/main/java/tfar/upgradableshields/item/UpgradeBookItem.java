package tfar.upgradableshields.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tfar.upgradableshields.UpgradeBookContainer;
import tfar.upgradableshields.world.PersistentData;
import tfar.upgradableshields.world.UpgradeData;

public class UpgradeBookItem extends Item {
    public UpgradeBookItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) {
            UpgradeData data = PersistentData.getDefaultInstance((ServerWorld)worldIn).getOrCreate(playerIn.getGameProfile().getId());
            playerIn.openContainer(new SimpleNamedContainerProvider((int id, PlayerInventory inv, PlayerEntity player) ->
                    new UpgradeBookContainer(id,inv,data),new StringTextComponent("Upgrade Book")));
        }
        return ActionResult.resultConsume(playerIn.getHeldItem(handIn));
    }
}
