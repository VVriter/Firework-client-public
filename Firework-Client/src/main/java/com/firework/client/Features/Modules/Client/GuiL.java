package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import org.lwjgl.input.Keyboard;

@ModuleManifest(
        name = "GuiL",
        category = Module.Category.CLIENT,
        description = "ClickGui module lol"
)
public class GuiL extends Module {

    public GuiL(){
        this.key.setValueNoEvent(Keyboard.KEY_U);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(new com.firework.client.Implementations.UI.GuiN.GuiN());
        onDisable();
    }
}
