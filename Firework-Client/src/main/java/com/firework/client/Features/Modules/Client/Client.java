package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

@ModuleManifest(name = "Client",category = Module.Category.CLIENT)
public class Client extends Module {

    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> loadedSound = null;

    public Client() {
        enabled = this.isEnabled;
        loadedSound = new Setting<>("LoadingSound", true, this);
        enabled.setValue(true);
    }
}
