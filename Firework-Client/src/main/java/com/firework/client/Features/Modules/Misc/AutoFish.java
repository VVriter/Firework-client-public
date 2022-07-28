package com.firework.client.Features.Modules.Misc;

import com.firework.client.Features.CommandsSystem.CommandManager;
import com.firework.client.Features.Modules.Client.DiscordNotificator;
import com.firework.client.Features.Modules.Module;
import com.firework.client.Features.Modules.ModuleManifest;
import com.firework.client.Implementations.Events.OnFishingEvent;
import com.firework.client.Implementations.Events.PacketEvent;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.DiscordWebhook;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.EnumHand;
import ua.firework.beet.Listener;
import ua.firework.beet.Subscribe;

import java.awt.*;
import java.util.Date;

@ModuleManifest(
        name = "AutoFish",
        category = Module.Category.MISCELLANEOUS,
        description = "Automatically fishing"
)
public class AutoFish extends Module {

    @Subscribe
    public Listener<PacketEvent.Receive> listener1 = new Listener<>(e -> {
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
    );

    public Setting<Boolean> discord = new Setting<>("Discord", true, this);

    String WEBHOOK = DiscordNotificator.webhook;

    Date dateFromStart;
    Date dateFromEnd;
    Date dateFishingTime;


    @Override
    public void onEnable() {
        super.onEnable();
        dateFromStart = new Date();


        if (discord.getValue()) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordWebhook discordWebhookOnEnable = new DiscordWebhook(WEBHOOK);
                                DiscordWebhook.EmbedObject onEnableEmbed = new DiscordWebhook.EmbedObject();
                                onEnableEmbed.setColor(Color.green);
                                onEnableEmbed.setTitle("AutoFish module is enabled now!");
                                onEnableEmbed.setThumbnail("https://static.wikia.nocookie.net/minecraftdungeons/images/e/ec/FishingRod%283D%29.png/revision/latest?cb=20200408200809");
                                onEnableEmbed.addField("Time started fishing",dateFromStart.toString(),false);
                                discordWebhookOnEnable.addEmbed(onEnableEmbed);
                                discordWebhookOnEnable.execute();
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
        }
    }


    @Override
    public void onDisable() {
        super.onDisable();
        dateFromEnd = new Date();
        if (discord.getValue()) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordWebhook discordWebhookOnDisable = new DiscordWebhook(WEBHOOK);
                                DiscordWebhook.EmbedObject onDisableEmbed = new DiscordWebhook.EmbedObject();
                                onDisableEmbed.setColor(Color.red);
                                onDisableEmbed.setTitle("AutoFish module is disabled now!");
                                onDisableEmbed.setThumbnail("https://static.wikia.nocookie.net/minecraftdungeons/images/e/ec/FishingRod%283D%29.png/revision/latest?cb=20200408200809");
                                onDisableEmbed.addField("Time fishing ended",dateFromEnd.toString(),false);
                                discordWebhookOnDisable.addEmbed(onDisableEmbed);
                                discordWebhookOnDisable.execute();
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
        }
    }

    @Subscribe
    public Listener<OnFishingEvent> onFish = new Listener<>(e -> {

        ItemStack item = e.getDrops().get(0);

        if (discord.getValue()) {
            new Thread(
                    new Runnable() {
                        public void run() {
                            try {
                                DiscordWebhook webhook = new DiscordWebhook(WEBHOOK);
                                DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();
                                embed.setColor(new Color(153, 26, 199,255));
                                embed.setThumbnail("https://static.wikia.nocookie.net/minecraftdungeons/images/e/ec/FishingRod%283D%29.png/revision/latest?cb=20200408200809");
                                embed.setTitle("Fished new item!");
                                embed.addField("Time fishing", getTimeNow(),true);
                                embed.addField("Fished item", item.getDisplayName(),true);
                                embed.addField("Rod damage","Damage: "+ e.getRodDamage(),true);
                                embed.addField("Enchants", String.valueOf(item.getEnchantmentTagList()),true);
                                webhook.addEmbed(embed);
                                webhook.execute();
                            }catch (Exception e){
                                MessageUtil.sendError("Webhook is invalid, use "+ CommandManager.prefix+"webhook webhook link to link ur webhook",-1117);
                            }
                        }
                    }).start();
            }
        }
    );


    public String getTimeNow(){
        dateFishingTime = new Date();
        long diff = dateFishingTime.getTime() - dateFromStart.getTime();

        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        return "Hours:"+diffHours+" Minutes:"+diffMinutes+" Seconds:"+diffSeconds;
    }
}