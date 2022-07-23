package com.firework.client.Features.CommandsSystem.Commands.GameSettings;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;


@CommandManifest(label = "fov",aliases = "customfov")
public class FovCommand extends Command {
    @Override
    public void execute(String[] args) {
        try {
        Firework.minecraft.gameSettings.fovSetting = Float.parseFloat(args[1]);
        MessageUtil.sendClientMessage("Fov is setted to: "+args[1],-11114);
        } catch (NumberFormatException e) {
            MessageUtil.sendError("Only numbers LoL",-11114);
        } catch (ArrayIndexOutOfBoundsException e) {
            MessageUtil.sendError("Enter a number!",-11114);
        }
    }
}


