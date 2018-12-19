package com.mw.raumships.client.gui.zpm;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.OneSlotEnergyTileEntityBase;
import com.mw.raumships.common.calc.PowerUnitCalculator;
import com.mw.raumships.common.items.ZPMItem;
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
    public static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance(Locale.getDefault());

    private static final int FIRST_LINE_X = 11 + ZpmHubContainer.SLOT_X_SPACING;
    private static final int FIRST_LINE_Y = 7;
    private static final int FONT_COLOR = 0x000000;

    private final OneSlotEnergyTileEntityBase zpmHubTileEntity;
    private final String textNoZpm;
    private final String textZpmInstalled;

    public ZpmHubGuiContainer(InventoryPlayer invPlayer, OneSlotEnergyTileEntityBase zpmHubTileEntity) {
        super(new ZpmHubContainer(invPlayer, zpmHubTileEntity));

        this.zpmHubTileEntity = zpmHubTileEntity;
        this.textNoZpm = I18n.format("custom.zpmhub.gui.noZpm");
        this.textZpmInstalled = I18n.format("custom.zpmhub.gui.zpmInstalled");
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(GUI_TEXTURE);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        boolean before = fontRenderer.getUnicodeFlag();
        try {
            fontRenderer.setUnicodeFlag(true);

            if (!zpmHubTileEntity.hasZpm()) {
                fontRenderer.drawString(textNoZpm, FIRST_LINE_X, FIRST_LINE_Y, FONT_COLOR);
            } else {
                fontRenderer.drawString(textZpmInstalled, FIRST_LINE_X, FIRST_LINE_Y, FONT_COLOR);

                long rf = zpmHubTileEntity.getEnergyStoredAsLong();
                fontRenderer.drawString("FE / RF / T: " + NUMBER_FORMAT.format(rf), FIRST_LINE_X, FIRST_LINE_Y + 20, FONT_COLOR);

                double eu = PowerUnitCalculator.toEU(rf);
                fontRenderer.drawString("EU: " + NUMBER_FORMAT.format(eu), FIRST_LINE_X, FIRST_LINE_Y + 30, FONT_COLOR);

                double j = PowerUnitCalculator.toJ(rf);
                fontRenderer.drawString("J: " + NUMBER_FORMAT.format(j), FIRST_LINE_X, FIRST_LINE_Y + 40, FONT_COLOR);

                double mj = PowerUnitCalculator.toMJ(rf);
                fontRenderer.drawString("MJ: " + NUMBER_FORMAT.format(mj), FIRST_LINE_X, FIRST_LINE_Y + 50, FONT_COLOR);

                double gj = PowerUnitCalculator.togJ(rf);
                fontRenderer.drawString("gJ: " + NUMBER_FORMAT.format(gj), FIRST_LINE_X, FIRST_LINE_Y + 60, FONT_COLOR);

                GL11.glPushMatrix();
                GL11.glScalef(1.5F, 1.5F, 1.5F);

                int percentage = (int)(rf * 100.0 / ZPMItem.MAX_ZPM_ENERGY);
                fontRenderer.drawStringWithShadow(percentage + " %", FIRST_LINE_X + 63, FIRST_LINE_Y + 39, FONT_COLOR);

                GL11.glPopMatrix();
            }
        } finally {
            fontRenderer.setUnicodeFlag(before);
        }
    }
}
