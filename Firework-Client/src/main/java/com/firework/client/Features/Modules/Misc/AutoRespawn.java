package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Managers.CommandManager.CommandManager;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class AutoRespawn extends Module {


    private static Minecraft mc = Minecraft.getMinecraft();
    public static Setting<Boolean> enabled = null;
    public Setting<Boolean> respawn  = new Setting<>("Respawn", true, this);
    public Setting<Boolean> antiDeathScreen  = new Setting<>("AntiDeathScreen", true, this);
    public Setting<Boolean> deathCoords  = new Setting<>("DeathCords", true, this);
    public Setting<Boolean> clipBoard  = new Setting<>("Clipboard", true, this);
    public Setting<Boolean> deathSounds  = new Setting<>("DeathSounds", true, this);


    public AutoRespawn(){super("DeathFilter",Category.MISC);
        enabled = this.isEnabled;}

    @SubscribeEvent
    public void onDisplayDeathScreen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (this.deathCoords.getValue().booleanValue() && event.getGui() instanceof GuiGameOver) {
                if(clipBoard.getValue()){
                MessageUtil.sendClientMessage(String.format(ChatFormatting.BLUE + "[COPIED TO CLIPBOARD] "+ChatFormatting.RESET + " You died at x %d y %d z %d", (int) AutoRespawn.mc.player.posX, (int) AutoRespawn.mc.player.posY, (int) AutoRespawn.mc.player.posZ),-1117);}else {
                    MessageUtil.sendClientMessage(String.format(" You died at x %d y %d z %d", (int) AutoRespawn.mc.player.posX, (int) AutoRespawn.mc.player.posY, (int) AutoRespawn.mc.player.posZ),-1117);
                }
                final StringSelection contents = new StringSelection (String.format("You died at x %d y %d z %d", (int) AutoRespawn.mc.player.posX, (int) AutoRespawn.mc.player.posY, (int) AutoRespawn.mc.player.posZ));
                if(clipBoard.getValue()){
                    final Clipboard clipboard = Toolkit.getDefaultToolkit ( ).getSystemClipboard ( );
                    clipboard.setContents ( contents , null );}
            }
            if (this.respawn.getValue() != false && AutoRespawn.mc.player.getHealth() <= 0.0f || this.antiDeathScreen.getValue().booleanValue() && AutoRespawn.mc.player.getHealth() > 0.0f) {
                event.setCanceled(true);
                AutoRespawn.mc.player.respawnPlayer();
                final StringSelection contents = new StringSelection (String.format("You died at x %d y %d z %d", (int) AutoRespawn.mc.player.posX, (int) AutoRespawn.mc.player.posY, (int) AutoRespawn.mc.player.posZ));
                if(clipBoard.getValue()){
                    final Clipboard clipboard = Toolkit.getDefaultToolkit ( ).getSystemClipboard ( );
                    clipboard.setContents ( contents , null );}
            }
            if(deathSounds.getValue() && event.getGui() instanceof GuiGameOver){
                SoundUtill.playSound(new ResourceLocation("firework/audio/loaded.wav"));
            }
        }
    }
}
