package com.firework.client.Features.Modules.Combat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraft.util.EnumHand;

import java.util.Arrays;

public class AutoCrystal extends Module {

    public Setting<String> swing = new Setting<>("Swing", "Right", this, Arrays.asList("Right", "Left","None"));

    public AutoCrystal(){super("AutoCrystal",Category.COMBAT);}

    @Override
    public void onTick() {
        super.onTick();











        //Swing---------------------------------------------------------------------------------------------------------------------
        if(swing.getValue().equals("Right")){
            mc.player.swingingHand = EnumHand.MAIN_HAND;
        }else if(swing.getValue().equals("Left")){
            mc.player.swingingHand = EnumHand.OFF_HAND;
        }else if(swing.getValue().equals("None")){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }

    }
}
