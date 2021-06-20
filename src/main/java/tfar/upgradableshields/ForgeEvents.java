package tfar.upgradableshields;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Util;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerXpEvent;
import tfar.upgradableshields.util.UpgradeType;
import tfar.upgradableshields.world.PersistentData;
import tfar.upgradableshields.world.UpgradeData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ForgeEvents {

    public static void registerEvents() {
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::reflectArrow);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::reflectFireball);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::explodeOnLand);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::onDeath);
        MinecraftForge.EVENT_BUS.addListener(ForgeEvents::pickupXP);
    }

    private static void reflectArrow(ProjectileImpactEvent.Arrow e) {
        reflect1(e,e.getArrow());
    }

    private static void reflectFireball(ProjectileImpactEvent.Fireball e) {
        reflect1(e,e.getFireball());
    }

    private static void explodeOnLand(LivingFallEvent e) {
        LivingEntity entity = e.getEntityLiving();
        if (entity instanceof VillagerEntity && entity.getPersistentData().contains("explodes")) {
            World world = entity.world;
            world.createExplosion(entity,entity.getPosX(),entity.getPosY() - .5,entity.getPosZ(),5,false, Explosion.Mode.NONE);
            entity.getPersistentData().remove("explodes");
        }
    }

    private static void onDeath(LivingDropsEvent e) {
        LivingEntity victim = e.getEntityLiving();
        DamageSource source = e.getSource();
        Entity attacker = source.getTrueSource();
        if (!(victim instanceof PlayerEntity) && attacker instanceof PlayerEntity) {
            int maxHealth = (int) victim.getMaxHealth();

            double multiplier = 1;

            int level = ((PlayerEntity)attacker).experienceLevel;

            for (Map.Entry<Integer,Double> entry: levelMap.entrySet()) {
                int key = entry.getKey();
                if (level < key) {
                    break;
                }
                multiplier = entry.getValue();
            }

            int coins = (int) (multiplier * maxHealth);

            ItemStack stack = new ItemStack(ModItems.COIN,coins);
            ItemEntity itemEntity = new ItemEntity(victim.world,victim.getPosX(),victim.getPosY(),victim.getPosZ(),stack);
            e.getDrops().add(itemEntity);
        }
    }

    private static void reflect1(ProjectileImpactEvent event,Entity reflect) {

        RayTraceResult result = event.getRayTraceResult();
        if (result instanceof EntityRayTraceResult) {
            EntityRayTraceResult entityRayTraceResult = (EntityRayTraceResult)result;
            Entity target = entityRayTraceResult.getEntity();
            if (target instanceof PlayerEntity && !target.world.isRemote) {
                PlayerEntity player = (PlayerEntity)target;
                PersistentData data = PersistentData.getDefaultInstance((ServerWorld) reflect.world);
                UpgradeData upgradeData = data.getOrCreate(player.getGameProfile().getId());

                if (!getHeldShield(player).isEmpty() && !upgradeData.locked(UpgradeType.REFLECTION)) {
                    reflect(reflect);
                    event.setCanceled(true);
                }
            }
        }
    }
    
    public static final Map<Integer,Double> levelMap = new HashMap<>();
    
    static {
        levelMap.put(7,1.5);
        levelMap.put(15,2.5);
        levelMap.put(21,3.333);
        levelMap.put(26,5d);
        levelMap.put(32,10d);
    }

    private static final Map<UUID,Integer> lastLevels = new HashMap<>();

    private static void pickupXP(PlayerXpEvent.LevelChange e) {
        PlayerEntity player = e.getPlayer();
        int currentLevels = player.experienceLevel;
        int lastLevel = lastLevels.computeIfAbsent(player.getGameProfile().getId(),i-> 0);


        for (Map.Entry<Integer,Double> entry: levelMap.entrySet()) {
            int key = entry.getKey();
            if (currentLevels >= key && lastLevel < key) {
                player.sendMessage(new StringTextComponent("Coin multiplier upgraded to x"+entry.getValue()), Util.DUMMY_UUID);
                break;
            }
        }
        lastLevels.put(player.getGameProfile().getId(),currentLevels);
    }

    public static ItemStack getHeldShield(PlayerEntity player) {
        return player.getHeldItemMainhand().getItem() instanceof UpgradableShieldItem ? player.getHeldItemMainhand() :
                player.getHeldItemOffhand().getItem() instanceof UpgradableShieldItem ? player.getHeldItemOffhand() : ItemStack.EMPTY;
    }

    private static void reflect(Entity entity) {
        entity.setMotion(entity.getMotion().scale(-2));
        entity.markPositionDirty();
    }
}
