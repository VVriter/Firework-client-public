package com.firework.client.Implementations.UI.ConfigUi;

import com.firework.client.Firework;
import com.firework.client.Implementations.UI.Particles.ParticleInfo;
import com.firework.client.Implementations.UI.Particles.ParticleSystem;
import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import com.firework.client.Implementations.Utill.Render.Rounded.RenderRound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class CGui extends GuiScreen {
    public ParticleSystem particleSystem;

    public AnimationUtil popOutAnimation;
    public AnimationUtil scrollAnimation;

    ScaledResolution sr;
    public CGui(){
        particleSystem = new ParticleSystem();

        popOutAnimation = new AnimationUtil();
        popOutAnimation.setValues(1, 0.1f);
        scrollAnimation = new AnimationUtil();

    }

    @Override
    public void initGui() {
        sr = new ScaledResolution(mc);
        guiCordX = sr.getScaledWidth()/2-150;
        guiCordY = sr.getScaledHeight()/2-100;
    }

    int guiCordX;
    int guiCordY;
    boolean isGuiDragged;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if(ParticleInfo.isEnabled) {
            particleSystem.updatePositions();
            particleSystem.drawLines();
            particleSystem.drawParticles();
        }

        popOutAnimation.update();
        glPushMatrix();
        glScaled(popOutAnimation.width, popOutAnimation.width, 1);
        glTranslated((width / 2) * (1 - popOutAnimation.width), height * (1 - popOutAnimation.width), 0);

        RenderRound.drawGradientRound(guiCordX-3,guiCordY-3,303,203,5,Color.MAGENTA,Color.CYAN,Color.GREEN,Color.ORANGE);
        RenderRound.drawRound(guiCordX, guiCordY+10,300-3,200-13,1,true, Firework.colorManager.getColor());
        Point2D.Double start = new Point2D.Double(guiCordX+100,guiCordY+9);
        Point2D.Double end = new Point2D.Double(guiCordX+100,guiCordY+200-2);
        RenderUtils2D.drawGradientLine(start,end,Color.BLUE,Color.ORANGE,5);

        RenderRound.drawRound(guiCordX+100+3,guiCordY+25,191,10,1,true,Color.RED);
        Firework.customFontManager.drawCenteredString("Save current config",guiCordX+100+3+95,guiCordY+25,Color.WHITE.getRGB());

        RenderRound.drawRound(guiCordX+100+3,guiCordY+39,191,10,1,true,Color.RED);
        Firework.customFontManager.drawCenteredString("Load",guiCordX+100+3+95,guiCordY+39,Color.WHITE.getRGB());

        RenderRound.drawRound(guiCordX+100+3,guiCordY+39+14,191,10,1,true,Color.RED);
        Firework.customFontManager.drawCenteredString("Unload",guiCordX+100+3+95,guiCordY+39+14,Color.WHITE.getRGB());

        RenderRound.drawRound(guiCordX+100+3,guiCordY+39+14+14,191,10,1,true,Color.RED);
        Firework.customFontManager.drawCenteredString("Update Configs",guiCordX+100+3+95,guiCordY+39+14+14,Color.WHITE.getRGB());

        Firework.customFontManager.drawCenteredString("Config Manager",guiCordX+150,guiCordY-1,Color.WHITE.getRGB());
        Firework.customFontManager.drawCenteredString("Configs List",guiCordX+50,guiCordY+13,Color.WHITE.getRGB());
        Firework.customFontManager.drawCenteredString(currentConfig(),guiCordX+100+3+95,guiCordY+13, Color.WHITE.getRGB());

        if (isGuiDragged) {
            guiCordX = mouseX;
            guiCordY = mouseY;
        }
        glPopMatrix();
    }



    void saveCurrentConfig() {

    }

    void updateConfigList() {

    }
    String currentConfig() {
        return "Current Config";
    }


    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) throws IOException {
        super.mouseClicked(mouseX, mouseY, state);
        isGuiDragged = true;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isGuiDragged = false;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
