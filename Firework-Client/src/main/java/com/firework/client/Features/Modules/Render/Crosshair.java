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


    public Setting<Enum> colorMode = new Setting<>("Color", colorModes.Gradient, this);
    public enum colorModes {
        Gradient, Static
    }

    public Setting<Double> size = new Setting<>("Size", (double)30, this, -0.3, 300);
    public Setting<Double> lineWidth = new Setting<>("LineWidth", (double)5, this, 1, 10);
    public Setting<HSLColor> startColor = new Setting<>("StartColor", new HSLColor(120, 54, 43), this).setVisibility(v-> colorMode.getValue(colorModes.Gradient));
    public Setting<HSLColor> endColor = new Setting<>("EndColor", new HSLColor(1, 54, 43), this).setVisibility(v-> colorMode.getValue(colorModes.Gradient));

    public Setting<HSLColor> staticColor = new Setting<>("Color", new HSLColor(1, 54, 43), this).setVisibility(v-> colorMode.getValue(colorModes.Static));

    public Setting<Boolean> dot = new Setting<>("Dot", false, this);
    public Setting<Double> dotSize = new Setting<>("DotSize", (double)3, this, -0.3, 100).setVisibility(v-> dot.getValue(true));

    @SubscribeEvent
    public void onRenderCrosshair(RenderGameOverlayEvent.Pre e) {
        if(e.getType() == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
           e.setCanceled(true);
           
           if (colorMode.getValue(colorModes.Gradient) && !dot.getValue()) {
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

            } else if (colorMode.getValue(colorModes.Gradient) && dot.getValue()) {
               ScaledResolution sr = new ScaledResolution(mc);
               Point2D.Double aDouble = new Point2D.Double(sr.getScaledWidth()/2+dotSize.getValue(),sr.getScaledHeight()/2);
               Point2D.Double bDouble = new Point2D.Double(sr.getScaledWidth()/2+size.getValue()+dotSize.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebro1 = new Point2D.Double(sr.getScaledWidth()/2-dotSize.getValue(),sr.getScaledHeight()/2);
               Point2D.Double bebro2 = new Point2D.Double(sr.getScaledWidth()/2-size.getValue()-dotSize.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebrio1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+dotSize.getValue());
               Point2D.Double bebrio2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+size.getValue()+dotSize.getValue());

               Point2D.Double pun1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-dotSize.getValue());
               Point2D.Double pun2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-size.getValue()-dotSize.getValue());

               RenderUtils2D.drawGradientLine(bebro1, bebro2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(bebrio1, bebrio2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(pun1, pun2, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(aDouble, bDouble, startColor.getValue().toRGB(),endColor.getValue().toRGB(),lineWidth.getValue().intValue());

           } else if (colorMode.getValue(colorModes.Static) && !dot.getValue()) {
               ScaledResolution sr = new ScaledResolution(mc);
               Point2D.Double aDouble = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
               Point2D.Double bDouble = new Point2D.Double(sr.getScaledWidth()/2+size.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebro1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
               Point2D.Double bebro2 = new Point2D.Double(sr.getScaledWidth()/2-size.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebrio1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
               Point2D.Double bebrio2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+size.getValue());

               Point2D.Double pun1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2);
               Point2D.Double pun2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-size.getValue());

               RenderUtils2D.drawGradientLine(bebro1, bebro2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(bebrio1, bebrio2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(pun1, pun2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(aDouble, bDouble, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());

           } else if (colorMode.getValue(colorModes.Static) && dot.getValue()) {
               ScaledResolution sr = new ScaledResolution(mc);
               Point2D.Double aDouble = new Point2D.Double(sr.getScaledWidth()/2+dotSize.getValue(),sr.getScaledHeight()/2);
               Point2D.Double bDouble = new Point2D.Double(sr.getScaledWidth()/2+size.getValue()+dotSize.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebro1 = new Point2D.Double(sr.getScaledWidth()/2-dotSize.getValue(),sr.getScaledHeight()/2);
               Point2D.Double bebro2 = new Point2D.Double(sr.getScaledWidth()/2-size.getValue()-dotSize.getValue(),sr.getScaledHeight()/2);

               Point2D.Double bebrio1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+dotSize.getValue());
               Point2D.Double bebrio2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2+size.getValue()+dotSize.getValue());

               Point2D.Double pun1 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-dotSize.getValue());
               Point2D.Double pun2 = new Point2D.Double(sr.getScaledWidth()/2,sr.getScaledHeight()/2-size.getValue()-dotSize.getValue());

               RenderUtils2D.drawGradientLine(bebro1, bebro2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(bebrio1, bebrio2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(pun1, pun2, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());
               RenderUtils2D.drawGradientLine(aDouble, bDouble, staticColor.getValue().toRGB(),staticColor.getValue().toRGB(),lineWidth.getValue().intValue());

           }
        }
    }
}
