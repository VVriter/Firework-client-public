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
        animationUtil.setValues(1, 0.01f);
        if(GuiInfo.frames.isEmpty())
            GuiInfo.setupFrames();

        isDragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GuiInfo.frames.forEach(Frame::draw);

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
        animationUtil.update();
        glPushMatrix();

        glScaled(animationUtil.width, animationUtil.width, 1);
        glTranslated((width / 2f) * (1 - animationUtil.width), (height / 2f) * (1 - animationUtil.width), 0);
        GuiInfo.frames.forEach(frame -> frame.update(mouseX, mouseY, state));

        glPopMatrix();
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        isDragging = false;
    }
}
