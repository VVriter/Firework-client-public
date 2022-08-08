package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;

@ModuleManifest(
        name = "Background",
        category = Module.Category.CLIENT,
        description = "Draw cool background"
)
public class Background extends Module {
    public Setting<Boolean> blur = new Setting<>("Blur", false, this);
    public Setting<Boolean> gradientSubBool = new Setting<>("Gradient", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> enableGradient = new Setting<>("EnableGradient", false, this).setVisibility(v-> gradientSubBool.getValue());
    public Setting<HSLColor> color1 = new Setting<>("DownColor", new HSLColor(1, 54, 43), this).setVisibility(v-> gradientSubBool.getValue());
    public Setting<HSLColor> color2 = new Setting<>("UpColor", new HSLColor(80, 54, 43), this).setVisibility(v-> gradientSubBool.getValue());

    @Subscribe
    public Listener<Render2dE> listener = new Listener<>(e -> {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mc.currentScreen instanceof GuiScreen && enableGradient.getValue()) {
            RenderUtils2D.drawGradientRectVertical(
                    new Rectangle(0, 0, sr.getScaledWidth(),
                            sr.getScaledHeight()),
                    color1.getValue().toRGB(), color2.getValue().toRGB());
            }
        }
    );

    @Override
    public void onTick(){
        super.onTick();
        GameSettings.Options.FOV.setValueMax(176F);
        if (!blur.getValue()) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }

        if(blur.getValue()){
            if (mc.world != null) {
                if (!(mc.currentScreen instanceof GuiN) &&!(mc.currentScreen instanceof GuiContainer) && !(mc.currentScreen instanceof GuiChat) && !(mc.currentScreen instanceof GuiConfirmOpenLink) && !(mc.currentScreen instanceof GuiEditSign) && !(mc.currentScreen instanceof GuiGameOver) && !(mc.currentScreen instanceof GuiOptions) && !(mc.currentScreen instanceof GuiIngameMenu) && !(mc.currentScreen instanceof GuiVideoSettings) && !(mc.currentScreen instanceof GuiScreenOptionsSounds) && !(mc.currentScreen instanceof GuiControls) && !(mc.currentScreen instanceof GuiCustomizeSkin) && !(mc.currentScreen instanceof GuiModList)) {
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
    }
}
