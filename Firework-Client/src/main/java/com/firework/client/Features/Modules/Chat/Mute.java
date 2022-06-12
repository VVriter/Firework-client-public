package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@ModuleArgs(name = "MuteList",category = Module.Category.CHAT)
public class Mute extends Module {
    @SubscribeEvent
    public void onChatClientReceive(ClientChatReceivedEvent e){
        if(e.getMessage().getUnformattedText().contains(""/*Тут типо бебры*/)){
            e.setCanceled(true);
        }
    }
}
