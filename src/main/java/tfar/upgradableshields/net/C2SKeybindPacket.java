package tfar.upgradableshields.net;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import tfar.upgradableshields.util.UpgradeType;

import java.util.function.Supplier;

public class C2SKeybindPacket {

    private UpgradeType type;

    public C2SKeybindPacket(UpgradeType type) {
        this.type = type;
    }

    //decode
    public C2SKeybindPacket(PacketBuffer buf) {
        this.type = UpgradeType.values()[buf.readInt()];
    }

    public void encode(PacketBuffer buffer) {
        buffer.writeInt(type.ordinal());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity player = ctx.get().getSender();
        if (player == null) return;
        ctx.get().enqueueWork(()-> type.perform(player));
        ctx.get().setPacketHandled(true);
    }
}
