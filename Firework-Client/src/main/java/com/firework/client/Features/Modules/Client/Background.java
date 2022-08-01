package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Render.Render2dE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import com.firework.client.Implementations.Utill.Render.Rectangle;
import com.firework.client.Implementations.Utill.Render.RenderUtils2D;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
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

    public Setting<Boolean> gradientSubBool = new Setting<>("Gradient", false, this).setMode(Setting.Mode.SUB);
    public Setting<HSLColor> color1 = new Setting<>("DownColor", new HSLColor(1, 54, 43), this).setVisibility(v-> gradientSubBool.getValue());
    public Setting<HSLColor> color2 = new Setting<>("UpColor", new HSLColor(80, 54, 43), this).setVisibility(v-> gradientSubBool.getValue());

    @Subscribe
    public Listener<Render2dE> listener = new Listener<>(e -> {
        ScaledResolution sr = new ScaledResolution(mc);
        if (mc.currentScreen instanceof GuiScreen) {
            RenderUtils2D.drawGradientRectVertical(
                    new Rectangle(0, 0, sr.getScaledWidth(),
                            sr.getScaledHeight()),
                    color1.getValue().toRGB(), color2.getValue().toRGB());
            }
        }
    );
}
