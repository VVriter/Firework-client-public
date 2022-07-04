package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

@ModuleManifest(name = "Client",category = Module.Category.CLIENT)
public class Client extends Module {

    public static String lastestIpPlayerPlyed;
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
        if (mc.player.isServerWorld()) {
            lastestIpPlayerPlyed = mc.getCurrentServerData().serverIP;
            System.out.println(lastestIpPlayerPlyed);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        onEnable();
    }

}
