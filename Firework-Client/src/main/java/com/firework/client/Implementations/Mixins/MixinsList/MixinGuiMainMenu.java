package com.firework.client.Implementations.Mixins.MixinsList;

import com.firework.client.Firework;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin( GuiMainMenu.class )
public class MixinGuiMainMenu extends GuiScreen {

    @Inject(method = { "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V" }, at = { @At("RETURN") })
    public void addDrawPepsiStuff(final int mouseX, final int mouseY, final float partialTicks, final CallbackInfo ci) {
        this.drawString(this.fontRenderer, "Copyright Mojang AB. Do not distribute!", this.width - this.fontRenderer.getStringWidth("Copyright Mojang AB. Do not distribute!") - 2, this.height - 10, -1);
    }

    @Redirect(method = { "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawTexturedModalRect(IIIIII)V"))
    public void removeMenuLogoRendering(final GuiMainMenu guiMainMenu, final int x, final int y, final int textureX, final int textureY, final int width, final int height) {
    }

    @Redirect(method = { "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawModalRectWithCustomSizedTexture(IIFFIIFF)V"))
    public void removeSubLogoRendering(final int x, final int y, final float a, final float b, final int c, final int d, final float e, final float f) {
    }

    @Redirect(method = { "Lnet/minecraft/client/gui/GuiMainMenu;drawScreen(IIF)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawString(Lnet/minecraft/client/gui/FontRenderer;Ljava/lang/String;III)V"))
    public void removeAllDrawStrings(final GuiMainMenu guiMainMenu, final FontRenderer fontRenderer1, final String string, final int i1, final int i2, final int i3) {
    }
    @Inject( method = "initGui", at = @At( "RETURN" ) )
    public void initGui( CallbackInfo info )
    {
        Firework.shaders.init( );
    }

    @Inject( method = "drawScreen", at = @At( "HEAD" ) )
    public void drawScreen( int mouseX, int mouseY, float partialTicks, CallbackInfo info )
    {
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
    @Inject( method = "renderSkybox", at = @At( "HEAD" ), cancellable = true )
    public void renderSkybox( int mouseX, int mouseY, float partialTicks, CallbackInfo info )
    {
        if( Firework.shaders.currentshader != null )
            info.cancel( );
    }

    @Redirect( method = "drawScreen", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 0 ) )
    public void drawGradientRect1( GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor )
    {
        if( Firework.shaders.currentshader == null )
            drawGradientRect( left, top, right, bottom, startColor, endColor );
    }

    @Redirect( method = "drawScreen", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiMainMenu;drawGradientRect(IIIIII)V", ordinal = 1 ) )
    public void drawGradientRect2( GuiMainMenu guiMainMenu, int left, int top, int right, int bottom, int startColor, int endColor )
    {
        if( Firework.shaders.currentshader == null )
            drawGradientRect( left, top, right, bottom, startColor, endColor );
    }
}