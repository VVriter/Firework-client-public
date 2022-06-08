package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

public class GuiGradient extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<HSLColor> Color1 = null;
    public static Setting<HSLColor> Color2 = null;
    public GuiGradient(){super("GuiGradient",Category.CLIENT);
        enabled = this.isEnabled;
        this.isEnabled.setValue(false);
        Color1 = new Setting<>("UpColor", new HSLColor(1, 54, 43), this);
        Color2 = new Setting<>("DownColor", new HSLColor(1, 54, 43), this);
    }
}
