package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Misc.AutoRespawn;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Managers.CommandManager.CommandManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class DiscordNotificator extends Module {

    public Setting<Boolean> deathCoords  = new Setting<>("DeathCords", true, this);


    public static String webhook = "";
    public DiscordNotificator(){super("DiscordNotificator",Category.CLIENT);}
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
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue().booleanValue() && event.getGui() instanceof GuiGameOver) {
                   DiscordUtil.sendMsg("```Your death cords is X:" + mc.player.posX+" Y:"+mc.player.posY+" Z:"+mc.player.posZ+"```",webhook);
                }
        }
    }


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
