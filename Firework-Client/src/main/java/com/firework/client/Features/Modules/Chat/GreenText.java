package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Chat.ChatSendE;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "GreenText",
        category = Module.Category.CHAT,
        description = "Sends in chat green messages"
)
public class GreenText extends Module {
    public static Setting<Boolean> enabled = null;
    public GreenText() {
        enabled = this.isEnabled;
    }
    String GREATERTHAN = ">";

    @Subscribe(priority = Listener.Priority.LOWEST)
    public Listener<ChatSendE> listener = new Listener<>(event-> {
        if (event.getMessage().startsWith("/") || event.getMessage().startsWith(".")
                || event.getMessage().startsWith(",") || event.getMessage().startsWith("-")
                || event.getMessage().startsWith("$") || event.getMessage().startsWith("*")) return;
        if (!ChatSufix.enabled.getValue()) {
            event.setCancelled(true);
            mc.player.sendChatMessage(GREATERTHAN+event.getMessage());
        }
    });
}
