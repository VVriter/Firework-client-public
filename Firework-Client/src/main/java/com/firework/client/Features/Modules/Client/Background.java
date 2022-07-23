package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@ModuleManifest(name = "Background",category = Module.Category.CLIENT)
public class Background extends Module {

    public Setting<HSLColor> color1 = new Setting<>("DownColor", new HSLColor(1, 54, 43), this);

   @SubscribeEvent
    public void onRender(TickEvent.RenderTickEvent e) {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mc.currentScreen instanceof GuiScreen) {
            RenderUtils2D.drawGradientRectVertical(
                    new Rectangle(0, 0, sr.getScaledWidth(),
                            sr.getScaledHeight()),
                    color1.getValue().toRGB(), new Color(1,1,1,0));
        }
    }
}
