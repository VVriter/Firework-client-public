package com.firework.client.Features.CommandsSystem.Commands.Fun;


import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Features.Modules.Client.Notifications;
import com.firework.client.Firework;
import com.firework.client.Implementations.Gui.Gui;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;


@CommandManifest(label = "cowdupe")
public class CowDupeCommand extends Command {



    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.player.inventory.getCurrentItem().getItem().equals(Items.SHEARS)) {
            for (int i = 0; i < 150; i++) {
                if (mc.pointedEntity != null)
                    mc.getConnection().sendPacket((Packet)new CPacketUseEntity(mc.pointedEntity, EnumHand.MAIN_HAND));
            }
            MessageUtil.sendClientMessage("Finished shearing targeted entity.",-1117);
        } else {
            MessageUtil.sendError("You need to hold shears to do the glitch.",-1117);
        }
    }
}