package com.firework.client.Implementations.Utill.Render.Shaders;

import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class CShader extends FramebufferShader{
    public float time;
    public float timeMult = 0.05f;

    public CShader(String name) {
        super(name);
    }

    @Override
    public void setupUniforms() {
        this.setupUniform("resolution");
        this.setupUniform("time");
    }

    @Override
    public void updateUniforms() {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        GL20.glUniform2f(this.getUniform("resolution"), scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight());
        GL20.glUniform1f(this.getUniform("time"), this.time);
        time += timeMult * animationSpeed;
    }
}
