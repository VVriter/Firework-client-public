package com.firework.client.Implementations.Events.Entity;

import net.minecraft.entity.EntityLivingBase;
import ua.firework.beet.Event;

public class LivingUpdateEvent extends Event {
    private final EntityLivingBase entityLivingBase;

    public LivingUpdateEvent(EntityLivingBase entityLivingBase){
        this.entityLivingBase = entityLivingBase;
    }

    public EntityLivingBase getEntityLivingBase(){
        return this.entityLivingBase;
    }
}
