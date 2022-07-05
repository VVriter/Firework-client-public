package com.firework.client.Implementations.Utill.Render.Shaders;

import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.*;

import static com.firework.client.Implementations.Utill.Util.mc;

public abstract class FramebufferShader extends Shader {
    public static Framebuffer framebuffer;
    public boolean entityShadows;
    public int animationSpeed;

    public FramebufferShader(final String fragmentShader) {
        super(fragmentShader);
    }

    public void startDraw() {
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();
        (FramebufferShader.framebuffer = setupFrameBuffer(FramebufferShader.framebuffer)).framebufferClear();
        FramebufferShader.framebuffer.bindFramebuffer(true);
        entityShadows = mc.gameSettings.entityShadows;
        mc.gameSettings.entityShadows = false;
    }

    public void stopDraw() {
        mc.gameSettings.entityShadows = this.entityShadows;
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        mc.getFramebuffer().bindFramebuffer(true);
        mc.entityRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();
        startShader();
        mc.entityRenderer.setupOverlayRendering();
        drawFramebuffer(FramebufferShader.framebuffer);
        stopShader();
        mc.entityRenderer.disableLightmap();
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    public Framebuffer setupFrameBuffer(Framebuffer frameBuffer) {
        if (frameBuffer != null) frameBuffer.deleteFramebuffer();
        frameBuffer = new Framebuffer(mc.displayWidth, mc.displayHeight, true);
        return frameBuffer;
    }

    public void drawFramebuffer(final Framebuffer framebuffer) {
        final ScaledResolution scaledResolution = new ScaledResolution(mc);
        GL11.glBindTexture(3553, framebuffer.framebufferTexture);
        GL11.glBegin(7);
        RenderUtils2D.drawCompleteImage(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL11.glEnd();
        GL20.glUseProgram(0);
    }
}

