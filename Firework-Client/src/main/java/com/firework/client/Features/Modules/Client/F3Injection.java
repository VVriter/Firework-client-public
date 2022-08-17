package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Arrays;

@ModuleManifest(
        name = "Fake F3",
        category = Module.Category.CLIENT,
        description = "Module to prevent cord leak"
)
public class F3Injection extends Module {

    public static Setting<Boolean> Coords = null;
    public static Setting<Boolean> FPS  = null;
    public static Setting<String> fpsmode = null;
    public static Setting<Boolean> Direction  = null;
    public static Setting<Boolean> Biome  = null;
    public static Setting<Boolean> enabled = null;

    public F3Injection() {
        Coords = new Setting<>("Cords", true, this);
        FPS = new Setting<>("FPS", true, this);
        fpsmode = new Setting<>("Mode", "Hide", this, Arrays.asList("Hide", "Fake")).setVisibility(()-> FPS.getValue(true));
        Direction = new Setting<>("Direction", true, this);
        Biome = new Setting<>("Biome ", true, this);
        enabled = this.isEnabled;
    }
}
