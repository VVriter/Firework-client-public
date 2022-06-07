package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@ModuleArgs(name = "FireworkShifr",category = Module.Category.CHAT)
public class FireworkShifr extends Module {
    @SubscribeEvent
    public void onChatReceive(ClientChatReceivedEvent e){
        ITextComponent msg = e.getMessage();
        if(msg.getUnformattedText().contains("fireworkencode")){
            e.setCanceled(true);
            MessageUtil.sendShifrText(msg,-1117);
        }
    }



   /* @SubscribeEvent
    public void onClientChatSend(ClientChatEvent e){
        String msg = e.getMessage();
        if(msg.contains("fireworkencode")){

        }
    }*/
}
