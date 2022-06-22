package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;

import java.util.Arrays;

@ModuleManifest(name = "AntiVoid",category = Module.Category.MOVEMENT)
public class AntiVoid extends Module {


    public Setting<String> mode  = new Setting<>("Mode", "Jump", this, Arrays.asList("Jump","PacketFly"));


    @Override
    public void onTick() {
        super.onTick();
        if(mode.getValue().equals("Jump")){
        if (mc.player.posY <= 0.5) {
            mc.player.moveVertical = 10.0f;
            mc.player.jump();
        }
        else {
           mc.player.moveVertical = 0.0f;
            }
        }
    }

    public void onDisable() {
        mc.player.moveVertical = 0.0f;
    }
}
