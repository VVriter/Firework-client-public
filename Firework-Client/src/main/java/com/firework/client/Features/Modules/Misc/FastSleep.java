package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Gui.GuiOpenEvent;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketEntityAction;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;


@ModuleManifest(
        name = "FastSleep",
        category = Module.Category.MISCELLANEOUS,
        description = "Exploit to sleep faster"
)
public class FastSleep extends Module {

    @Subscribe
    public Listener<GuiOpenEvent> guiOpenEventListener = new Listener<>(event -> {
        EntityPlayerSP player = mc.player;
        if (player.isPlayerSleeping()) {
            if (player.getSleepTimer() > 10) {
                player.connection.sendPacket(new CPacketEntityAction(player, CPacketEntityAction.Action.STOP_SLEEPING));
            }
        }
    });
}
