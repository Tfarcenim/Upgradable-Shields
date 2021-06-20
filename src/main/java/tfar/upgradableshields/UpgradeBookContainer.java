package tfar.upgradableshields;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import tfar.upgradableshields.world.UpgradeData;

public class UpgradeBookContainer extends Container {

    public final UpgradeData upgradeData;

    //client
    public UpgradeBookContainer(int id, PlayerInventory inv) {
        this(id,inv,UpgradeData.makeDefault());
    }

    //server
    public UpgradeBookContainer(int id, PlayerInventory inv, UpgradeData upgradeData) {
        super(UpgradableShields.UPGRADE_BOOK,id);
        this.upgradeData = upgradeData;
        trackIntArray(upgradeData);
        addPlayerSlots(inv);
    }

    protected void addPlayerSlots(PlayerInventory playerinventory) {
        int yStart = 84;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = 8 + col * 18;
                int y = row * 18 + yStart;
                this.addSlot(new Slot(playerinventory, col + row * 9 + 9, x, y));
            }
        }

        for (int row = 0; row < 9; ++row) {
            int x = 8 + row * 18;
            int y = yStart + 58;
            this.addSlot(new Slot(playerinventory, row, x, y));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }
}
