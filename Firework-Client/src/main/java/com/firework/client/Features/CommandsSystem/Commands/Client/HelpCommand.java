package com.firework.client.Features.CommandsSystem.Commands.Client;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import net.minecraft.util.ResourceLocation;

@CommandManifest(label = "help")
public class HelpCommand extends Command {
    @Override
    public void execute(String[] args) {
        MessageUtil.sendShown("Help Command - Usage: "+ CommandManager.prefix +"help ","Sends list of commands",false);
        MessageUtil.sendShown("Fakeplayer Command - Usage: "+ CommandManager.prefix +"fakeplayer nickname ","Spawns fakeplayer",false);
        MessageUtil.sendShown("Dir Command - Usage: "+ CommandManager.prefix +"dir ","Opens firework dir",false);
        MessageUtil.sendShown("Clear Command - Usage: "+CommandManager.prefix+"clear ","Clears chat",false);
        MessageUtil.sendShown("Coords Command - Usage: "+CommandManager.prefix+"coords ","Copies ur coords to clipboard",false);
        MessageUtil.sendShown("Fov Command - Usage: "+CommandManager.prefix+"fov ","Changes your fov",false);
        MessageUtil.sendShown("NameMc Command - Usage: "+CommandManager.prefix+"namemc nickoftheplayer ","Open ncmemc site with profile",false);
        MessageUtil.sendShown("Webhook Command - Usage: "+CommandManager.prefix+"webhook urDiscordWebhook ","Adds ur webhook",false);
        MessageUtil.sendShown("Dupe Command - Usage: "+CommandManager.prefix+"dupe ","Makes an a sexdupe",false);
        MessageUtil.sendShown("Gamma Command - Usage: "+CommandManager.prefix+"gamma floatvalue ","Changes gamma setting",false);
        MessageUtil.sendShown("Yaw Command - Usage: "+CommandManager.prefix+"Yaw floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("Pitch Command - Usage: "+CommandManager.prefix+"Pitch floatvalue ","Moves ur camera",false);
        MessageUtil.sendShown("VClip Command - Usage: "+CommandManager.prefix+"Clip floatvalue ","Moves ur up",false);
        MessageUtil.sendShown("Peek Command - Usage: "+CommandManager.prefix+"peek ","U need to hold shulker in main hand",false);
        MessageUtil.sendShown("Penis Command - Usage: "+CommandManager.prefix+"penis ","Show ur penis size :)",false);
        MessageUtil.sendShown("Book Command - Usage: "+CommandManager.prefix+"prefix ","Makes an a dupe book",false);
        MessageUtil.sendShown("Prefix Command - Usage: "+CommandManager.prefix+"prefix value ","Changes ur command prefix!",false);


        //Plays Notification sound
        Notifications.notificate();
    }
}