package com.firework.client.Features.Modules.Movement;


import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

public class AirJump extends Module {
    public int delay = 1000;

    public static Setting<Boolean> enabled = null;
    public AirJump() {
        super("AirJump", Category.MOVEMENT);
        enabled = this.isEnabled;
    }

    @Override
    public void onTick() {
        super.onTick();
        if(MovementHelper.parkour.getValue() && MovementHelper.enabled.getValue()){
            MessageUtil.sendError("U are dumb dont use air jump with parkour helper",-1117);}else {mc.player.onGround = true;}
    }
}