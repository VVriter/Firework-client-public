package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.AltManagerRewrite.Guis.AltManagerGui;
import com.firework.client.Features.IngameGuis.GuiDisconnected.DisconnectedGui;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.UI.Hud.HudGui;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.geom.Point2D;

@ModuleManifest(name = "Hud", category = Module.Category.CLIENT)
public class Hud extends Module{

    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> blur = null;
    public Hud(){
        this.key.setValue(Keyboard.KEY_L);
        enabled = this.isEnabled;
        enabled.setValue(true);
        blur = new Setting<>("Blur", false, this);
    }

    @Override
    public void onTick(){
        super.onTick();
        GameSettings.Options.FOV.setValueMax(176F);
        if (!blur.getValue()) {
            mc.entityRenderer.getShaderGroup().deleteShaderGroup();
        }
    }

    @Override
    public void onUpdate() {
        if(blur.getValue()){
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
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent e) {
        if (mc.currentScreen instanceof AltManagerGui) {
            ScaledResolution sr = new ScaledResolution(this.mc);

            //Center
            Point2D.Double screenCenter = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);

            Point2D.Double leftLine1stpoint = new Point2D.Double(screenCenter.getX()-100,200);
            Point2D.Double leftLine2ndpoint = new Point2D.Double(screenCenter.getX()-100, 100);

            RenderUtils2D.drawGradientLine(leftLine1stpoint,leftLine2ndpoint,new Color(140, 40, 175),new Color(78, 129, 218),5);
        }
    }
    @Override
    public void onEnable() {
        super.onEnable();
        if (!(mc.currentScreen instanceof Gui)) {
            mc.displayGuiScreen(new HudGui());
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onEnable();
    }
}
