package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Events.Chat.ChatReceiveE;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

public class DiscordNotificator extends Module {

    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> death = null;
    public static Setting<Boolean> notify2b2t = null;
    public static Setting<Boolean> serverConnection = null;
    public static Setting<Boolean> chatListner = null;


    public static String webhook = "";
    public DiscordNotificator(){super("DiscordNotificator",Category.CLIENT);

        enabled = this.isEnabled;
        notify2b2t = new Setting<>("Queue notify", true, this);
        death = new Setting<>("DeathNotificator", true, this);
        serverConnection = new Setting<>("ServerConnection", true, this);
        chatListner = new Setting<>("ChatListner", true, this);
    }
    public void onEnable(){
        super.onEnable();
        DiscordUtil.sendMsg("```Discord Notificator module already works```",webhook);
    }


    @Subscribe
    public Listener<ChatReceiveE> listener = new Listener<>(e -> {
        if (notify2b2t.getValue()) {
            if (e.getMessage().getUnformattedText().contains("position")) {
                DiscordUtil.sendMsg("@here```" + e.getMessage() + "```", webhook);
            }
        }
    });

    @Subscribe
    public Listener<ChatReceiveE> listener1 = new Listener<>(e -> {
        String msg = e.getMessage().getUnformattedText();
        if(chatListner.getValue()){
            DiscordUtil.sendMsg("```"+msg+"```",webhook);
        }
    });


    public void onDisable(){
        super.onDisable();
        DiscordUtil.sendMsg("```Discord Notificator is toggeled off```",webhook);
      }
    }
