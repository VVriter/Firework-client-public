package com.firework.client.Features.IngameGuis.GuiMainMenu;

import com.firework.client.Firework;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;

public class GuiMainMenu extends GuiScreen {

    ScaledResolution sr = null;

    @Override
    public void initGui() {
        super.initGui();
        sr = new ScaledResolution(mc);
        Firework.shaders.init();
        mc.gameSettings.guiScale = 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
       drawShaderShit(mouseX,mouseY);
        //RenderRound.drawRound(sr.getScaledWidth()/2-100, sr.getScaledHeight()/2-20,200,80,15,true, new Color(255, 255, 255, 48));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {

    }




    void drawShaderShit(int mouseX, int mouseY) {
        if( Firework.shaders.currentshader != null ) {
            GlStateManager.disableCull( );

            Firework.shaders.currentshader.useShader( width * 2, height * 2, mouseX * 2, mouseY * 2, ( System.currentTimeMillis( ) - Firework.shaders.time ) / 1000f );


            GL11.glBegin( GL11.GL_QUADS );
            GL11.glVertex2f( -1f, -1f );
            GL11.glVertex2f( -1f, 1f );
            GL11.glVertex2f( 1f, 1f );
            GL11.glVertex2f( 1f, -1f );
            GL11.glEnd( );
            GL20.glUseProgram( 0 );
        }
    }

}
