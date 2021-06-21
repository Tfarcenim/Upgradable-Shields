package tfar.upgradableshields.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import tfar.upgradableshields.CoinConfigs;
import tfar.upgradableshields.UpgradableShields;
import tfar.upgradableshields.UpgradeBookContainer;
import tfar.upgradableshields.net.C2SUnlockUpgradePacket;
import tfar.upgradableshields.net.PacketHandler;
import tfar.upgradableshields.util.UpgradeType;

import java.util.ArrayList;
import java.util.List;

public class UpgradeScreen extends ContainerScreen<UpgradeBookContainer> {

    private static final ResourceLocation GUI_TEXTURES = new ResourceLocation(UpgradableShields.MODID, "textures/gui/upgrade_book.png");

    public static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(UpgradableShields.MODID, "textures/gui/upgrade_button.png");
    public static final ResourceLocation BUTTON_TEXTURE_LOCKED = new ResourceLocation(UpgradableShields.MODID, "textures/gui/upgrade_button_locked.png");


    public UpgradeScreen(UpgradeBookContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void init() {
        super.init();

        int xStart = guiLeft+ 5;

        int yStart = this.height / 2 - 65;

        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.ARROW));

        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.REFLECTION));

        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.SUMMON));

        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20,0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.LIGHTNING));

        yStart += 30;
        xStart -= 120;


        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.CHARGE_DASH));

        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.JUMP));




        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.ENDER));


        this.addButton(new UpgradeButton(xStart+=30, yStart,
                20, 20, 0, 0,
                20, BUTTON_TEXTURE, 256, 256,
                button -> sendToServer((UpgradeButton)button),
                this::onTooltip,
                StringTextComponent.EMPTY,
                UpgradeType.ULTIMATE));
    }

    private static void sendToServer(UpgradeButton button) {
        PacketHandler.INSTANCE.sendToServer(new C2SUnlockUpgradePacket(button.type));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bindTexture(GUI_TEXTURES);
        int i = this.guiLeft;
        int j = (this.height - this.ySize) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.xSize, this.ySize);
    }

    private void onTooltip(Button button, MatrixStack matrixStack, int mouseX, int mouseY) {
        List<ITextComponent> list = new ArrayList<>();
        UpgradeButton upgradeButton = (UpgradeButton)button;

        boolean locked = container.upgradeData.locked(upgradeButton.type);

        if (locked) {
            list.add(new StringTextComponent("Locked, requires " +upgradeButton.type.getCost()+ " coins"));
        } else {
            list.add(new StringTextComponent(upgradeButton.type.toString().toLowerCase()));
        }

        func_243308_b(matrixStack, list, mouseX, mouseY);
    }
}
