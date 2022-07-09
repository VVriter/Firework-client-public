package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.UI.ConsoleGui.ConsoleGui;
import com.firework.client.Implementations.UI.GuiNEO.Gui;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "Console",category = Module.Category.CLIENT)
public class Console extends Module {


    public Console() {
        this.isEnabled.setValue(true);
        this.key.setValue(Keyboard.KEY_COMMA);
    }

    @SubscribeEvent
    public void onPressedKey(InputEvent.KeyInputEvent event) {
        if (mc.currentScreen instanceof ConsoleGui) {

        if (Keyboard.getEventCharacter() != Keyboard.KEY_BACKSLASH && Keyboard.getEventCharacter() != Keyboard.KEY_RETURN) {
            ConsoleGui.text = ConsoleGui.text + (String.valueOf(Keyboard.getEventCharacter()));
            System.out.println(ConsoleGui.text);
            }

        }
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
