package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

import java.util.Arrays;

@ModuleManifest(name = "Sprint",category = Module.Category.MOVEMENT)
public class Sprint extends Module {
    public Setting<String> mode = new Setting<>("Mode", "Legit", this, Arrays.asList("Legit", "Multi"));
    @Override
    public void onTick(){
        super.onTick();
        if (mode.getValue().equals("Legit")) {
            if (mc.player.moveForward > 0 && !mc.player.collidedHorizontally) {
                mc.player.setSprinting(true);
            }
        } else if (mode.getValue().equals("Multi")) {
            if (Sprint.mc.player == null) {
                return;
            }
            if ((Sprint.mc.player.moveForward != 0.0f || Sprint.mc.player.moveStrafing != 0.0f)) {
                Sprint.mc.player.setSprinting(true);
            }
            else {
                Sprint.mc.player.setSprinting(Sprint.mc.player.moveForward > 0.0f || Sprint.mc.player.moveStrafing > 0.0f);
            }
        }
    }
}
