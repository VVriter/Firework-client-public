package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.multiplayer.ServerData;

@ModuleManifest(name = "Client",category = Module.Category.CLIENT)
public class Client extends Module {

    public static ServerData lastData;
    public static Setting<Boolean> enabled = null;

    public static Setting<Boolean> loadedSound = null;

    public Client() {
        enabled = this.isEnabled;
        loadedSound = new Setting<>("LoadingSound", true, this);
        enabled.setValue(true);
    }
    
    @Override
    public void onTick() {
        super.onTick();
        updateLastConnectedServer();
    }

    public void updateLastConnectedServer() {
        ServerData data = mc.getCurrentServerData();
        if (data != null) {
            lastData = data;
        }
    }
    @Override
    public void onDisable() {
        super.onDisable();
        onEnable();
    }

}
