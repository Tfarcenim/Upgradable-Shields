package tfar.upgradableshields.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.upgradableshields.UpgradableShieldItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract ItemStack getActiveItemStack();

    @Inject(method = "canBlockDamageSource",at = @At(value = "INVOKE",target = "Lnet/minecraft/util/DamageSource;getDamageLocation()Lnet/minecraft/util/math/vector/Vector3d;"),cancellable = true)
    private void allBlock(DamageSource damageSourceIn, CallbackInfoReturnable<Boolean> cir) {
        if (getActiveItemStack().getItem() instanceof UpgradableShieldItem) {
            cir.setReturnValue(true);
        }
    }
}
