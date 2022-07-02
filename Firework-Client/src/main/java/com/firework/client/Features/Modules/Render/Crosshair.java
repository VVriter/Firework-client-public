package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.geom.Point2D;

@ModuleManifest(name = "Crosshair",category = Module.Category.RENDER)
public class Crosshair extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Gradient, this, modes.values());
    public enum modes{
        Gradient, Static
    }

    public Setting<Double> size = new Setting<>("Size", (double)30, this, -0.3, 300).setVisibility(mode,modes.Gradient);
    public Setting<Double> lineWidth = new Setting<>("LineWidth", (double)5, this, 1, 10);
    public Setting<HSLColor> startColor = new Setting<>("StartColor", new HSLColor(120, 54, 43), this).setVisibility(mode,modes.Gradient);
    public Setting<HSLColor> endColor = new Setting<>("EndColor", new HSLColor(1, 54, 43), this).setVisibility(mode,modes.Gradient);

    @SubscribeEvent
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre e) {
        if(e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
           e.setCanceled(true);
           if (mode.getValue(modes.Gradient)) {
            ScaledResolution sr = new ScaledResolution(mc);
            Point2D.Double aDouble = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
            Point2D.Double bDouble = new Point2D.Double(sr.getScaledWidth()/2+size.getValue(),sr.getScaledHeight()/2);

            Point2D.Double bebro1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
            Point2D.Double bebro2 = new Point2D.Double(sr.getScaledWidth()/2-size.getValue(),sr.getScaledHeight()/2);

            Point2D.Double bebrio1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
            Point2D.Double bebrio2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+size.getValue());

            Point2D.Double pun1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
            Point2D.Double pun2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-size.getValue());

            RenderUtils2D.drawGradientLine(bebro1, bebro2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
            RenderUtils2D.drawGradientLine(bebrio1, bebrio2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
            RenderUtils2D.drawGradientLine(pun1, pun2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
            RenderUtils2D.drawGradientLine(aDouble, bDouble, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
            }
        }
    }
}
