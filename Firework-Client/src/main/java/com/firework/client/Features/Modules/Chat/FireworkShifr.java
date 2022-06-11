package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.CryptUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleArgs(name = "FireworkShifr",category = Module.Category.CHAT)
public class FireworkShifr extends Module {

    @SubscribeEvent
    public void onClientChat(ClientChatEvent event){
        event.setMessage("IWFyc2llbW9k" + CryptUtil.encrypt(event.getOriginalMessage(), "firework"));
    }

    @SubscribeEvent
    public void onServerChat(ServerChatEvent event){
        if(event.getMessage().startsWith("IWFyc2llbW9k")){
            String toDecrypt = event.getMessage().replace("IWFyc2llbW9k", "");
            MessageUtil.sendClientMessage((String.format("[%s] %s", event.getUsername(), CryptUtil.decrypt(toDecrypt, "arsiemod"))), true);
            event.setCanceled(true);
        }
    }
}
