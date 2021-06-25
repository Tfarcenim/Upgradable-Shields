package tfar.upgradableshields.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.play.server.SExplosionPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import tfar.upgradableshields.CoinConfigs;
import tfar.upgradableshields.ForgeEvents;
import tfar.upgradableshields.ModItems;
import tfar.upgradableshields.item.WalletItem;
import tfar.upgradableshields.world.PersistentData;
import tfar.upgradableshields.world.UpgradeData;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public enum UpgradeType {
    ARROW(() -> CoinConfigs.arrow, UpgradeType::arrow),
    REFLECTION(() -> CoinConfigs.reflection, player -> {
    }),
    SUMMON(() -> CoinConfigs.summon, UpgradeType::summon),
    LIGHTNING(() -> CoinConfigs.lightning, UpgradeType::summonLightning),
    CHARGE_DASH(() -> CoinConfigs.charge_dash, UpgradeType::chargeDash),
    JUMP(() -> CoinConfigs.jump, UpgradeType::explosiveJump),

    ENDER(() -> CoinConfigs.ender, UpgradeType::ender),
    ULTIMATE(() -> CoinConfigs.ultimate, player -> {
    });

    private final Supplier<ForgeConfigSpec.IntValue> coins;
    private final Consumer<ServerPlayerEntity> consumer;

    UpgradeType(Supplier<ForgeConfigSpec.IntValue> coins, Consumer<ServerPlayerEntity> consumer) {
        this.coins = coins;
        this.consumer = consumer;
    }

    public int getCost() {
        return coins.get().get();
    }


    public void perform(ServerPlayerEntity player) {
        ServerWorld level = (ServerWorld) player.world;
        PersistentData data = PersistentData.getDefaultInstance(level);
        UpgradeData upgradeData = data.getOrCreate(player.getUniqueID());

        if (!ForgeEvents.getHeldShield(player).isEmpty()) {
            if (!upgradeData.locked(this)) {
                consumer.accept(player);
            } else {
                player.sendStatusMessage(new StringTextComponent(this+" not unlocked yet").setStyle(Style.EMPTY.applyFormatting(TextFormatting.RED)),true);
            }
        }
    }

    public boolean hasCoins(PlayerEntity player) {
        int rawCoins = Counter.clearOrCountMatchingItems(player.inventory, stack -> stack.getItem() == ModItems.COIN, 0, true);
        int walletCoins = 0;

        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack.getItem() instanceof WalletItem) {
                if (stack.hasTag()) {
                    walletCoins += stack.getTag().getInt("coins");
                }
            }
        }

        return walletCoins + rawCoins >= getCost();
    }

    private static void summonLightning(ServerPlayerEntity player) {
        World world = player.world;
        BlockRayTraceResult result = (BlockRayTraceResult) player.pick(25, 0, false);
        BlockPos pos = result.getPos();
        LightningBoltEntity lightningboltentity = EntityType.LIGHTNING_BOLT.create(player.world);
        lightningboltentity.moveForced(pos.getX(), player.world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos.getX(), pos.getZ()), pos.getZ());
        world.addEntity(lightningboltentity);

    }

    private static void chargeDash(ServerPlayerEntity player) {
        ItemStack shield = ForgeEvents.getHeldShield(player);
        int time = 30;
        if (!shield.isEmpty()) {
            shield.getOrCreateTag().putInt("dashing", time);
            player.addPotionEffect(new EffectInstance(Effects.SPEED, time, 4, false, false));
        }
    }

    private static void explosiveJump(ServerPlayerEntity player) {
        World world = player.world;
        world.createExplosion(player, player.getPosX(), player.getPosY() - .5, player.getPosZ(), 5, false, Explosion.Mode.BREAK);

        player.connection.sendPacket(new SExplosionPacket(player.getPosX(), player.getPosY(), player.getPosZ(), 5, new ArrayList<>(), new Vector3d(0,1,0)));
        player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 40, 4, false, false));

    }

    private static void summon(ServerPlayerEntity player) {
        for (int i = 0; i < 16; i++) {
            VillagerEntity villager = EntityType.VILLAGER.create(player.world);

            Random rand = player.getRNG();

            double x = player.getPosX();// + rand.nextInt(2) - 1;
            double y = player.getPosY() + 2;//rand.nextInt(16) - 7;
            double z = player.getPosZ();// + rand.nextInt(2) - 1;

            villager.setLocationAndAngles(x, y, z, villager.rotationYaw, villager.rotationPitch);
            villager.getPersistentData().putBoolean("explodes", true);
            villager.setMotion(new Vector3d(4 * (Math.random() - .5), .4, 4 * (Math.random() - .5)));
            player.world.addEntity(villager);
        }
    }

    private static void ender(ServerPlayerEntity player) {

        BlockRayTraceResult result = (BlockRayTraceResult) player.pick(25, 0, false);
        BlockPos pos = result.getPos();

        EnderTeleportEvent event = new EnderTeleportEvent(player, pos.getX(), pos.getY() + 1, pos.getZ(), 5.0F);
        if (!MinecraftForge.EVENT_BUS.post(event)) {
            player.setPositionAndUpdate(event.getTargetX(), event.getTargetY(), event.getTargetZ());
            player.fallDistance = 0.0F;
            //player.attackEntityFrom(DamageSource.FALL, event.getAttackDamage());
        }
    }

    private static void arrow(ServerPlayerEntity player) {
        World level = player.world;
        for (int i = 0; i < 3; i++) {
            ItemStack arrowStack = new ItemStack(Items.ARROW);
            ArrowItem arrowitem = (ArrowItem) (arrowStack.getItem() instanceof ArrowItem ? arrowStack.getItem() : Items.ARROW);
            AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(level, arrowStack, player);
            //abstractarrowentity = customArrow(abstractarrowentity);
            float f = 1;
            abstractarrowentity.setDirectionAndMovement(player, player.rotationPitch, player.rotationYaw, 0.0F, f * 3.0F, 1.0F);
            abstractarrowentity.setIsCritical(true);
            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            level.addEntity(abstractarrowentity);
        }
    }
}
