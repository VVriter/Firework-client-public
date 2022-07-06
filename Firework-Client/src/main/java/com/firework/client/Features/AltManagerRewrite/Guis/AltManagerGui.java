package com.firework.client.Features.AltManagerRewrite.Guis;

import com.firework.client.Features.Modules.Client.Client;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;

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
        this.buttonList.add(new GuiButton(1, this.width / 2 + 46, sr.getScaledHeight()-23 , 100,
                20, "Cancel"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 46, sr.getScaledHeight()-46 , 100,
                20, "New Account"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        if (button.id == 1) {
            mc.displayGuiScreen(new GuiMainMenu());
        } else if (button.id == 2) {

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

        //Center
        Point2D.Double screenCenter = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);

        //LeftLine
        Point2D.Double leftLine1stpoint = new Point2D.Double(screenCenter.getX()-150, screenCenter.getY()+120);
        Point2D.Double leftLine2ndpoint = new Point2D.Double(screenCenter.getX()-150, screenCenter.getY()-120);
        RenderUtils2D.drawGradientLine(leftLine1stpoint,leftLine2ndpoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //RightLine
        Point2D.Double RightLine1stpoint = new Point2D.Double(screenCenter.getX()+150, screenCenter.getY()+120);
        Point2D.Double RightLine2ndpoint = new Point2D.Double(screenCenter.getX()+150, screenCenter.getY()-120);
        RenderUtils2D.drawGradientLine(RightLine1stpoint,RightLine2ndpoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //UpLine
        Point2D.Double upline1stPoint = new Point2D.Double(screenCenter.getX() - sr.getScaledWidth()/2, screenCenter.getY()-120);
        Point2D.Double upline2ndPoint = new Point2D.Double(screenCenter.getX() + sr.getScaledWidth()/2, screenCenter.getY()-120);
        RenderUtils2D.drawGradientLine(upline1stPoint,upline2ndPoint,new Color(140, 40, 175),new Color(78, 129, 218),5);

        //DownLine
        Point2D.Double downline1stPoint = new Point2D.Double(screenCenter.getX() - sr.getScaledWidth()/2, screenCenter.getY()+120);
        Point2D.Double downline2ndPoint = new Point2D.Double(screenCenter.getX() + sr.getScaledWidth()/2, screenCenter.getY()+120);
        RenderUtils2D.drawGradientLine(downline1stPoint,downline2ndPoint,new Color(140, 40, 175),new Color(78, 129, 218),5);
    }

}
