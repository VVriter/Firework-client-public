package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;

@ModuleArgs(name = "NoFall",category = Module.Category.MOVEMENT)
public class NoFall extends Module {
    public Setting<Enum> mode = new Setting<>("Mode", modes.Anti, this, modes.values());   public enum modes{Anti, Packet}
    public Setting<Double> fall = new Setting<>("Distanse", (double)3, this, 1, 10);

    @Override
    public void onEnable(){super.onEnable();
        if(mode.getValue()==modes.Anti){
    if(!mc.player.onGround){
        mc.player.jump();
            }
        }
    }

    @Override
    public void onTick(){
        super.onTick();
        if(mode.getValue()==modes.Anti){
        if(mc.player.fallDistance == fall.getValue().floatValue()){
            mc.player.jump();
            }
        }
    }

}
