package com.firework.client.Features.AltManager.Guis;

import com.firework.client.Implementations.Utill.Render.RainbowUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

public class AltManagerGui extends GuiScreen {
    public AltManagerGui(){
        super();
    }

    @Override
    public void initGui() {

        ScaledResolution sr = new ScaledResolution(this.mc);

        int i = this.height / 4 + 48;

        this.buttonList.clear();
        this.buttonList.add(new GuiButton(1, this.width / 2 + 51, sr.getScaledHeight()-28 , 100,
                20, "Cancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 51, sr.getScaledHeight()-51 , 100,
                20, "New Account"));

        this.buttonList.add(new GuiButton(3, this.width / 2 - 49, sr.getScaledHeight()-28 , 100,
                20, "Login"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 49, sr.getScaledHeight()-51 , 100,
                20, "Delete"));

        this.buttonList.add(new GuiButton(5, this.width / 2 - 149, sr.getScaledHeight()-28 , 100,
                20, "Import"));
        this.buttonList.add(new GuiButton(6, this.width / 2 - 149, sr.getScaledHeight()-51 , 100,
                20, "Edit"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        } else if (button.id == 2) {
            mc.displayGuiScreen(new GuiAddFirst());
        }
        super.actionPerformed(button);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        drawDefaultBackground();
        drawLinesStuff();




        for (GuiButton guiButton : this.buttonList) {
            guiButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }




    private void drawLinesStuff() {
        ScaledResolution sr = new ScaledResolution(this.mc);


       // Firework.customFontForAlts.drawCenteredString("Firework account manager",sr.getScaledWidth()/2,sr.getScaledHeight()/2-140, new Color(222, 53, 53).getRGB());

      //  drawCenteredString(fontRenderer,"Firework account manager",sr.getScaledWidth()/2,sr.getScaledHeight()/2-140, RainbowUtil.generateRainbowFadingColor(1,true));

        drawString(2,"Firework account manager",sr.getScaledWidth()/2+15,sr.getScaledHeight()/2-160, RainbowUtil.generateRainbowFadingColor(1,true));


        //Center
        Point2D.Double screenCenter = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);

        //LeftLine
        Point2D.Double leftLine1stpoint = new Point2D.Double(screenCenter.getX()-165, screenCenter.getY()+110);
        Point2D.Double leftLine2ndpoint = new Point2D.Double(screenCenter.getX()-165, screenCenter.getY()-110);
        RenderUtils2D.drawGradientLine(leftLine1stpoint,leftLine2ndpoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //RightLine
        Point2D.Double RightLine1stpoint = new Point2D.Double(screenCenter.getX()+165, screenCenter.getY()+110);
        Point2D.Double RightLine2ndpoint = new Point2D.Double(screenCenter.getX()+165, screenCenter.getY()-110);
        RenderUtils2D.drawGradientLine(RightLine1stpoint,RightLine2ndpoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //UpLine
        Point2D.Double upline1stPoint = new Point2D.Double(screenCenter.getX() - sr.getScaledWidth()/2, screenCenter.getY()-110);
        Point2D.Double upline2ndPoint = new Point2D.Double(screenCenter.getX() + sr.getScaledWidth()/2, screenCenter.getY()-110);
        RenderUtils2D.drawGradientLine(upline1stPoint,upline2ndPoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //DownLine
        Point2D.Double downline1stPoint = new Point2D.Double(screenCenter.getX() - sr.getScaledWidth()/2, screenCenter.getY()+110);
        Point2D.Double downline2ndPoint = new Point2D.Double(screenCenter.getX() + sr.getScaledWidth()/2, screenCenter.getY()+110);
        RenderUtils2D.drawGradientLine(downline1stPoint,downline2ndPoint,new Color(140, 40, 175),new Color(78, 129, 218),5);
    }


    public static void drawString(final double scale, final String text,
                                  final float x, final float y, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, scale);
        Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, x, y, color);
        GlStateManager.popMatrix();
    }

}
