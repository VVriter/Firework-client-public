package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.UI.ConsoleGui.ConsoleGui;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import com.firework.client.Implementations.Utill.Render.HSLColor;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "Console",category = Module.Category.CLIENT)
public class Console extends Module {

    public static Setting<HSLColor> outlineStart = null;
    public static Setting<HSLColor> outlineEnd = null;

    public static Setting<HSLColor> fillStart = null;
    public static Setting<HSLColor> fillEnd = null;
    public static Setting<Boolean> boolTest = null;
    public Console() {
        this.isEnabled.setValue(true);
        this.key.setValue(Keyboard.KEY_COMMA);
        fillStart =  new Setting<>("fillStart", new HSLColor(1, 54, 43), this);
        fillEnd =  new Setting<>("fillEnd", new HSLColor(1, 54, 43), this);
        outlineStart =  new Setting<>("outlineStart", new HSLColor(1, 54, 43), this);
        outlineEnd =  new Setting<>("outlineEnd", new HSLColor(1, 54, 43), this);
        boolTest = new Setting<>("tS", false, this);
    }
    @Override
    public void onDisable() {
        super.onDisable();
        onEnable();
        if (!(mc.currentScreen instanceof Gui)) {
            mc.displayGuiScreen(new ConsoleGui());
        }
    }
}
