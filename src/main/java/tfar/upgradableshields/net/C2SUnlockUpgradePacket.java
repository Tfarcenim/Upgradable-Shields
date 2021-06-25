package tfar.upgradableshields.net;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;
import tfar.upgradableshields.ModItems;
import tfar.upgradableshields.UpgradableShieldItem;
import tfar.upgradableshields.item.WalletItem;
import tfar.upgradableshields.util.Counter;
import tfar.upgradableshields.util.UpgradeType;
import tfar.upgradableshields.world.PersistentData;
import tfar.upgradableshields.world.UpgradeData;

import java.util.Locale;
import java.util.function.Supplier;

public class C2SUnlockUpgradePacket {

    private UpgradeType type;

    public C2SUnlockUpgradePacket(UpgradeType type) {
        this.type = type;
    }

    //decode
    public C2SUnlockUpgradePacket(PacketBuffer buf) {
        this.type = UpgradeType.values()[buf.readInt()];
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(type.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null) return;
        ctx.get().enqueueWork(()-> {
            PersistentData data = PersistentData.getDefaultInstance(player.getServerWorld());
            UpgradeData upgradeData = data.getOrCreate(player.getGameProfile().getId());
            if (upgradeData.canUnlock(type,player)) {
                upgradeData.unlock(type);
                takeCoins(player,type);
                for (ItemStack stack : player.inventory.mainInventory) {
                    if (stack.getItem() instanceof UpgradableShieldItem) {
                        stack.setDisplayName(new StringTextComponent(type.name().toLowerCase(Locale.ROOT).substring(0, 1) + type.name().toLowerCase(Locale.ROOT).substring(1)));
                    }
                }
                data.markDirty();
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static void takeCoins(PlayerEntity player,UpgradeType type) {
        final int totalCost = type.getCost();
        int remaining = totalCost;
        int taken = Counter.clearOrCountMatchingItems(player.inventory,stack -> stack.getItem() == ModItems.COIN,totalCost,false);
        remaining -= taken;
        if (remaining > 0) {
            for (ItemStack stack : player.inventory.mainInventory) {
                if (stack.getItem() instanceof WalletItem) {
                    if (stack.hasTag()) {
                        int stored = stack.getTag().getInt("coins");
                        if (remaining > stored) {
                            stack.getTag().remove("coins");
                            remaining -= stored;
                        } else {
                            stack.getTag().putInt("coins", stored - remaining);
                            break;
                        }
                    }
                }
            }
        }
    }
}
