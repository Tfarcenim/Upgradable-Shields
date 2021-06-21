package tfar.upgradableshields.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IHasArm;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3f;
import tfar.upgradableshields.ModItems;
import tfar.upgradableshields.UpgradableShieldItem;

public class UltimateShieldLayer<T extends LivingEntity, M extends EntityModel<T> & IHasArm> extends LayerRenderer<T, M> {

    public UltimateShieldLayer(IEntityRenderer<T, M> entityRendererIn) {
        super(entityRendererIn);
    }

    public void render(MatrixStack matrices, IRenderTypeBuffer bufferIn, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack stack = findShield(livingEntity);
        if (!stack.isEmpty()) {
            matrices.push();
            if (this.getEntityModel().isChild) {
                float f = 0.5F;
                matrices.translate(0.0D, 0.75D, 0.0D);
                matrices.scale(0.5F, 0.5F, 0.5F);
            }

             ItemStack rightLeft = new ItemStack(ModItems.SHIELD_RIGHT_LEFT);
             rightLeft.setTag(stack.getTag());
             ItemStack back = new ItemStack(ModItems.SHIELD_BACK);
             back.setTag(stack.getTag());

            if (livingEntity.isActiveItemStackBlocking()) {
                this.renderExtraShield(livingEntity, rightLeft, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, HandSide.RIGHT, matrices, bufferIn, packedLight);
                this.renderExtraShield(livingEntity, rightLeft, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.LEFT, matrices, bufferIn, packedLight);

                this.renderExtraShield(livingEntity, back, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, HandSide.RIGHT, matrices, bufferIn, packedLight);
            }
            matrices.pop();
        }
    }

    private ItemStack findShield(LivingEntity entity) {

        boolean flag = entity.getPrimaryHand() == HandSide.RIGHT;
        ItemStack right = flag ? entity.getHeldItemOffhand() : entity.getHeldItemMainhand();
        ItemStack left = flag ? entity.getHeldItemMainhand() : entity.getHeldItemOffhand();

        if(right.getItem() instanceof UpgradableShieldItem &&  right.getDisplayName().getString().contains("ultimate"))
        return right;
           if     (left.getDisplayName().getString().contains("ultimate") && left.getItem() instanceof UpgradableShieldItem)
               return left;
           return ItemStack.EMPTY;
    }

    private void renderExtraShield(LivingEntity entity, ItemStack p_229135_2_, ItemCameraTransforms.TransformType p_229135_3_, HandSide side, MatrixStack matrices, IRenderTypeBuffer p_229135_6_, int p_229135_7_) {
        if (!p_229135_2_.isEmpty()) {
            matrices.push();
            this.getEntityModel().translateHand(side, matrices);
            matrices.rotate(Vector3f.XP.rotationDegrees(-90.0F));
            matrices.rotate(Vector3f.YP.rotationDegrees(180.0F));
            boolean flag = side == HandSide.LEFT;
            matrices.translate((float)(flag ? -1 : 1) / 16d, 1/8D, -1/16D);
            Minecraft.getInstance().getFirstPersonRenderer().renderItemSide(entity, p_229135_2_, p_229135_3_, flag, matrices, p_229135_6_, p_229135_7_);
            matrices.pop();
        }
    }
}
