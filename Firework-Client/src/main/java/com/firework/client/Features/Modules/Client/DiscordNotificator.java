package com.firework.client.Features.Modules.Client;
import com.firework.client.Features.Modules.Misc.AutoRespawn;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

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
        chatListner = new Setting<>("ChatListner", true, this);}



    public void onEnable(){
        super.onEnable();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordUtil.sendMsg("```Discord Notificator module already works```",webhook);
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
              }
                }).start();
    }


    @SubscribeEvent
    public void queueNotify(ClientChatReceivedEvent e){
     if(notify2b2t.getValue()){
        if(e.getMessage().getUnformattedText().contains("position")){
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordUtil.sendMsg("@here```"+e.getMessage()+"```",webhook);
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();}
             }
         }



    //ChatListner
    @SubscribeEvent
    public void onUrina(ClientChatReceivedEvent e){
        String msg = e.getMessage().getUnformattedText();
        if(chatListner.getValue()){
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordUtil.sendMsg("```"+msg+"```",webhook);
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
                    }
                }).start();}
    }
    //ChatListner


    //Server Connection
    @SubscribeEvent
    public void onBebra(FMLNetworkEvent.ClientDisconnectionFromServerEvent e){
        if(serverConnection.getValue()){
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordUtil.sendMsg("```U are disconnected from server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",webhook);
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
                    }
                }).start();}
    }

    @SubscribeEvent
    public void onBebra1(FMLNetworkEvent.ClientConnectedToServerEvent e){
        if(serverConnection.getValue()){
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordUtil.sendMsg("```U are connected to server "+ Minecraft.getMinecraft().getCurrentServerData().serverIP+"```",webhook);
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
                    }
                }).start();}
    }
    //ServerConnection



    public void onDisable(){
        super.onDisable();
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordUtil.sendMsg("```Discord Notificator is toggeled off```",webhook);
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
                    }
                }).start();
    }
}