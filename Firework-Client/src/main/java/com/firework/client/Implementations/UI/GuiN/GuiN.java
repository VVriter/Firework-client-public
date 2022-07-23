package com.firework.client.Implementations.UI.GuiN;

import com.firework.client.Implementations.Utill.Render.AnimationUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static net.minecraft.util.math.MathHelper.floor;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class GuiN extends GuiScreen {
    public static boolean isDragging = true;

    public static double offsetY = 0;
    public float lastScroll = 1;

    public AnimationUtil popOutAnimation;
    public AnimationUtil scrollAnimation;

    public GuiN(){
        popOutAnimation = new AnimationUtil();
        popOutAnimation.setValues(1, 0.1f);
        scrollAnimation = new AnimationUtil();

        if(GuiInfo.frames.isEmpty())
            GuiInfo.setupFrames();

        isDragging = false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        offsetY = scrollAnimation.width;
        scrollAnimation.update();

        popOutAnimation.update();
        glPushMatrix();

        glScaled(popOutAnimation.width, popOutAnimation.width, 1);
        glTranslated((width / 2) * (1 - popOutAnimation.width), height * (1 - popOutAnimation.width), 0);
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
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        float scroll = Math.signum(Mouse.getDWheel());
        int speed = floor(com.firework.client.Features.Modules.Client.Gui.scrollSpeed.getValue());

        if(scroll != 0) {
            if(lastScroll != scroll)
                scrollAnimation.tasks.clear();
            scrollAnimation.setValues(scrollAnimation.width + speed * scroll, 0.4f);
        }
        lastScroll = scroll;
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