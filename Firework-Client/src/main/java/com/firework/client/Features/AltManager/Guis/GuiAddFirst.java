package com.firework.client.Features.AltManager.Guis;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

public class GuiAddFirst extends GuiScreen {
    public GuiAddFirst(){
        super();
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(this.mc);

        int i = this.height / 4 + 48;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 - 49, sr.getScaledHeight()/2+40 , 100,
                20, "§cCancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 51, sr.getScaledHeight()/2 , 100,
                20, "Microsoft"));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 149, sr.getScaledHeight()/2 , 100,
                20, "Mojang"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 49, sr.getScaledHeight()/2 , 100,
                20, "Crack"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new AltManagerGui());
        } else if (button.id == 2) {

        } else if (button.id == 3) {
            //mc.displayGuiScreen(new GuiAddMojang());
        } else if (button.id == 4) {
            mc.displayGuiScreen(new GuiAddCrack());
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();




        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }


    public static void drawString(final double scale, final String text,
                                  final float x, final float y, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        GlStateManager.popMatrix();
    }
}
