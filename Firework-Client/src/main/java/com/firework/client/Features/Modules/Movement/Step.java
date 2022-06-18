package com.firework.client.Features.Modules.Movement;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Settings.Setting;


@ModuleArgs(name = "Step",category = Module.Category.MOVEMENT)
public class Step extends Module {

    public Setting<Enum> mode = new Setting<>("Mode", modes.Vanilla, this, modes.values());
    public Setting<Double> Y = new Setting<>("Height", (double)1, this, 1, 10);
    public Setting<Boolean> reverse = new Setting<>("Reverse", false, this);


    @Override
    public void onEnable(){
        super.onEnable();
        mc.gameSettings.autoJump = false;
    }

    @Override
    public void onTick(){
        super.onTick();
        if(mode.getValue(modes.Vanilla)){
            mc.player.stepHeight = Y.getValue().floatValue();
        }if(mode.getValue(modes.ByPass)){
            mc.player.stepHeight = 0.6f;
            //Code for bypass it cringe
            if(mc.player.collidedHorizontally){
                mc.player.motionY = 0.1;
            }
        }



        //Code for reverse step
        if(reverse.getValue()){
            if(mc.player.onGround){
                mc.player.motionY -= 1.0;
            }
        }
    }

    @Override
    public void onDisable(){
        super.onDisable();
        mc.player.stepHeight = 0.6f;
    }

    public enum modes{
        Vanilla, ByPass
    }
}
