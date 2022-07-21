package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class GuiN extends GuiScreen {
    public static boolean isDragging = true;

    public AnimationUtil animationUtil;

    public GuiN(){
        animationUtil = new AnimationUtil();
        animationUtil.setValues(1, 0.1f);
        if(GuiInfo.frames.isEmpty())
            GuiInfo.setupFrames();

        isDragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        animationUtil.update();
        animationUtil.setValues(1, 0.1f);
        glPushMatrix();

        glScaled(animationUtil.width, animationUtil.width, 1);
        glTranslated((width / 2) * (1 - animationUtil.width), height * (1 - animationUtil.width), 0);
        GuiInfo.frames.forEach(Frame::draw);

        glPopMatrix();
        if(isDragging) {
            try {
                mouseClicked(mouseX, mouseY, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int state) throws IOException {
        super.mouseClicked(mouseX, mouseY, state);
        GuiInfo.frames.forEach(frame -> frame.update(mouseX, mouseY, state));
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDragging = false;
    }
}
