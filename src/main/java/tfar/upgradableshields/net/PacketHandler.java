package tfar.upgradableshields.net;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import tfar.upgradableshields.UpgradableShields;

public class PacketHandler {
    public static SimpleChannel INSTANCE;

    public static void registerPackets() {
            int id = 0;
            INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(UpgradableShields.MODID, UpgradableShields.MODID), () -> "1.0", s -> true, s -> true);

            INSTANCE.registerMessage(id++, C2SKeybindPacket.class,
                    C2SKeybindPacket::encode,
                    C2SKeybindPacket::new,
                    C2SKeybindPacket::handle);

        INSTANCE.registerMessage(id++, C2SUnlockUpgradePacket.class,
                C2SUnlockUpgradePacket::encode,
                C2SUnlockUpgradePacket::new,
                C2SUnlockUpgradePacket::handle);
        }
}
