package com.firework.client.Features.Modules.Client;

import com.firework.client.Features.Modules.Module;
import com.firework.client.Implementations.Settings.Setting;
import com.firework.client.Implementations.Utill.Chat.MessageUtil;
import com.firework.client.Implementations.Utill.Client.SoundUtill;
import net.minecraft.util.ResourceLocation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Notifications extends Module {

        public static Setting<Boolean> enabled = null;
        public static Setting<Boolean> sounds = null;
        public static Setting<Boolean> burrowNotificator = null;


    public Notifications(){super("Notifications",Category.CLIENT);
        enabled = this.isEnabled;
        this.isEnabled.setValue(true);
        sounds = new Setting<>("Sounds", true, this);
        burrowNotificator = new Setting<>("Burrow", true, this);}


    public static void notificate(){
        if(Notifications.sounds.getValue() && Notifications.enabled.getValue()){
            SoundUtill.playSound(new ResourceLocation("firework/audio/pop.wav"));}
    }


    private final ConcurrentHashMap<EntityPlayer, Integer> players = new ConcurrentHashMap<>();
    private final List<EntityPlayer> anti_spam = new ArrayList<>();

    public void update() {
        if(mc.player == null || mc.world == null) return;

        for (EntityPlayer player : mc.world.playerEntities) {
            if (anti_spam.contains(player)) continue;
            BlockPos pos = new BlockPos(player.posX, player.posY + 0.2D, player.posZ);
            if (mc.world.getBlockState(pos).getBlock().equals(Blocks.OBSIDIAN)) {
                add_player(player);
                anti_spam.add(player);
            }
        }
    }

    private void add_player(EntityPlayer player) {
        if (player == null) return;
        if (players.containsKey(player)) {
            int value = players.get(player) + 1;
            players.put(player, value);
            if(burrowNotificator.getValue()){
                notificate();
            MessageUtil.warning(player.getName() + " has burrowed " + value + " times",-1117);}
        } else {
            players.put(player, 1);
            if(burrowNotificator.getValue()){
                notificate();
            MessageUtil.warning(player.getName()  + " has burrowed " ,-1117);}
        }
    }
}
