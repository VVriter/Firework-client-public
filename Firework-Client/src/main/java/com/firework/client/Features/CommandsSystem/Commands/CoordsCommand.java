package com.firework.client.Features.CommandsSystem.Commands;

import com.firework.client.Features.CommandsSystem.Command;
import com.firework.client.Features.CommandsSystem.CommandManifest;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.text.DecimalFormat;

@CommandManifest(label = "coords",aliases = "cord")
public class CoordsCommand extends Command {
    @Override
    public void execute(String[] args) {
        Minecraft mc = Minecraft.getMinecraft();
        final DecimalFormat format = new DecimalFormat ( "#" );
        final StringSelection contents = new StringSelection ( format.format ( mc.player.posX ) + ", " + format.format ( mc.player.posY ) + ", " + format.format ( mc.player.posZ ) );
        final Clipboard clipboard = Toolkit.getDefaultToolkit ( ).getSystemClipboard ( );
        clipboard.setContents ( contents , null );
        MessageUtil.sendClientMessage("Ur coords copied to clipboard: "+ mc.player.posX+", "+ mc.player.posY+", "+mc.player.posZ,-11114);
    }
}
