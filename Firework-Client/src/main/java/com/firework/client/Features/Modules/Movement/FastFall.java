package com.firework.client.Features.Modules.Movement;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;

@ModuleArgs(name = "FastFall",category = Module.Category.MOVEMENT)
public class FastFall extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Motion, this, modes.values());
    public enum modes{Motion,Timer}
    public Setting<Double> distanse = new Setting<>("Distance", (double)3, this, 0, 25).setVisibility(mode,modes.Motion);
    public Setting<Double> reduction = new Setting<>("Reduction", (double)3, this, 0, 25).setVisibility(mode,modes.Motion);

    @Override
    public void onTick(){
        super.onTick();
        if(mode.getValue()==modes.Motion){
        if(mc.player.fallDistance == distanse.getValue().floatValue()){
            mc.player.motionY = -20;
            }
        }
    }
}