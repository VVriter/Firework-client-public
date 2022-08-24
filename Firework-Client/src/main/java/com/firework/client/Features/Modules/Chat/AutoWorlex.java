package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Chat.ChatSendE;
import org.codehaus.plexus.util.StringUtils;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "AutoWorlex",
        category = Module.Category.CHAT
)
public class AutoWorlex extends Module {

    @Subscribe
    public Listener<ChatSendE> evv = new Listener<>(e-> {
        String text;
        text = e.getMessage();
        e.setCancelled(true);
        String textToSend = StringUtils.capitalise(text+",");
        mc.player.sendChatMessage(textToSend);
    });

}