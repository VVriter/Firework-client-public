package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;

public class ClickGui extends Module{
    private static ClickGui INSTANCE;
    public static Setting<Double> scrollSpeed = new Setting<>("Scroll Speed", (double)3, new ClickGui(), 1, 10);
    public Setting<Boolean> background = new Setting<>("Background", false, this);

    public Minecraft mc = Minecraft.getMinecraft();

    public ClickGui() {
        super("GuiSettings", Module.Category.CLIENT);
        INSTANCE = this;
    }

    public static ClickGui getInstance() {
        return INSTANCE;
    }
    public void onEnable(){
        super.onEnable();

    }
}
