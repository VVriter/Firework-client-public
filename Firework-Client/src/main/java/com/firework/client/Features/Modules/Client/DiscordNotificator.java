package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

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


    @SubscribeEvent
    public void queueNotify(ClientChatReceivedEvent e) {
        if (notify2b2t.getValue()) {
            if (e.getMessage().getUnformattedText().contains("position")) {
                DiscordUtil.sendMsg("@here```" + e.getMessage() + "```", webhook);
            }
        }
    }
    @SubscribeEvent
    public void onUrina(ClientChatReceivedEvent e){
        String msg = e.getMessage().getUnformattedText();
        if(chatListner.getValue()){
            DiscordUtil.sendMsg("```"+msg+"```",webhook);
        }
    }
    @SubscribeEvent
    public void onBebra(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
        if(serverConnection.getValue()){
            DiscordUtil.sendMsg("```U are disconnected from server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",webhook);
        }
    }

    @SubscribeEvent
    public void onBebra1(FMLNetworkEvent.ClientConnectedToServerEvent e){
        if(serverConnection.getValue()){
            DiscordUtil.sendMsg("```U are connected to server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",webhook);
        }
    }



    public void onDisable(){
        super.onDisable();
        DiscordUtil.sendMsg("```Discord Notificator is toggeled off```",webhook);
      }
    }
