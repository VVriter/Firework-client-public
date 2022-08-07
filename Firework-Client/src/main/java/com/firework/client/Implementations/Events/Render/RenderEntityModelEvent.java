package com.firework.client.Implementations.Events.Render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import ua.firework.beet.Event;

import java.util.function.Consumer;

public class RenderEntityModelEvent extends Event {
    private final Entity entity;
    private final Consumer<Object> renderAction;

    public RenderEntityModelEvent(Stage stage, Entity entity, Consumer<Object> renderAction){
        super(stage);
        this.entity = entity;
        this.renderAction = renderAction;
    }

    public Entity getEntity(){
        return this.entity;
    }

    public void render(){
        renderAction.accept(new Object());
    }
}
