package tfar.upgradableshields.mixin;

import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.upgradableshields.client.ClientMod;

@Mixin(ItemCameraTransforms.class)
public class ItemCameraTransformsMixin {


  //  @Inject(method =  "getTransform",at = @At("HEAD"),cancellable = true)
    private void getTransform(ItemCameraTransforms.TransformType type, CallbackInfoReturnable<ItemTransformVec3f> cir) {
        if (type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
        cir.setReturnValue(ClientMod.newVec(type));
    }
}
