package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.GuiN.GuiN;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class Gui extends Module {

    public static Setting<Boolean> enabled = null;
    public static Setting<Double> scrollSpeed;

    public static Setting<modes> mode;
    public enum modes{
        Gradient, Single
    }
    public static Setting<HSLColor> Romeo;
    public static Setting<HSLColor> Juliet;

    public static Setting<HSLColor> Color;

    public Gui(){
        super("Gui",Category.CLIENT);
        this.key.setValue(Keyboard.KEY_RSHIFT) ;

        enabled = this.isEnabled;
        scrollSpeed = new Setting<>("ScrollSpeed", 50d, this, 0.1, 50);
        mode = new Setting<>("Mode", modes.Gradient, this);
        Romeo = new Setting<>("Romeo", new HSLColor(271, 72, 61), this).setVisibility(()-> mode.getValue(modes.Gradient));
        Juliet = new Setting<>("Juliet", new HSLColor(321, 72, 61), this).setVisibility(()-> mode.getValue(modes.Gradient));

        Color = new Setting<>("Color", new HSLColor(271, 72, 61), this).setVisibility(()-> mode.getValue(modes.Single));
    }


    @Override
    public void onEnable(){
        super.onEnable();
        Minecraft.getMinecraft().displayGuiScreen(new GuiN());
        this.isEnabled.setValue(false);
    }

    public enum endBlockMode{
        Static, Dynamic
    }
}
