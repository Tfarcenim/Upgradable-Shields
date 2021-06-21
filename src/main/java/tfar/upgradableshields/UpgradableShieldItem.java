package tfar.upgradableshields;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import tfar.upgradableshields.util.UpgradeType;
import tfar.upgradableshields.world.PersistentData;
import tfar.upgradableshields.world.UpgradeData;

import javax.annotation.Nullable;
import java.util.List;

public class UpgradableShieldItem extends ShieldItem {
    public UpgradableShieldItem(Properties builder) {
        super(builder);
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {

        if (!worldIn.isRemote && entityIn instanceof PlayerEntity) {
            PersistentData persistentData = PersistentData.getDefaultInstance((ServerWorld) worldIn);
            UpgradeData upgradeData = persistentData.getOrCreate(((PlayerEntity)entityIn).getGameProfile().getId());
            UpgradeType highest = upgradeData.getHighestUnlocked();
            if (highest != null) {
                stack.getOrCreateTag().putString("highest",highest.name());
            }
        }

        if (stack.hasTag() && stack.getTag().getInt("dashing") > 0 && isSelected && !worldIn.isRemote) {
            stack.getTag().putInt("dashing",stack.getTag().getInt("dashing") - 1);
            List<Entity> entityList = worldIn.getEntitiesInAABBexcluding(entityIn,entityIn.getBoundingBox().grow(1,0,1), LivingEntity.class::isInstance);
            for (Entity entity : entityList) {
                LivingEntity entity1 = (LivingEntity)entity;
                entity1.applyKnockback(5,entity1.getPosX() - entityIn.getPosX(), entity1.getPosZ() - entityIn.getPosZ());
                entity1.setMotion(entityIn.getMotion().add(0,2,0));
            }

            int x1 = entityIn.getPosition().getX() - 1;
            int y1 = entityIn.getPosition().getY() + 0;
            int z1 = entityIn.getPosition().getZ() - 1;

            int x2 = entityIn.getPosition().getX() + 2;
            int y2 = entityIn.getPosition().getY() + 2;
            int z2 = entityIn.getPosition().getZ() + 2;

            if (entityIn instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity)entityIn;
                for (int x = x1; x < x2; x++) {
                    for (int y = y1; y < y2; y++) {
                        for (int z = z1; z < z2; z++) {
                            BlockPos pos = new BlockPos(x, y, z);
                            if (player.canPlayerEdit(pos,player.getHorizontalFacing(),player.getHeldItemMainhand())) {
                                worldIn.destroyBlock(pos,true,player);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !ItemStack.areItemsEqual(oldStack,newStack);
    }
}
