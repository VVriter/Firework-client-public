package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.client.Minecraft;

import java.util.Arrays;

public class Sprint extends Module {
    public Setting<Boolean> testSetting = new Setting<>("Move", false, this);
    public Setting<Boolean> testSetting1 = new Setting<>("Colided", false, this);

    public Minecraft mc = Minecraft.getMinecraft();
    public boolean e = true;

    public Sprint() {
        super("Sprint", Category.MOVEMENT);
        isEnabled.setValue(false);
        System.out.println("Module status:" + isEnabled.getValue());
    }

    @Override
    public void onEnable() {
        super.onEnable();
    }

    @Override
    public void tryToExecute() {
        super.tryToExecute();

        if(testSetting.getValue() == false){
            if (mc.player.moveForward > 0) {
                mc.player.setSprinting(true);
            }
        }
        if(testSetting1.getValue()){
            if (!mc.player.collidedHorizontally) {
                mc.player.setSprinting(true);
            }
        }


       mc.player.setSprinting(true);
    }
}
