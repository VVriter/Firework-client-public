package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManager;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Chat.ChatSendE;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;


@ModuleManifest(
        name = "ChatSuffix",
        category = Module.Category.CHAT,
        description = "Prints messages in chat with your own suffix"
)
public class ChatSufix extends Module {
    public static Setting<Boolean> enabled = null;
    public ChatSufix() {
        enabled = this.isEnabled;
    }
    String append;

    @Override
    public void onEnable() {
        super.onEnable();
        append = " | FIREWORK";
    }

    @Override
    public void onDisable() {
        super.onDisable();
        append = null;
    }

    @Subscribe(priority = Listener.Priority.HIGH)
    public Listener<ChatSendE> listener = new Listener<>(e-> {
        String msg = e.getMessage();
        e.setCancelled(true);
        if(msg.startsWith("/") || msg.startsWith("!") || msg.startsWith("#") || msg.startsWith(CommandManager.prefix)){
            mc.player.sendChatMessage(msg);
        } else {
                if (!GreenText.enabled.getValue()) {
                    mc.player.sendChatMessage(msg + append);
                 } else {
                    mc.player.sendChatMessage(">"+msg + append);
                }
            }
        }
    );
}
