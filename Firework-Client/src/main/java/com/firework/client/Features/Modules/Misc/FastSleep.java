package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;


@ModuleManifest(name = "FastSleep",category = Module.Category.MISC)
public class FastSleep extends Module {


    @Override
    public void onTick(){
        super.onTick();
        EntityPlayerSP player = mc.player;
        if (player.isPlayerSleeping()) {
            if (player.getSleepTimer() > 10) {
                player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SLEEPING));
            }
        }

    }

}
