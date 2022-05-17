package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Firework;
import com.firework.client.Implementations.Settings.Setting;

public class Sprint extends Module {
    public Setting<Boolean> moveSetting = new Setting<>("Move", false, this);
    public Setting<Boolean> collidedSetting = new Setting<>("Collided", false, this);

    public boolean e = true;

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        System.out.println("Module status:" + isEnabled.getValue());
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();

        if(moveSetting.getValue()){
            if (Firework.minecraft.player.moveForward > 0) {
                Firework.minecraft.player.setSprinting(true);
            }
        }
        if(collidedSetting.getValue()){
            if (!Firework.minecraft.player.collidedHorizontally) {
                Firework.minecraft.player.setSprinting(true);
            }
        }
    }
}
