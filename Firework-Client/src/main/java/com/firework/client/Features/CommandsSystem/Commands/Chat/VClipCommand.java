package com.firework.client.Features.CommandsSystem.Commands.Chat;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

@CommandManifest(label = "vclip",aliases = "clip")
public class VClipCommand extends Command {
    @Override
    public void execute(String[] args) {
        if (args.length == 2) {
            try {
                if (Firework.minecraft.player.getRidingEntity() != null) {
                    Firework.minecraft.player.getRidingEntity().setPosition(Firework.minecraft.player.getRidingEntity().posX, Firework.minecraft.player.getRidingEntity().posY + Double.parseDouble(args[1]), Firework.minecraft.player.getRidingEntity().posZ);
                } else {
                    Firework.minecraft.player.setPosition(Firework.minecraft.player.posX, Firework.minecraft.player.posY + Double.parseDouble(args[1]), Firework.minecraft.player.posZ);
                }
                MessageUtil.sendClientMessage("Teleported you " + (Double.parseDouble(args[1]) > 0 ? "up " : "down ") + Math.abs(Double.parseDouble(args[1])) + " blocks.",-1117);
            } catch (NumberFormatException exception) {
                MessageUtil.sendError("Please enter a valid distance!",-1117);
            }
        } else {
            MessageUtil.sendError("Please enter a valid distance!",-1117);
        }

    }
}
