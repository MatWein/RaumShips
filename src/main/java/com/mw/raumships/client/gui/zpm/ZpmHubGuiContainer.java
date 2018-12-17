package com.mw.raumships.client.gui.zpm;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.ZPMHubTileEntity;
import com.mw.raumships.common.calc.PowerUnitCalculator;
import com.mw.raumships.server.gui.zpm.ZpmHubContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.text.NumberFormat;
import java.util.Locale;

@SideOnly(Side.CLIENT)
public class ZpmHubGuiContainer extends GuiContainer {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation(RaumShipsMod.MODID, "textures/gui/zpmhub.png");
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.getDefault());

    private static final int FIRST_LINE_X = 20 + ZpmHubContainer.SLOT_X_SPACING;
    private static final int FIRST_LINE_Y = 10;
    private static final int FONT_COLOR = 0x000000;

    private final ZPMHubTileEntity zpmHubTileEntity;
    private final String textNoZpm;
    private final String textZpmInstalled;

    public ZpmHubGuiContainer(InventoryPlayer invPlayer, ZPMHubTileEntity zpmHubTileEntity) {
        super(new ZpmHubContainer(invPlayer, zpmHubTileEntity));

        this.zpmHubTileEntity = zpmHubTileEntity;
        this.textNoZpm = I18n.format("custom.zpmhub.gui.noZpm");
        this.textZpmInstalled = I18n.format("custom.zpmhub.gui.zpmInstalled");
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_TEXTURE);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        GL11.glScalef(0.75F, 0.75F, 0.75F);

        if (!zpmHubTileEntity.hasZpm()) {
            fontRenderer.drawString(textNoZpm, FIRST_LINE_X, FIRST_LINE_Y, FONT_COLOR);
        } else {
            fontRenderer.drawString(textZpmInstalled, FIRST_LINE_X, FIRST_LINE_Y, FONT_COLOR);

            int rf = zpmHubTileEntity.getEnergyStored();
            fontRenderer.drawString("FE / RF / T: " + NUMBER_FORMAT.format(rf), FIRST_LINE_X, FIRST_LINE_Y + 25, FONT_COLOR);

            double eu = PowerUnitCalculator.toEU(rf);
            fontRenderer.drawString("EU: " + NUMBER_FORMAT.format(eu), FIRST_LINE_X, FIRST_LINE_Y + 40, FONT_COLOR);

            double j = PowerUnitCalculator.toJ(rf);
            fontRenderer.drawString("J: " + NUMBER_FORMAT.format(j), FIRST_LINE_X, FIRST_LINE_Y + 55, FONT_COLOR);

            double mj = PowerUnitCalculator.toMJ(rf);
            fontRenderer.drawString("MJ: " + NUMBER_FORMAT.format(mj), FIRST_LINE_X, FIRST_LINE_Y + 70, FONT_COLOR);

            double gj = PowerUnitCalculator.togJ(rf);
            fontRenderer.drawString("gJ: " + NUMBER_FORMAT.format(gj), FIRST_LINE_X, FIRST_LINE_Y + 85, FONT_COLOR);
        }

        GL11.glPopMatrix();
    }
}
