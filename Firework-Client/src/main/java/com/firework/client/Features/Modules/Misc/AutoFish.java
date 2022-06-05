package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleArgs;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;

import com.firework.client.Implementations.Utill.Client.DiscordWebhook;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;


import net.minecraft.util.*;
import net.minecraft.network.play.client.*;
import net.minecraft.network.*;

import java.awt.*;
import java.util.Arrays;
import java.util.Random;


@ModuleArgs(name = "AutoFish",category = Module.Category.MISC)
public class AutoFish extends Module {

    DiscordWebhook simpleDiscordWebhook;
    Color embedColor = new Color(191, 10, 10);

    private final Random random = new Random();


    public  Setting<Boolean> enabled = this.isEnabled;

    public Setting<String> mode  = new Setting<>("Mode", "Normal", this, Arrays.asList("Normal","Advanced"));
    public Setting<String> swith  = new Setting<>("Switch", "Normal", this, Arrays.asList("Normal","Silent","None"));
    public Setting<Boolean> swing  = new Setting<>("Swing", true, this);
    public Setting<Boolean> antiAfk  = new Setting<>("AntiAFK", true, this);

    @Override
    public void onEnable(){
        super.onEnable();
        if(mode.getValue().equals("Normal")){
            MessageUtil.sendClientMessage("You need to press right click to start fishing",false);
        }
        if(mode.getValue().equals("Advanced")){
            MessageUtil.sendClientMessage("You need to press right click to start fishing",false);
            sendEnabledMsg();
        }
        if(!mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD) && swith.getValue().equals("Normal")){
            makeNormalSwitch();
        }else {
            if((mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD))))){
                MessageUtil.sendError("You need to hold Fishing rod in mainhand!",-1117);
                enabled.setValue(false);
            }
        }
    }


    @SubscribeEvent
    public void antiAFK(PacketEvent.Receive e){
        if(antiAfk.getValue()){
            int randomNumber = random.nextInt(500);
            switch (randomNumber) {
                case 0: pressAndUnpress(mc.gameSettings.keyBindLeft.getKeyCode(), random.nextInt(200)); break;
                case 100: pressAndUnpress(mc.gameSettings.keyBindRight.getKeyCode(), random.nextInt(200)); break;
                case 200:if(mc.player.onGround) mc.player.jump(); break;
                case 300: pressAndUnpress(mc.gameSettings.keyBindSneak.getKeyCode(), random.nextInt(200)); break;
            }
        }
    }



    @Override
    public void onTick(){
        super.onTick();

        //Atm swing animations
        if(swing.getValue()){
            mc.player.isSwingInProgress = false;
            mc.player.swingProgressInt = 0;
            mc.player.swingProgress = 0.0f;
            mc.player.prevSwingProgress = 0.0f;
        }

        //Mode
        if(mode.getValue().equals("Advanced")){
            putIsWebhookLinked();
            if(isWebhookPresent){
                //Ok
            }else{
                MessageUtil.sendError("Webhook is not present, use "+ CommandManager.prefix+"webhook to set webhook link",-1117);
                enabled.setValue(false);
            }
        }


        //Switch
        if(swith.getValue().equals("Normal")){
            makeNormalSwitch();
        }else if(swith.getValue().equals("Silent")){
            System.out.println("silent");
        }else {
            if((mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD))))){
                MessageUtil.sendError("You need to hold Fishing rod in mainhand!",-1117);
                enabled.setValue(false);}
        }
    }

    @SubscribeEvent
    public void onItemPickUp(PlayerEvent.ItemPickupEvent e){
        if(mode.getValue().equals("Advanced")){
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                                embed.setTitle("You picked up an item: "+e.getStack().getItem().getItemStackDisplayName(e.getStack()));
                                embed.setThumbnail("https://minecraft-archive.fandom.com/wiki/Items/Gallery?file="+e.getStack().getItem().getItemStackDisplayName(e.getStack())+"New.png");
                                embed.setColor(embedColor);

                                simpleDiscordWebhook.addEmbed(embed);

                                try {
                                    simpleDiscordWebhook.execute();
                                } catch (Exception e) {

                                }
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
        }
    }

    @SubscribeEvent
    public void autoFish(PacketEvent e){
        if((mode.getValue().equals("Advanced") && isWebhookPresent) || mode.getValue().equals("Normal")){
            if(e.getPacket() instanceof SPacketSoundEffect) {
                final SPacketSoundEffect packet = (SPacketSoundEffect)e.getPacket();
                if (packet.getSound().equals(SoundEvents.ENTITY_BOBBER_SPLASH)) {
                    if (mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        mc.player.swingArm(EnumHand.MAIN_HAND);
                    }
                    if (mc.player.getHeldItemOffhand().getItem() instanceof ItemFishingRod) {
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                        mc.player.swingArm(EnumHand.OFF_HAND);
                        mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
                        mc.player.swingArm(EnumHand.OFF_HAND);
                    }
                }
            }

        }
    }




    public boolean isWebhookPresent = false;


    public void putIsWebhookLinked() {
        isWebhookPresent = !DiscordNotificator.webhook.equals("");
        simpleDiscordWebhook = new DiscordWebhook(DiscordNotificator.webhook);
    }

    public void makeNormalSwitch(){
        if ( (mc.player.getHeldItemMainhand().getItem() == null || (!(mc.player.inventory.getCurrentItem().getItem().equals(Items.FISHING_ROD)) )))
            for (int j = 0; j < 9; j++) {
                if (mc.player.inventory.getStackInSlot(j) != null && mc.player.inventory.getStackInSlot(j).getCount() != 0 && mc.player.inventory.getStackInSlot(j).getItem() instanceof ItemFishingRod) {
                    mc.player.inventory.currentItem = j;
                    break;
                }
            }
    }

    public void sendEnabledMsg(){
        new Thread(
                new Runnable() {
                    public void run() {
                        try {
                            DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                            embed.setTitle("AutoFish module mod advanced is enabled!");
                            embed.setThumbnail("https://media.discordapp.net/attachments/962299429795295282/980160689568120842/98be7b5d87318d89133a7384346cf787.jpg");
                            embed.setColor(embedColor);

                            simpleDiscordWebhook.addEmbed(embed);

                            try {
                                simpleDiscordWebhook.execute();
                            } catch (Exception e) {

                            }
                        }catch (Exception e){
                            MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                        }
                    }
                }).start();
    }

    @Override
    public void onDisable(){
        super.onDisable();
        if(mode.getValue().equals("Advanced")){
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                                embed.setTitle("AutoFish module mod advanced is disabled!");
                                embed.setThumbnail("https://media.discordapp.net/attachments/962299429795295282/980160689568120842/98be7b5d87318d89133a7384346cf787.jpg");

                                embed.setColor(embedColor);

                                simpleDiscordWebhook.addEmbed(embed);

                                try {
                                    simpleDiscordWebhook.execute();
                                } catch (Exception e) {

                                }
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
        }
    }
    private void pressAndUnpress(int key, int delay) {
        new Thread(() -> {
            try {
                KeyBinding.setKeyBindState(key, true);
                Thread.sleep(delay);
                KeyBinding.setKeyBindState(key, false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
