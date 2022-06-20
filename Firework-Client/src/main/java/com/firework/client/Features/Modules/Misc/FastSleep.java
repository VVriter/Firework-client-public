package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;


@ModuleArgs(name = "FastSleep",category = Module.Category.MISC)
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
