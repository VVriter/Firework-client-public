package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.UI.Hud.HudGui;
import org.lwjgl.input.Keyboard;

@ModuleManifest(name = "Hud", category = Module.Category.CLIENT)
public class Hud extends Module{

    public Hud(){
        this.key.setValue(Keyboard.KEY_L);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        mc.displayGuiScreen(new HudGui());
        onDisable();
    }
}
