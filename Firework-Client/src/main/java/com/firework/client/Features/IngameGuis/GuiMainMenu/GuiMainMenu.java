package com.firework.client.Features.IngameGuis.GuiMainMenu;

import com.firework.client.Firework;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class GuiMainMenu extends GuiScreen {
    @Override
    public void initGui() {
        super.initGui();
        Firework.shaders.init();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if( Firework.shaders.currentshader != null )
        {
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
