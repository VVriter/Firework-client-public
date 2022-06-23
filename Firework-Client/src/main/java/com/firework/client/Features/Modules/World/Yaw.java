package com.firework.client.Features.Modules.World;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.YawUtil;

import java.util.Arrays;


@ModuleManifest(name = "Yaw",category = Module.Category.WORLD)
public class Yaw extends Module {

    public Setting<String> dimension = new Setting<>("Dimension", "Normal", this, Arrays.asList("Normal", "Multi"));
    public Setting<Double> intSpeed = new Setting<>("Speed", (double)20, this, 1, 100);

    @Override
    public void onTick(){
        super.onTick();
        if(dimension.getValue().equals("Multi")){
        YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),true);
        }if(dimension.getValue().equals("Normal")){
            YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),false);
        }
    }
}
