package com.mw.raumships.client.gui.rings;

import com.mw.raumships.RaumShipsMod;
import com.mw.raumships.common.blocks.rings.DtoRingsModel;
import com.mw.raumships.server.network.SaveRingsParametersToServer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RingsGUI extends GuiBase {
    public static final int FRAME_THICKNESS = 4;
    public static final int PADDING = 8;
    public static final int COLOR_TITLE_OK = 0xAA5500;
    public static final int COLOR_TITLE_PROBLEM = 0xB36262;

    private BlockPos pos;
    public RingsGuiState state;
    public boolean isOpen;

    public RingsGUI(BlockPos pos, RingsGuiState state) {
        super(196, 160, FRAME_THICKNESS, FRAME_COLOR, BG_COLOR, TEXT_COLOR, PADDING);

        this.pos = pos;
        this.state = state;
    }

    private List<GuiTextField> textFields = new ArrayList<>();

    private GuiTextField addressTextField;
    private GuiTextField nameTextField;
    private RingsGuiButton saveButton;

    @Override
    public void initGui() {
        addressTextField = createTextField(50, 20, 1, state.isInGrid() ? "" + state.getAddress() : "");
        textFields.add(addressTextField);

        nameTextField = createTextField(50, 35, 16, state.getName());
        textFields.add(nameTextField);

        saveButton = new RingsGuiButton(id++,
                getBottomRightInside(false) - 90,
                getBottomRightInside(true) - 20,
                90, 20,
                I18n.format("gui.rings.save"));
        buttonList.add(saveButton);

        isOpen = true;
    }

    @Override
    public void onGuiClosed() {
        isOpen = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mouseX -= getTopLeftAbsolute(false);
        mouseY -= getTopLeftAbsolute(true);

        GlStateManager.pushMatrix();
        translateToCenter();
        drawBackground();

        if (state.isInGrid()) {
            drawVerticallCenteredString(I18n.format("gui.rings.title") + state.getAddress(), 0, 0, COLOR_TITLE_OK);
        } else {
            drawVerticallCenteredString(I18n.format("gui.rings.notInGrid"), 0, 0, COLOR_TITLE_PROBLEM);
        }

        drawString(I18n.format("gui.rings.title.address"), 0, 20, 0x00AA00);
        drawString(I18n.format("gui.rings.title.name"), 0, 35, 0x00AAAA);

        for (GuiTextField tf : textFields)
            drawTextBox(tf);

        int y = 50;
        for (DtoRingsModel rings : state.getRings()) {
            drawString("" + rings.getAddress(), 60, y, 0x00AA00);
            drawString(rings.getName(), 70, y, 0x00AAAA);

            y += 12;
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button == saveButton) {
            EntityPlayer player = Minecraft.getMinecraft().player;

            try {
                int address = Integer.parseInt(addressTextField.getText());
                String name = nameTextField.getText();

                if (address > 0 && address <= 6) {
                    RaumShipsMod.proxy.getNetworkWrapper().sendToServer(new SaveRingsParametersToServer(pos, player, address, name));
                } else {
                    player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.wrong_address"), true);
                }
            } catch (NumberFormatException e) {
                player.sendStatusMessage(new TextComponentTranslation("tile.rings_block.wrong_address"), true);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        for (GuiTextField tf : textFields)
            tf.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        for (GuiTextField tf : textFields)
            tf.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        mouseX -= getTopLeftAbsolute(false);
        mouseY -= getTopLeftAbsolute(true);

        super.mouseClicked(mouseX, mouseY, mouseButton);

        for (GuiTextField tf : textFields)
            tf.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
