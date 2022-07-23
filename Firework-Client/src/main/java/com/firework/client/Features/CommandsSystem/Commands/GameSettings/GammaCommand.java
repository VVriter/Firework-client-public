package com.firework.client.Features.CommandsSystem.Commands.GameSettings;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;


@CommandManifest(label = "gamma",aliases = "gm")
public class GammaCommand extends Command {
    @Override
    public void execute(String[] args) {
        try {
            Firework.minecraft.gameSettings.gammaSetting = Float.parseFloat(args[1]);
            MessageUtil.sendClientMessage("Gamma is setted to: "+args[1],-11114);
        } catch (NumberFormatException e){
            MessageUtil.sendError("Only Numbers LoL",-11114);
        } catch (ArrayIndexOutOfBoundsException e) {
            MessageUtil.sendError("Wrong usage!",-11114);
        }
    }
}


