package tfar.upgradableshields;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.tuple.Pair;
import tfar.upgradableshields.client.ClientMod;
import tfar.upgradableshields.client.ModKeybinds;
import tfar.upgradableshields.client.UltimateShieldLayer;
import tfar.upgradableshields.client.UpgradeScreen;
import tfar.upgradableshields.datagen.ModDatagen;
import tfar.upgradableshields.net.PacketHandler;
import tfar.upgradableshields.world.PersistentData;

import java.util.Map;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(UpgradableShields.MODID)
public class UpgradableShields {
    // Directly reference a log4j logger.

    public static final String MODID = "upgradableshields";

    public static final ContainerType<UpgradeBookContainer> UPGRADE_BOOK = new ContainerType<>(UpgradeBookContainer::new);

    public UpgradableShields() {

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_SPEC);

        // Register the setup method for modloading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(ModDatagen::start);
        bus.addGenericListener(Item.class,this::items);
        bus.addGenericListener(ContainerType.class,this::menus);
        MinecraftForge.EVENT_BUS.addListener(this::firstJoin);
        // Register the doClientStuff method for modloading
        if (FMLEnvironment.dist.isClient()) {
            ClientMod.registerEvents(bus);
            bus.addListener(this::doClientStuff);
        }
    }

    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        final Pair<CoinConfigs, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(CoinConfigs::build);
        SERVER_SPEC = specPair2.getRight();
    }

    private void firstJoin(PlayerEvent.PlayerLoggedInEvent e) {
        PlayerEntity player = e.getPlayer();
        World world = player.world;
        PersistentData persistentData = PersistentData.getDefaultInstance((ServerWorld)world);
        UUID uuid = player.getGameProfile().getId();
        if (!persistentData.alreadyJoined(uuid)) {
            player.addItemStackToInventory(new ItemStack(ModItems.UPGRADE_BOOK));
            persistentData.markJoined(uuid);
        }
    }

    private void setup(final FMLCommonSetupEvent event) {
        ForgeEvents.registerEvents();
        PacketHandler.registerPackets();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ScreenManager.registerFactory(UPGRADE_BOOK, UpgradeScreen::new);
        ModKeybinds.register();
        ItemModelsProperties.registerProperty(ModItems.UPGRADABLE_SHIELD,new ResourceLocation("blocking"),
                (stack, world, livingEntity) -> livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == stack ? 1.0F : 0.0F);

        Map<String, PlayerRenderer> skinMap = Minecraft.getInstance().getRenderManager().getSkinMap();

        for (Map.Entry<String,PlayerRenderer> entry : skinMap.entrySet()) {
            entry.getValue().addLayer(new UltimateShieldLayer<>(entry.getValue()));
        }
    }

    private void items(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(ModItems.UPGRADABLE_SHIELD.setRegistryName("upgradable_shield"));
        e.getRegistry().register(ModItems.COIN.setRegistryName("coin"));

        e.getRegistry().register(ModItems.SHIELD_RIGHT_LEFT.setRegistryName("shield_right_left"));
        e.getRegistry().register(ModItems.SHIELD_BACK.setRegistryName("shield_back"));

        e.getRegistry().register(ModItems.UPGRADE_BOOK.setRegistryName("upgrade_book"));

        e.getRegistry().register(ModItems.WALLET.setRegistryName("wallet"));
    }

    private void menus(RegistryEvent.Register<ContainerType<?>> e) {
        e.getRegistry().register(UPGRADE_BOOK.setRegistryName("upgrade_book"));
    }
}
