package com.firework.client.Implementations.Events.Entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import ua.firework.beet.Event;

public class EntityMoveEvent extends Event {
    private final AxisAlignedBB bb;
    private final Entity entity;
    private final float step;

    public EntityMoveEvent(AxisAlignedBB bb, Entity entity, float step) {
        this.bb = bb;
        this.entity = entity;
        this.step = step;
    }

    public AxisAlignedBB getBB(){
        return this.bb;
    }

    public Entity getEntity(){
        return this.entity;
    }

    public float getStep() {
        return step;
    }
}
