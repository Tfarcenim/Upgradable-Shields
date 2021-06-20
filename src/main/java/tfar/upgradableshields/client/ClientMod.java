package tfar.upgradableshields.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tfar.upgradableshields.ForgeEvents;

import static tfar.upgradableshields.client.UpgradableShieldIster.*;

public class ClientMod {



    public static void registerEvents(IEventBus modBus) {
        modBus.addListener(ClientMod::stitch);
        MinecraftForge.EVENT_BUS.addListener(ModKeybinds::keyPress);
        MinecraftForge.EVENT_BUS.addListener(ClientMod::dashing);
    }

    public static void stitch(TextureStitchEvent.Pre e) {
        AtlasTexture atlasTexture = e.getMap();
        if (atlasTexture.getTextureLocation().equals(AtlasTexture.LOCATION_BLOCKS_TEXTURE)) {
            e.addSprite(LOCATION_UPGRADE_SHIELD_BASE.getTextureLocation());
            matMap.forEach((type, renderMaterial) -> e.addSprite(renderMaterial.getTextureLocation()));
        }
    }

    public static void dashing(InputUpdateEvent e) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack shield = ForgeEvents.getHeldShield(player);
            CompoundNBT nbt = shield.getTag();
            if (nbt != null) {
                boolean dashing = nbt.getInt("dashing") > 0;
                if (dashing) {
                    e.getMovementInput().moveForward = 1;
                }
            }
        }
    }

    public static ItemTransformVec3f newVec(ItemCameraTransforms.TransformType type) {
      //  "rotation": [ 45, 45, 0 ],
      //  "translation": [ 9,-5, 10],
      //  "scale": [ 1, 1, 1 ]
            return new ItemTransformVec3f(new Vector3f(45,45,0),new Vector3f(14/16f,-3/16f,10/16f),new Vector3f(1,1,1));
    }
}
