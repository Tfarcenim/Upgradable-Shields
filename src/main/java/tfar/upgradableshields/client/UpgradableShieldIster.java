package tfar.upgradableshields.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.model.ShieldModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import tfar.upgradableshields.UpgradableShields;
import tfar.upgradableshields.util.UpgradeType;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

public class UpgradableShieldIster extends ItemStackTileEntityRenderer {

    private ShieldModel shieldModel = new ShieldModel();

    public static final RenderMaterial LOCATION_UPGRADE_SHIELD_BASE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/upgradable_shield_base"));

    public static final RenderMaterial REFLECTION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/reflection_shield"));
    public static final RenderMaterial LIGHTNING = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/lightning_shield"));
    public static final RenderMaterial DASH = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/charge_dash_shield"));
    public static final RenderMaterial JUMP = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/jump_shield"));
    public static final RenderMaterial SUMMON = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/summon_shield"));
    public static final RenderMaterial ENDER = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/ender_shield"));
    public static final RenderMaterial ARROW = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/arrow_shield"));
    public static final RenderMaterial ULTIMATE = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS_TEXTURE, new ResourceLocation(UpgradableShields.MODID,"entity/ultimate_shield"));

    public static final Map<UpgradeType,RenderMaterial> matMap = new EnumMap<>(UpgradeType.class);

    static {
        matMap.put(UpgradeType.REFLECTION,REFLECTION);
        matMap.put(UpgradeType.LIGHTNING,LIGHTNING);
        matMap.put(UpgradeType.CHARGE_DASH,DASH);
        matMap.put(UpgradeType.JUMP,JUMP);
        matMap.put(UpgradeType.SUMMON,SUMMON);
        matMap.put(UpgradeType.ENDER,ENDER);
        matMap.put(UpgradeType.ARROW,ARROW);
        matMap.put(UpgradeType.ULTIMATE,ULTIMATE);
    }

    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        matrixStack.push();
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        RenderMaterial rendermaterial = getShieldTexture(stack);
        IVertexBuilder ivertexbuilder = rendermaterial.getSprite().wrapBuffer(ItemRenderer.getEntityGlintVertexBuilder(buffer, this.shieldModel.getRenderType(rendermaterial.getAtlasLocation()), true, stack.hasEffect()));
        this.shieldModel.func_228294_b_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        this.shieldModel.func_228293_a_().render(matrixStack, ivertexbuilder, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
    }

    public RenderMaterial getShieldTexture(ItemStack stack) {
        ITextComponent name = stack.getDisplayName();
        String raw = name.getString();
        for (Map.Entry<UpgradeType,RenderMaterial> type : matMap.entrySet()) {
            if (raw.toLowerCase(Locale.ROOT).contains(type.getKey().name().toLowerCase(Locale.ROOT))) {
                return type.getValue();
            }
        }
        return LOCATION_UPGRADE_SHIELD_BASE;
    }
}
