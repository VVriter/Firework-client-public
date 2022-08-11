package com.firework.client.Implementations.Events.Entity;


import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

public class EntityPushEvent extends Event {
    private final Entity entity;
    public EntityPushEvent(Entity entity){
        this.entity = entity;
    }

    public Entity getEntity(){
        return this.entity;
    }
}
