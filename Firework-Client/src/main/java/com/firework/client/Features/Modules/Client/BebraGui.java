package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

public class BebraGui extends Module {

    public static Setting<Double> scrollSpeed = null;
    public static Setting<Boolean> background = null;

    public BebraGui(){super("GuiModule",Category.CLIENT);
    this.key.setValue(Keyboard.KEY_RSHIFT) ;
    scrollSpeed = new Setting<>("ScrollSpeed", 3d, this, 0.1, 10);
    background = new Setting<>("Background", false, this);}


    @Override
    public void onEnable(){
        super.onEnable();
        Notifications.notificate();
        Minecraft.getMinecraft().player.closeScreen();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            Minecraft.getMinecraft().displayGuiScreen(new Gui());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
        this.isEnabled.setValue(false);
    }
}
