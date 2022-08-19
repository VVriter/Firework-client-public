package com.firework.client.Implementations.Events;

import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

public class DeathEvent extends Event {
    private final Entity deathEntity;
    public DeathEvent(Entity entity) {
        this.deathEntity = entity;
    }

    public Entity getEntity() {
        return deathEntity;
    }
}
