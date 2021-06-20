package tfar.upgradableshields;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import tfar.upgradableshields.client.UpgradableShieldIster;
import tfar.upgradableshields.item.UpgradeBookItem;
import tfar.upgradableshields.item.WalletItem;

public class ModItems {

    public static final Item UPGRADABLE_SHIELD = new UpgradableShieldItem(new Item.Properties().group(ItemGroup.COMBAT).setISTER(()-> UpgradableShieldIster::new));
    public static final Item UPGRADE_BOOK = new UpgradeBookItem(new Item.Properties().group(ItemGroup.COMBAT));
    public static final Item COIN = new Item(new Item.Properties().group(ItemGroup.COMBAT));

    public static final Item WALLET = new WalletItem(new Item.Properties().group(ItemGroup.COMBAT));

    public static final Item SHIELD_RIGHT_LEFT = new UpgradableShieldItem(new Item.Properties().setISTER(()-> UpgradableShieldIster::new));
    public static final Item SHIELD_BACK = new UpgradableShieldItem(new Item.Properties().setISTER(()-> UpgradableShieldIster::new));


}
