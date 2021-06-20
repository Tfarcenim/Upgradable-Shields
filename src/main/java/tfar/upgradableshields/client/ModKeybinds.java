package tfar.upgradableshields.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;
import tfar.upgradableshields.UpgradableShields;
import tfar.upgradableshields.net.C2SKeybindPacket;
import tfar.upgradableshields.net.PacketHandler;
import tfar.upgradableshields.util.UpgradeType;

public class ModKeybinds {

    public static final KeyBinding LIGHTNING = new KeyBinding("lightning", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);
    public static final KeyBinding DASHING = new KeyBinding("charge_dash", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);
    public static final KeyBinding JUMP = new KeyBinding("jump", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);
    public static final KeyBinding SUMMON = new KeyBinding("summon", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);
    public static final KeyBinding ARROW = new KeyBinding("arrow", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);
    public static final KeyBinding ENDER = new KeyBinding("ender", GLFW.GLFW_KEY_UNKNOWN, UpgradableShields.MODID);


    public static void register() {
        ClientRegistry.registerKeyBinding(LIGHTNING);
        ClientRegistry.registerKeyBinding(DASHING);
        ClientRegistry.registerKeyBinding(JUMP);
        ClientRegistry.registerKeyBinding(SUMMON);
        ClientRegistry.registerKeyBinding(ARROW);
        ClientRegistry.registerKeyBinding(ENDER);
    }

    public static void keyPress(InputEvent.KeyInputEvent e) {
        while (LIGHTNING.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.LIGHTNING));
        }

        while (DASHING.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.CHARGE_DASH));
        }

        while (JUMP.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.JUMP));
        }

        while (SUMMON.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.SUMMON));
        }

        while (ARROW.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.ARROW));
        }

        while (ENDER.isPressed()) {
            PacketHandler.INSTANCE.sendToServer(new C2SKeybindPacket(UpgradeType.ENDER));
        }
    }
}
