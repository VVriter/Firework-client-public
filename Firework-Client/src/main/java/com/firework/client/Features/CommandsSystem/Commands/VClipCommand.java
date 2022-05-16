package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;

@CommandManifest(label = "vclip",aliases = "clip")
public class VClipCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        if (args.length == 2) {
            try {
                if (mc.player.getRidingEntity() != null) {
                    mc.player.getRidingEntity().setPosition(mc.player.getRidingEntity().posX, mc.player.getRidingEntity().posY + Double.parseDouble(args[1]), mc.player.getRidingEntity().posZ);
                } else {
                    mc.player.setPosition(mc.player.posX, mc.player.posY + Double.parseDouble(args[1]), mc.player.posZ);
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
