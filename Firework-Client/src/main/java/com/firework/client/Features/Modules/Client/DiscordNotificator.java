package com.firework.client.Features.Modules.Client;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;

public class DiscordNotificator extends Module {
    public static Setting<Boolean> enabled = null;
    public static Setting<Boolean> notify2b2t = null;


    public static String webhook = "";
    public DiscordNotificator(){super("DiscordNotificator",Category.CLIENT);

        enabled = this.isEnabled;
        notify2b2t = new Setting<>("Queue notify", true, this);}



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
