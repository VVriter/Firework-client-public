package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.Chat.ChatReceiveE;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

@ModuleManifest(
        name = "AutoAuth",
        category = Module.Category.CHAT,
        description = "Use .auth <pass> to bind auto auth password"
)
public class AutoAuth extends Module {

    public static String authCode = "";

    @Subscribe
    public Listener<ChatReceiveE> lis = new Listener<>(e-> {
        if (e.getMessage().getFormattedText().contains("/reg") || e.getMessage().getFormattedText().contains("/register") || e.getMessage().getFormattedText().contains("/Зарегистрируйтесь")) {
            if (authCode != "") {
                mc.player.sendChatMessage("/reg "+authCode+" "+authCode);
            }
        }

        if (e.getMessage().getFormattedText().contains("Авторизуйтесь") || e.getMessage().getFormattedText().contains("/l") || e.getMessage().getFormattedText().contains("/login")) {
            if (authCode != "") {
                mc.player.sendChatMessage("/l "+authCode);
            }
        }
    });
}
