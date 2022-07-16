package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.YawUtil;

import java.util.Arrays;


@ModuleManifest(name = "RotationLock",category = Module.Category.MISC)
public class RotationLock extends Module {

    public Setting<Enum> page = new Setting<>("Page", pages.Yaw, this, pages.values());
    public enum pages{
       Yaw, Pitch
    }
    public Setting<Boolean> yawBool = new Setting<>("Enable", true, this).setVisibility(v-> page.getValue(pages.Yaw));
    public Setting<String> dimension = new Setting<>("Dimension", "Normal", this, Arrays.asList("Normal", "Multi")).setVisibility(v-> page.getValue(pages.Yaw));
    public Setting<Double> intSpeed = new Setting<>("Speed", (double)20, this, 1, 100).setVisibility(v-> page.getValue(pages.Yaw));

    public Setting<Boolean> pitchBool = new Setting<>("Enable", false, this).setVisibility(v-> page.getValue(pages.Pitch));
    public Setting<Integer> pitch = new Setting<>("Pitch", 0, this, -90, 90).setVisibility(pv-> page.getValue(pages.Pitch));


    @Override
    public void onTick(){
        super.onTick();
        if (yawBool.getValue()) {
        if(dimension.getValue().equals("Multi")){
        YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),true);
        }if(dimension.getValue().equals("Normal")){
            YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),false);
            }
        }

        if (pitchBool.getValue()) {
            mc.player.rotationPitch = pitch.getValue().floatValue();
        }
    }
}
