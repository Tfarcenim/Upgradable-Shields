package tfar.upgradableshields.world;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class PersistentData extends WorldSavedData {

    private final HashSet<UUID> hasBook = new HashSet<>();

    private final Map<UUID,UpgradeData> upgradeDataMap = new HashMap<>();

    public static PersistentData getDefaultInstance(ServerWorld level) {
        return getInstance(level.getServer().getWorld(World.OVERWORLD));
    }

    public UpgradeData getOrCreate(UUID uuid) {
        if (upgradeDataMap.containsKey(uuid)) {
            return upgradeDataMap.get(uuid);
        }
        UpgradeData upgradeData = UpgradeData.makeDefault();
        upgradeDataMap.put(uuid,upgradeData);
        markDirty();
        return upgradeDataMap.get(uuid);
    }

    private static PersistentData getInstance(ServerWorld level) {
        return level.getSavedData()
                .getOrCreate(() -> new PersistentData(level.getDimensionKey().getLocation().toString())
                        ,level.getDimensionKey().getLocation().toString());
    }

    public PersistentData(String name) {
        super(name);
    }

    public boolean alreadyJoined(UUID uuid) {
        return hasBook.contains(uuid);
    }

    public void markJoined(UUID uuid) {
        hasBook.add(uuid);
        markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        hasBook.clear();
        upgradeDataMap.clear();
        ListNBT listNBT = nbt.getList("hasBook", Constants.NBT.TAG_COMPOUND);
        for (INBT inbt : listNBT) {
            CompoundNBT nbt1 = (CompoundNBT)inbt;
            UUID uuid = nbt1.getUniqueId("uuid");
            hasBook.add(uuid);
        }

        ListNBT listNBT1 = nbt.getList("upgrade_datas",Constants.NBT.TAG_COMPOUND);
        for (INBT inbt : listNBT1) {
            CompoundNBT nbt1 = (CompoundNBT)inbt;
            ListNBT listNBT2 = nbt1.getList("upgrade_data",Constants.NBT.TAG_COMPOUND);
            UpgradeData upgradeData = UpgradeData.read(listNBT2);
            UUID uuid = nbt1.getUniqueId("uuid");
            upgradeDataMap.put(uuid,upgradeData);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        for (UUID uuid : hasBook) {
            CompoundNBT nbt1 = new CompoundNBT();
            nbt1.putUniqueId("uuid",uuid);
            listNBT.add(nbt1);
        }

        ListNBT listNBT1 = new ListNBT();
        for (Map.Entry<UUID,UpgradeData> entry: upgradeDataMap.entrySet()) {
            CompoundNBT nbt1 = new CompoundNBT();
            nbt1.putUniqueId("uuid",entry.getKey());
            nbt1.put("upgrade_data",entry.getValue().save());
            listNBT1.add(nbt1);
        }

        if (!listNBT.isEmpty())
        compound.put("hasBook",listNBT);
        if (!listNBT1.isEmpty())
        compound.put("upgrade_datas",listNBT1);
        return compound;
    }
}
