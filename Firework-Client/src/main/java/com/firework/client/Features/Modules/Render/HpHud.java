package com.firework.client.Features.Modules.Render;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleManifest(name = "HpHud",category = Module.Category.RENDER)
public class HpHud extends Module {
    @SubscribeEvent
    public void onGameOverlay(RenderGameOverlayEvent e) {
        if (mc.player != null && mc.world != null) {
            if (mc.player.getHealth() <= 10) {
                MessageUtil.sendClientMessage("Okay",-1117);
            }
        }
    }
}
