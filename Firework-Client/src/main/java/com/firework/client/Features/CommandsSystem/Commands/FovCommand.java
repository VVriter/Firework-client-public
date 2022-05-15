package com.firework.client.Features.CommandsSystem.Commands;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.event.ClickEvent;


@CommandManifest(label = "fov",aliases = "customfov")
public class FovCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();

        try{
        mc.gameSettings.fovSetting = Float.parseFloat(args[1]);
        MessageUtil.sendClientMessage("Fov is setted to: "+args[1],-11114);}catch (NumberFormatException e){
            MessageUtil.sendError("Only Numbers LoL",-11114);
        }
    }
}


