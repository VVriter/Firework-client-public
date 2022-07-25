package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.UpdateWalkingPlayerEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.YawUtil;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.util.Arrays;


@ModuleManifest(name = "RotationLock",category = Module.Category.MISCELLANEOUS)
public class RotationLock extends Module {

    public Setting<Boolean> yawBool = new Setting<>("Yaw", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> yawEnable = new Setting<>("EnableYaw", true, this).setVisibility(v-> yawBool.getValue());
    public Setting<String> dimension = new Setting<>("Dimension", "Normal", this, Arrays.asList("Normal", "Multi")).setVisibility(v-> yawBool.getValue());
    public Setting<Double> intSpeed = new Setting<>("Speed", (double)20, this, 1, 100).setVisibility(v-> yawBool.getValue());

    public Setting<Boolean> pitchBool = new Setting<>("Pitch", false, this).setMode(Setting.Mode.SUB);
    public Setting<Boolean> pitchEnable = new Setting<>("EnablePitch", false, this).setVisibility(v-> pitchBool.getValue());
    public Setting<Integer> pitch = new Setting<>("Value", 0, this, -90, 90).setVisibility(pv-> pitchBool.getValue());


    @Subscribe
    public Listener<UpdateWalkingPlayerEvent> listener1 = new Listener<>(event -> {
        if (yawEnable.getValue()) {
        if(dimension.getValue().equals("Multi")){
        YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),true);
        }if(dimension.getValue().equals("Normal")){
            YawUtil.MakeRoundedYaw(intSpeed.getValue().intValue(),false);
            }
        }

        if (pitchEnable.getValue()) {
            mc.player.rotationPitch = pitch.getValue().floatValue();
        }
    });
}
