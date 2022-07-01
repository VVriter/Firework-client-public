package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.GuiNEO.Gui;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;

@ModuleManifest(name = "Blur",category = Module.Category.CLIENT)
public class Blur extends Module {
    @Override
    public void onUpdate() {
        if (mc.world != null) {
            if (!(mc.currentScreen instanceof Gui) &&!(mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiConfirmOpenLink) && !(mc.currentScreen instanceof GuiEditSign) && !(mc.currentScreen instanceof GuiGameOver) && !(mc.currentScreen instanceof GuiOptions) && !(mc.currentScreen instanceof GuiIngameMenu) && !(mc.currentScreen instanceof GuiVideoSettings) && !(mc.currentScreen instanceof GuiScreenOptionsSounds) && !(mc.currentScreen instanceof GuiControls) && !(mc.currentScreen instanceof GuiCustomizeSkin) && !(mc.currentScreen instanceof GuiModList)) {
                if (mc.entityRenderer.getShaderGroup() != null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
            }
            else if (OpenGlHelper.shadersSupported && mc.getRenderViewEntity() instanceof EntityPlayer) {
                if (mc.entityRenderer.getShaderGroup() != null) {
                    mc.entityRenderer.getShaderGroup().deleteShaderGroup();
                }
                try {
                    mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blur.json"));
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            else if (mc.entityRenderer.getShaderGroup() != null && mc.currentScreen == null) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            }
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.entityRenderer.getShaderGroup().deleteShaderGroup();
    }
}
