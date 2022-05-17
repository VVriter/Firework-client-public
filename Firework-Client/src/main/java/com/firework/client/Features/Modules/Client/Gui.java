package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Gui extends Module{
    public static Setting<Double> scrollSpeed = new Setting<>("Scroll Speed", (double)3, new Gui(), 1, 10);

    public Minecraft mc = Minecraft.getMinecraft();

    public Gui() {
        super("GuiSettings", Module.Category.CLIENT);

    }
}
