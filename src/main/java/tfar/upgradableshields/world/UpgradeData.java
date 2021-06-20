package tfar.upgradableshields.world;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import tfar.upgradableshields.util.UpgradeType;

import java.util.EnumMap;
import java.util.Map;

public class UpgradeData implements IIntArray {

    final Map<UpgradeType,Integer> data = new EnumMap<>(UpgradeType.class);

    private static final int DISABLED = 0b1;
    private static final int ENABLED = 0b0;

    private static final int LOCKED = 0b00;
    private static final int UNLOCKED = 0b10;

    public UpgradeData() {
    }

    public void unlock(UpgradeType type) {
        int i = data.get(type);
        int i1 = UNLOCKED | i;
        data.put(type,i1);
    }

    public boolean canUnlock(UpgradeType type,PlayerEntity player) {
        if (!locked(type)) {
            player.sendStatusMessage(new StringTextComponent("already unlocked"),false);
            return false;
        }
        if (type.ordinal() > 0) {
            UpgradeType last = UpgradeType.values()[type.ordinal() - 1];
            if (locked(last)) {
                player.sendStatusMessage(new StringTextComponent("must unlock prior upgrade first"),false);
                return false;
            }
        }
        boolean b = type.hasCoins(player);
        if (!b) {
            player.sendStatusMessage(new StringTextComponent("insufficient funds"),false);
        }
        return b;
    }


    public boolean locked(UpgradeType type) {
        return (data.get(type) & UNLOCKED) == 0;
    }

    @Override
    public int get(int index) {
        UpgradeType type = UpgradeType.values()[index];
        return data.get(type);
    }

    @Override
    public void set(int index, int value) {
        UpgradeType type = UpgradeType.values()[index];
        data.put(type,value);
    }

    @Override
    public int size() {
        return data.size();
    }

    public ListNBT save() {
        ListNBT listNBT = new ListNBT();
        for (Map.Entry<UpgradeType,Integer> entry : data.entrySet()) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("upgrade_type",entry.getKey().name());
            nbt.putInt("data",entry.getValue());
            listNBT.add(nbt);
        }
        return listNBT;
    }

    public static UpgradeData makeDefault() {
        UpgradeData upgradeData = new UpgradeData();
        for (UpgradeType type : UpgradeType.values()) {
            upgradeData.data.put(type,0);
        }
        return upgradeData;
    }

    public static UpgradeData read(ListNBT listNBT) {
        UpgradeData upgradeData = new UpgradeData();
        for (INBT inbt : listNBT) {
            CompoundNBT nbt = (CompoundNBT)inbt;
            UpgradeType upgradeType = UpgradeType.valueOf(nbt.getString("upgrade_type"));
            int data = nbt.getInt("data");
            upgradeData.data.put(upgradeType,data);
        }
        return upgradeData;
    }
}
