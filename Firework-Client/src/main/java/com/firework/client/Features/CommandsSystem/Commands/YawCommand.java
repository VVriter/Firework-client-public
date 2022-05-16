package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Firework;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.event.ClickEvent;


@CommandManifest(label = "yaw",aliases = "yw")
public class YawCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc =Minecraft.getMinecraft();
        if (args.length == 2) {
            try {
                if (mc.player != null) {
                    mc.player.rotationYaw = (float) MathHelper.wrapDegrees(Double.parseDouble(args[1]));
                }
            } catch (NumberFormatException exception) {
                MessageUtil.sendError("Please enter a valid yaw!",-1117);
            }
        } else if (args.length == 3) {
            try {
                if (mc.player != null) {
                    mc.player.rotationYaw = (float) MathHelper.wrapDegrees((Math.toDegrees(Math.atan2(mc.player.posZ - Double.parseDouble(args[2]), mc.player.posX - Double.parseDouble(args[1]))) + 90.0));
                    MessageUtil.sendClientMessage("Yaw is setted to: "+args[1],-1117);
                }
            } catch (NumberFormatException exception) {
                MessageUtil.sendError("Please enter a valid yaw!",-1117);
            }
        }
    }
}