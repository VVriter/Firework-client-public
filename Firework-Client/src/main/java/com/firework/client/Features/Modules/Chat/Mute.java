package com.firework.client.Features.Modules.Chat;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Managers.MuteManager;
import com.firework.client.Implementations.Settings.Setting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.regex.Pattern;

@ModuleManifest(name = "Mute",category = Module.Category.CHAT)
public class Mute extends Module {
    String string;
    private static ArrayList<String> muted = new ArrayList<>();


    public static Setting<Boolean> enabled = null;
    private static final Pattern PATTERN_CHAT = Pattern.compile("^<([a-zA-Z0-9_]{3,16})> (.+)$");

    public Mute(){
        enabled = this.isEnabled;
        onEnable();
    }

    @SubscribeEvent
    public void onChatClientReceive(ClientChatReceivedEvent event){
        String msg = event.getMessage().getUnformattedText();
        msg = msg.replace("<", ""); String splitted[] = msg.split(">");
        System.out.println(splitted[0]);
        if(MuteManager.isMuted(splitted[0])){
            event.setCanceled(true);
        }
    }
}
