package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Gui extends Module {

    public static Setting<Boolean> enabled = null;
    public static Setting<Double> scrollSpeed;
    public static Setting<Boolean> background;
    public static Setting<HSLColor> upStartBlockColor;
    public static Setting<HSLColor> downStartBlockColor;

    public Gui(){super("GuiModule",Category.CLIENT);
    this.key.setValue(Keyboard.KEY_RSHIFT) ;
    enabled = this.isEnabled;
    scrollSpeed = new Setting<>("ScrollSpeed", 3d, this, 0.1, 10);
    background = new Setting<>("Background", false, this);
    upStartBlockColor = new Setting<>("upStartBlockColor", new HSLColor(175, 50, 50), this);
    downStartBlockColor = new Setting<>("downStartBlockColor", new HSLColor(334, 74, 77), this);
    }


    @Override
    public void onEnable(){
        super.onEnable();
        Notifications.notificate();
        Minecraft.getMinecraft().displayGuiScreen(new com.firework.client.Implementations.GuiNEO.Gui());
        this.isEnabled.setValue(false);
    }
}
