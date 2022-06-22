package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleManifest(name = "ChatSufix",category = Module.Category.CHAT)
public class ChatSufix extends Module {

    String append = " | FIREWORK";

    @SubscribeEvent
    public void onPacket(ClientChatEvent e){
        String msg = e.getMessage();
        e.setCanceled(true);
        if(msg.startsWith(">") || msg.startsWith("/") || msg.startsWith("!") || msg.startsWith("#") || msg.startsWith(CommandManager.prefix)){
            mc.player.sendChatMessage(msg);
        }else {
            mc.player.sendChatMessage(msg + append);
        }
    }
}
