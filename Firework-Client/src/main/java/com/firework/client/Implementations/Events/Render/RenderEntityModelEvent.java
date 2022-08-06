package com.firework.client.Implementations.Events.Render;

import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

public class RenderEntityModelEvent extends Event {
    private final Entity entity;

    public RenderEntityModelEvent(Stage stage, Entity entity){
        super(stage);
        this.entity = entity;
    }

    public Entity getEntity(){
        return this.entity;
    }
}
