package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiLog4j extends Module {
    public AntiLog4j(){super("AntiHack",Category.CHAT);}
    @SubscribeEvent
    public void onClientChat(ClientChatReceivedEvent e){
                 if(e.getMessage().getUnformattedText().contains("${")
                ||e.getMessage().getUnformattedText().contains("$<")
                ||e.getMessage().getUnformattedText().contains("$:-")
                ||e.getMessage().getUnformattedText().contains("jndi:ldap")){
            e.setCanceled(true);
            MessageUtil.warning("Log4j detected, you saved from hacking!",-1117);
        }
    }
}
