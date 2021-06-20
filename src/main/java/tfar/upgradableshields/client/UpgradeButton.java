package tfar.upgradableshields.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import tfar.upgradableshields.UpgradeBookContainer;
import tfar.upgradableshields.util.UpgradeType;

public class UpgradeButton extends ImageButton {

    private int textureWidth0;
    private int textureHeight0;
    private int xTexStart0;
    private int yTexStart0;
    private int yDiffText0;
    public final UpgradeType type;

    public UpgradeButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, IPressable onPressIn, UpgradeType type) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, onPressIn);

        this.type = type;
    }

    public UpgradeButton(int xIn, int yIn, int widthIn, int heightIn, int xTexStartIn, int yTexStartIn, int yDiffTextIn, ResourceLocation resourceLocationIn, int textureWidth, int textureHeight, IPressable onPressIn, UpgradeType type) {
        super(xIn, yIn, widthIn, heightIn, xTexStartIn, yTexStartIn, yDiffTextIn, resourceLocationIn, textureWidth, textureHeight, onPressIn);
        this.type = type;
    }

    public UpgradeButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, IPressable onPress, ITextComponent title, UpgradeType type) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, title);
        this.type = type;
    }

    public UpgradeButton(int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffText, ResourceLocation resourceLocation, int textureWidth, int textureHeight, IPressable onPress, ITooltip onTooltip, ITextComponent title, UpgradeType type) {
        super(x, y, width, height, xTexStart, yTexStart, yDiffText, resourceLocation, textureWidth, textureHeight, onPress, onTooltip, title);
        this.type = type;
        this.xTexStart0 = xTexStart;
        this.yTexStart0 = yTexStart;
        yDiffText0 = yDiffText;
        this.textureWidth0 = textureWidth;
        this.textureHeight0 = textureHeight;
    }

    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();

        Screen screen = minecraft.currentScreen;
        if (screen instanceof UpgradeScreen) {
            UpgradeBookContainer upgradeBookContainer = ((UpgradeScreen) screen).getContainer();

            ResourceLocation rl = upgradeBookContainer.upgradeData.locked(type) ? UpgradeScreen.BUTTON_TEXTURE_LOCKED : UpgradeScreen.BUTTON_TEXTURE;

            minecraft.getTextureManager().bindTexture(rl);
            int i = this.yTexStart0;
            if (this.isHovered()) {
                i += this.yDiffText0;
            }

            RenderSystem.enableDepthTest();
            blit(matrixStack, this.x, this.y, (float) this.xTexStart0, (float) i, this.width, this.height, this.textureWidth0, this.textureHeight0);
            if (this.isHovered()) {
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }
        }
    }
}
