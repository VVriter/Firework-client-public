package com.firework.client.Implementations.Events.Player;

import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

public class TotemPopEvent extends Event {
    Entity player;
    public TotemPopEvent(Entity player) {
        this.player = player;
    }

    public Entity getEntity() {
        return player;
    }
}
