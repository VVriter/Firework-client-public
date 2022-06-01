package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;

public class GuiGradient extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> rainbow1  = null;
    public static Setting<HSLColor> Color1 = null;

    public static Setting<Boolean> rainbow2   = null;
    public static Setting<HSLColor> Color2 = null;


    public GuiGradient(){super("GuiGradient",Category.CLIENT);
        enabled = this.isEnabled;
        this.isEnabled.setValue(false);

        rainbow1 = new Setting<>("rainbow1", false, this);
        Color1 = new Setting<>("PlayerColor", new HSLColor(1, 54, 43), this);


        rainbow2 = new Setting<>("rainbow1", false, this);
        Color2 = new Setting<>("PlayerOutline", new HSLColor(1, 54, 43), this);


    }
}
