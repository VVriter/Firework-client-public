package com.firework.client.Implementations.Events.Player;

import net.minecraft.entity.player.EntityPlayer;
import ua.firework.beet.Event;

public class PlayerRespawnEvent extends Event {

    private final EntityPlayer player;

    public PlayerRespawnEvent(EntityPlayer player){
        this.player = player;
    }

    public EntityPlayer getPlayer(){
        return this.player;
    }
}
