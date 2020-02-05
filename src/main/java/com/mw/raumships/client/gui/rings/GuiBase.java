package com.mw.raumships.client.gui.rings;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;

public class GuiBase extends GuiScreen {
    public static final int FRAME_COLOR = 0xFF181A1F;
    public static final int BG_COLOR = 0xF2272B33;
    public static final int TEXT_COLOR = 0x6B768C;

    protected int imageWidth;
    protected int imageHeight;
    protected int frameThickness;
    protected int frameColor;
    protected int bgColor;
    protected int textColor;
    protected int padding;
    protected int id;

    public GuiBase(int w, int h, int frameThickness, int frameColor, int bgColor, int textColor, int padding) {
        this.imageWidth = w;
        this.imageHeight = h;
        this.frameThickness = frameThickness;

        this.frameColor = frameColor;
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.padding = padding;

        this.id = 0;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    protected int getTopLeftInside() {
        return frameThickness + padding;
    }

    protected int getBottomRightInside(boolean returnHeight) {
        return returnHeight ? (imageHeight - getTopLeftInside()) : (imageWidth - getTopLeftInside());
    }

    protected int getTopLeftAbsolute(boolean returnHeight) {
        return (int) (returnHeight ? ((height - imageHeight) / 2f) : ((width - imageWidth) / 2f));
    }

    protected void translateToCenter() {
        GlStateManager.translate((width - imageWidth) / 2f, (height - imageHeight) / 2f, 0);
    }

    protected void drawBackground() {
        frame(imageWidth, imageHeight, frameThickness, frameColor);

        drawRect(frameThickness, frameThickness, imageWidth - frameThickness, imageHeight - frameThickness, bgColor);
    }

    protected void frame(int w, int h, int thickness, int color) {
        // Up
        drawRect(0, 0, w, thickness, color);

        // Down
        drawRect(0, h - thickness, w, h, color);

        // Left
        drawRect(0, thickness, thickness, h - thickness, color);

        // Right
        drawRect(w - thickness, thickness, w, h - thickness, color);
    }

    protected void drawString(String text, int x, int y, int color) {
        x += getTopLeftInside();
        y += getTopLeftInside() - 1;

        fontRenderer.drawStringWithShadow(text, x, y, color);
    }

    protected void drawVerticallCenteredString(String text, int x, int y, int color) {
        int len = fontRenderer.getStringWidth(text);
        x += ((imageWidth - getTopLeftInside()) - len) / 2;

        drawString(text, x, y, color);
    }

    protected void drawTextBox(GuiTextField tf) {
        int x = tf.x - 2;
        int y = tf.y - 2;
        int x2 = tf.x + tf.width;
        int y2 = tf.y + tf.height;

        drawRect(x - 1, y - 1, x2 + 1, y2 + 1, frameColor);
        drawRect(x, y, x2, y2, 0xFF1D2026);

        tf.drawTextBox();
    }

    protected GuiTextField createTextField(int x, int y, int maxLength, String defaultText) {
        int x1 = x + getTopLeftInside();
        int y1 = y + getTopLeftInside();

        GuiTextField tf = new GuiTextField(id++, fontRenderer, x1 + 3, y1 - 1, getBottomRightInside(false) - x1 - 6, 10);

        tf.setMaxStringLength(maxLength);
        tf.setEnableBackgroundDrawing(false);
        tf.setText(defaultText);

        tf.width += 2;
        tf.height -= 1;

        return tf;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
